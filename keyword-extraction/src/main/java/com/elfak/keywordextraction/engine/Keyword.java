package com.elfak.keywordextraction.engine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Keyword implements Comparable<Keyword> {

    private final String stem;
    private final Set<String> terms = new HashSet<String>();
    private int frequency = 0;
    // broj term-a koji ne sadrze ni jednu brojku u reci pre i posle
    private int booleans = 0;
    // true ako je booleans*2 >= frequency
    private boolean isBoolean;
    // vrednost koja se dodeljuje u check(), ako je isBoolean=true onda value moze da bude true ili false, inace se uzima prethodna ili naredna rec (ona koja ima vise brojeva u sebi)
    private String value;

    public Keyword(String stem) {
        this.stem = stem;
    }

    public void add(String term, boolean isBoolean) {
        terms.add(term);
        frequency++;
        if (isBoolean) {
            booleans++;
        }
        // prilikom svakog dodavanja term-a azuriramo atribut isBoolean
        if (booleans*2 >= frequency) {
            this.isBoolean = true;
        } else {
            this.isBoolean = false;
        }
    }

    @Override
    public int compareTo(Keyword o) {
        // opadajuci redosled
        return Integer.valueOf(o.frequency).compareTo(frequency);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Keyword)) {
            return false;
        } else {
            return stem.equals(((Keyword) obj).stem);
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] { stem });
    }

    public String getStem() {
        return stem;
    }

    public Set<String> getTerms() {
        return terms;
    }

    public int getFrequency() {
        return frequency;
    }

    public boolean isBoolean() {
        return isBoolean;
    }


    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Keyword{" +
                "stem='" + stem + '\'' +
                ", terms=" + terms +
                ", frequency=" + frequency +
                ", isBoolean=" + isBoolean +
                '}';
    }

    public String print() {
        return "Keyword{" +
                "stem='" + stem + '\'' +
                ", value=" + value +
                '}';
    }
}
