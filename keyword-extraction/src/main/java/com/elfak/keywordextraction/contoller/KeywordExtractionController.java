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

    private final Engine engine;
    private final AdvertisementRepository advertisementRepository;
    private final KeywordRepository keywordRepository;
    private final TermRepository termRepository;

    @PostMapping("/train")
    public List<Keyword> train(@RequestBody TrainTest txt) throws IOException {
        advertisementRepository.deleteAll();
        termRepository.deleteAll();
        keywordRepository.deleteAll();

        engine.trainKeywords(txt.getText());
        engine.trainAds(txt.getText());

        return keywordRepository.findAll();
    }

    @PostMapping("/check-keywords")
    public List<Keyword> check(@RequestBody TrainTest txt) throws IOException {
        return engine.testKeywords(txt.getText());
    }

    @PostMapping("/check-ads")
    public List<Advertisement> checkAds(@RequestBody TrainTest txt) {
        return engine.testAds(txt.getText());
    }

    @GetMapping("/keywords")
    public List<Keyword> keyword() {
        return keywordRepository.findAll();
    }

    @GetMapping("/search")
    public List<Advertisement> search(@RequestParam String s) throws IOException {
        return engine.searchAds(s);
    }
}
