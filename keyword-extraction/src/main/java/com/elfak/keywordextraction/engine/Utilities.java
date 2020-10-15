package com.elfak.keywordextraction.engine;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

@Service
@RequiredArgsConstructor
public class Utilities {

    private final Store store;

    public String stem(String term) throws IOException {

        TokenStream tokenStream = null;
        try {

            // tokenizer
            ClassicTokenizer classicTokenizer = new ClassicTokenizer();
            classicTokenizer.setReader(new StringReader(term));

            tokenStream = new PorterStemFilter(classicTokenizer);

            // set za korene reci (zbog duplikata)
            Set<String> stems = new HashSet<String>();
            CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                stems.add(token.toString());
            }

            // ako je koren manji od 2
            if (stems.size() != 1) {
                return null;
            }
            String stem = stems.iterator().next();
            // ako koren nema brojeve i slova
            if (!stem.matches("[a-zA-Z0-9-]+")) {
                return null;
            }

            return stem;

        } finally {
            if (tokenStream != null) {
                tokenStream.close();
            }
        }

    }

    public <T> T find(Collection<T> collection, T example) {
        for (T element : collection) {
            if (element.equals(example)) {
                return element;
            }
        }
        collection.add(example);
        return example;
    }

    // ne dodajemo rec u Store
    public <T> T findOnly(Collection<T> collection, T example) {
        for (T element : collection) {
            if (element.equals(example)) {
                return element;
            }
        }
        return null;
    }

    public List<Keyword> guessFromString(String input) throws IOException {

        TokenStream tokenStream = null;
        try {
            // improvizacija za spajanje prideva sa imenicom
            input = input.replaceAll("ni ", "ni-");
            input = input.replaceAll("nji ", "nji-");

            // hack - kodiranje novog reda ---> ubuduce moze i kodiranje novo oglasa
            input = input.replaceAll("\n", " newnewnewnew ");

            // hack - kodiranje poluslozenica ("pro-evropski" umesto "pro" i "evropski")
            input = input.replaceAll("-+", "abc55abc55abc");
            // izbacivanje znakova interpunkcije osim apostrofa i povlaka
            input = input.replaceAll("[\\p{Punct}&&[^'-]]+", " ");
            // izbacinja najcescih engleskih skracenica
            input = input.replaceAll("(?:'(?:[tdsm]|[vr]e|ll))+\\b", "");

            // izbacivanje reci sa jednim karakterom ---> ubuduce mozda ne izbacivati brojeve
            input = input.replaceAll(" [a-zA-Z0-9] ", " ");

            // tokenizer
            ClassicTokenizer classicTokenizer = new ClassicTokenizer();
            classicTokenizer.setReader(new StringReader(input));
            // to lowercase
            tokenStream = new LowerCaseFilter(classicTokenizer);
            // engleski filter - remove dots from acronyms (and "'s" but already done manually above)
            tokenStream = new ClassicFilter(tokenStream);
            // konverzija u ASCII
            tokenStream = new ASCIIFoldingFilter(tokenStream);
            // izbacivanje engleskog stop set-a
            tokenStream = new StopFilter(tokenStream, EnglishAnalyzer.getDefaultStopSet());

            List<Keyword> keywords = new LinkedList<Keyword>();
            CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
            tokenStream.reset();

            // analiziramo prethodnu rec, tekucu rec i narednu rec
            String termOld = "";
            String term = "";
            String termNew = "";
            tokenStream.incrementToken();
            term = token.toString();

            while (tokenStream.incrementToken()) {
                termNew = token.toString();
                // korenovanje
                String stem = stem(term);
                // druga provera: izbacujemo reci koje sadrze samo brojeve
                if (stem != null && !tryParseInt(stem)) {
                    stem = stem.replaceAll("abc55abc55abc", "-");
                    if (!stem.equals("newnewnewnew")) {
                        // kreiranje kljucne reci ili pronalazanje postojece
                        Keyword keyword = find(keywords, new Keyword(stem));
                        // drugi parametar (isBoolean) je true ako ni prethodna, ni naredna rec ne sadrze ni jedan broj u sebi, a false ako bar jedna od prethodne i naredne reci sadrzi broj
                        keyword.add(
                                term.replaceAll("abc55abc55abc", "-"),
                                !(termOld.replaceAll("abc55abc55abc", "-").matches("^(?=.*\\d).+$")
                                        || termNew.replaceAll("abc55abc55abc", "-").matches("^(?=.*\\d).+$")));
                    }
                }

                termOld = term;
                term = termNew;
            }

            // izbacivanje veznika
            for (String stop : this.store.getSerbianStopSet()) {
                for (Keyword keyword : keywords) {
                    if (keyword.getStem().equals(stop)) {
                        keywords.remove(keyword);
                        break;
                    }
                }
            }

            // sortiranje po frekvenciji
            Collections.sort(keywords);

            return keywords;

        } finally {
            if (tokenStream != null) {
                tokenStream.close();
            }
        }


    }

    public List<Keyword> check(String input) throws IOException {
        List<Keyword> store = this.store.getKeywords();
        TokenStream tokenStream = null;
        try {
            // improvizacija za spajanje prideva sa imenicom
            input = input.replaceAll("ni ", "ni-");
            input = input.replaceAll("nji ", "nji-");

            // hack - kodiranje novog reda ---> ubuduce moze i kodiranje novo oglasa
            input = input.replaceAll("\n", " newnewnewnew ");

            // hack - kodiranje poluslozenica ("pro-evropski" umesto "pro" i "evropski")
            input = input.replaceAll("-+", "abc55abc55abc");
            // izbacivanje znakova interpunkcije osim apostrofa i povlaka
            input = input.replaceAll("[\\p{Punct}&&[^'-]]+", " ");
            // izbacinja najcescih engleskih skracenica
            input = input.replaceAll("(?:'(?:[tdsm]|[vr]e|ll))+\\b", "");

            // izbacivanje reci sa jednim karakterom ---> ubuduce mozda ne izbacivati brojeve
            input = input.replaceAll(" [a-zA-Z0-9] ", " ");

            // tokenizer
            ClassicTokenizer classicTokenizer = new ClassicTokenizer();
            classicTokenizer.setReader(new StringReader(input));
            // to lowercase
            tokenStream = new LowerCaseFilter(classicTokenizer);
            // engleski filter - remove dots from acronyms (and "'s" but already done manually above)
            tokenStream = new ClassicFilter(tokenStream);
            // konverzija u ASCII
            tokenStream = new ASCIIFoldingFilter(tokenStream);
            // izbacivanje engleskog stop set-a
            tokenStream = new StopFilter(tokenStream, EnglishAnalyzer.getDefaultStopSet());

            List<Keyword> keywords = new LinkedList<Keyword>();
            CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
            tokenStream.reset();

            // analiziramo prethodnu rec, tekucu rec i narednu rec
            String termOld = "";
            String term = "";
            String termNew = "";
            tokenStream.incrementToken();
            // novi red, odnosno newnewnewnew, postavljamo na true
            term = token.toString().equals("newnewnewnew") ? "true" : token.toString();

            while (tokenStream.incrementToken()) {
                termNew = token.toString().equals("newnewnewnew") ? "true" : token.toString();
                // korenovanje
                String stem = stem(term);
                // druga provera: izbacujemo reci koje sadrze samo brojeve
                if (stem != null && !tryParseInt(stem)) {
                    // kreiranje kljucne reci ili pronalazanje postojece
                    Keyword keyword = findOnly(store, new Keyword(stem.replaceAll("abc55abc55abc", "-")));
                    // findOnly moze da vrati null, ako u Store ne postoji ta rec
                    if (keyword != null) {
                        // zabranjujemo dupliranje reci
                        if (!keywords.contains(keyword)) {
                            // mozda iz Keyword izbaciti isBoolean i testirati na licu mesta da li leva i desna rec sadrze brojke pa zavisno od toga postavljati keywowrd.value
                            if (keyword.isBoolean()) {
                                // keyword.value postaje true ako ni prethodna, ni naredna rec nisu neka od negacija, inace false
                                keyword.setValue(trueOrFalse(termOld.replaceAll("abc55abc55abc", "-"), termNew.replaceAll("abc55abc55abc", "-")));
                            } else {
                                // keyword.value postaje prethodna ili naredna rec, u zavisnosti od toga koja ima vise brojeva (mozda i 0, onda se uzima naredna)
                                keyword.setValue(stringWithMoreDigits(termOld.replaceAll("abc55abc55abc", "-"), termNew.replaceAll("abc55abc55abc", "-")));
                            }
                            keywords.add(keyword);
                        }
                    }
                }

                termOld = term;
                term = termNew;
            }

            // sortiranje po frekvenciji
            Collections.sort(keywords);

            return keywords;

        } finally {
            if (tokenStream != null) {
                tokenStream.close();
            }
        }
    }

    //funkcija koja vraca prethodnu ili narednu rec u zavisnosti od toga koja ima vise brojeva
    private String stringWithMoreDigits(String str1, String str2) {
        int count1 = 0;
        for (int i = 0, len = str1.length(); i < len; i++) {
            if (Character.isDigit(str1.charAt(i))) {
                count1++;
            }
        }
        int count2 = 0;
        for (int i = 0, len = str2.length(); i < len; i++) {
            if (Character.isDigit(str2.charAt(i))) {
                count2++;
            }
        }
        if (count1 > count2) {
            return str1;
        } else {
            return str2;
        }
    }

    // funkcija koja proverava da li prethodna ili naredna rec oznacavaju negaciju
    private String trueOrFalse(String str1, String str2) {
        if ((str1.startsWith("ne") && str1.length() < 5) || (str2.startsWith("ne") && str2.length() < 5))
            return "false";
        if ((str1.startsWith("ni") && str1.length() < 5) || (str2.startsWith("ni") && str2.length() < 5))
            return "false";
        if ((str1.startsWith("no") && str1.length() < 5) || (str2.startsWith("no") && str2.length() < 5))
            return "false";
        if (str1.equals("bez"))
            return "false";
        return "true";
    }

    // pomocna funkcija da izbacimo reci koje sadrze samo brojeve
    private boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
