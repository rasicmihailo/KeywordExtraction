package com.elfak.keywordextraction.contoller;

import com.elfak.keywordextraction.engine.*;
import com.elfak.keywordextraction.model.Advertisement;
import com.elfak.keywordextraction.model.Keyword;
import com.elfak.keywordextraction.model.TrainTest;
import com.elfak.keywordextraction.repository.AdvertisementRepository;
import com.elfak.keywordextraction.repository.KeywordRepository;
import com.elfak.keywordextraction.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class KeywordExtractionController {

    private final Utilities utilities;
    private final AdvertisementRepository advertisementRepository;
    private final KeywordRepository keywordRepository;
    private final TermRepository termRepository;

    @PostMapping("/train")
    public List<Keyword> train(@RequestBody TrainTest txt) throws IOException {
        advertisementRepository.deleteAll();
        termRepository.deleteAll();
        keywordRepository.deleteAll();

        List<Keyword> keywordsFound = utilities.trainKeywords(txt.getText());

        // maksimalno 100 keyword-ova
        keywordsFound.stream().limit(100).forEach(keyword -> {
            // ne uzimamo u obzir reci sa frekvencijom manjom od 2
            if (keyword.getFrequency() > 1) {
                // ne smemo rec bez da izbacimo pre ovog trenutka jer koristimo u funkciji Utilities.trueOrFalse()
                if (!keyword.getStem().equals("bez") && !keyword.getStem().equals("stop11stop11stop")) {
                    Keyword keywordSaved = keywordRepository.save(keyword);
                    keyword.getTerms().stream().forEach(term -> term.setKeyword(keywordSaved));
                    termRepository.saveAll(keyword.getTerms());
                }
            }
        });

        utilities.trainAds(txt.getText());

        return keywordRepository.findAll();
    }

    @PostMapping("/check")
    public List<Keyword> check(@RequestBody TrainTest txt) throws IOException {
        return utilities.testKeywords(txt.getText());
    }

    @PostMapping("/check-ads")
    public List<Advertisement> checkAds(@RequestBody TrainTest txt) {
        return utilities.testAds(txt.getText());
    }

    @GetMapping("/keywords")
    public List<Keyword> keyword() {
        return keywordRepository.findAll();
    }

    @GetMapping("/search")
    public List<Advertisement> search(@RequestParam String s) throws IOException {
        return utilities.searchAds(s);
    }
}
