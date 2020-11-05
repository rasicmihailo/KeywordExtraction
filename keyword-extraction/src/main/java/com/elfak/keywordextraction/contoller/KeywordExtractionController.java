package com.elfak.keywordextraction.contoller;

import com.elfak.keywordextraction.engine.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
        store.getAdvertisements().clear();

        utilities.guessFromString(txt.getText()).forEach(keyword -> {
            // dinamicki odredjujemo koja frekvencija je dovoljna da bismo uzimali rec u obzir
            if (keyword.getFrequency() > 5) {
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

    @PostMapping("/check")
    public List<Keyword> check(@RequestBody TrainTest txt) throws IOException {
        return utilities.check(txt.getText());
    }

    @PostMapping("/check-ads")
    public List<Advertisement> checkAds(@RequestBody TrainTest txt) {
        return utilities.checkAd(txt.getText());
    }

    @GetMapping("/search")
    public List<Advertisement> search(@RequestParam String s) {
        return utilities.getAds(s);
    }
}
