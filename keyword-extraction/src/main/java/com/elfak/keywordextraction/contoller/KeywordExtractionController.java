package com.elfak.keywordextraction.contoller;

import com.elfak.keywordextraction.engine.Keyword;
import com.elfak.keywordextraction.engine.Store;
import com.elfak.keywordextraction.engine.Utilities;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class KeywordExtractionController {

    private final Utilities utilities;
    private final Store store;

    @GetMapping("/train")
    public String train(@RequestBody String txt) throws IOException {

        store.getKeywords().clear();
        StringBuilder stringBuilder = new StringBuilder();

        utilities.guessFromString(txt).forEach(keyword -> {
            // dinamicki odredjujemo koja frekvencija je dovoljna da bismo uzimali rec u obzir
            if (keyword.getFrequency() > txt.length()/2000 || (keyword.getFrequency() > txt.length()/4000 && !keyword.isBoolean())) {
                // ne smemo rec bez da izbacimo pre ovog trenutka jer koristimo u funkciji Utilities.trueOrFalse()
                if (!keyword.getStem().equals("bez")) {
                    stringBuilder.append(keyword.toString() + "\n");
                    store.getKeywords().add(keyword);
                }
            }
        });

        return stringBuilder.toString();
    }

    @GetMapping("/keywords")
    public List<Keyword> keyword() {
        return store.getKeywords();
    }

    @GetMapping("/check")
    public String check(@RequestBody String txt) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        utilities.check(txt).forEach(keyword -> {
            stringBuilder.append(keyword.print() + "\n");
        });
        return stringBuilder.toString();
    }
}
