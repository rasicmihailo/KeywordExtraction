package com.elfak.keywordextraction.contoller;

import com.elfak.keywordextraction.engine.Keyword;
import com.elfak.keywordextraction.engine.Store;
import com.elfak.keywordextraction.engine.TrainTest;
import com.elfak.keywordextraction.engine.Utilities;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class KeywordExtractionController {

    private final Utilities utilities;
    private final Store store;

    @PostMapping("/train")
    public List<Keyword> train(@RequestBody TrainTest txt) throws IOException {

        store.getKeywords().clear();
        store.getAds().clear();

        utilities.guessFromString(txt.getText()).forEach(keyword -> {
            // dinamicki odredjujemo koja frekvencija je dovoljna da bismo uzimali rec u obzir
            if (keyword.getFrequency() > txt.getText().length()/2000 || (keyword.getFrequency() > txt.getText().length()/4000 && !keyword.isBoolean())) {
                // ne smemo rec bez da izbacimo pre ovog trenutka jer koristimo u funkciji Utilities.trueOrFalse()
                if (!keyword.getStem().equals("bez") && !keyword.getStem().equals("stop11stop11stop")) {
                    store.getKeywords().add(keyword);
                }
            }
        });


        utilities.createAds(txt.getText());

        return store.getKeywords();
    }

    @GetMapping("/keywords")
    public List<Keyword> keyword() {
        return store.getKeywords();
    }

    @GetMapping("/check")
    public List<Keyword> check(@RequestBody String txt) throws IOException {
        List<Keyword> keywords = new ArrayList<>();
        utilities.check(txt).forEach(keyword -> {
            keywords.add(keyword);
        });
        return keywords;
    }
}
