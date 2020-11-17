package com.elfak.keywordextraction.engine;

import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class StopSet {

    List<String> serbianStopSet = new ArrayList<>();
    List<String> serbianStopSetAfter = new ArrayList<>();

    @PostConstruct
    private void initStore() {
        // reci koje izbacujemo - veznici pretezno
        serbianStopSet.add("i");
        serbianStopSet.add("na");
        serbianStopSet.add("u");
        serbianStopSet.add("sve");
        serbianStopSet.add("kao");
        serbianStopSet.add("ili");
        serbianStopSet.add("pa");
        serbianStopSet.add("te");
        serbianStopSet.add("to");
        serbianStopSet.add("ti");
        serbianStopSet.add("on");
        serbianStopSet.add("a");
        serbianStopSet.add("u");
        serbianStopSet.add("o");
        serbianStopSet.add("vec");
        serbianStopSet.add("su");
        serbianStopSet.add("je");
        serbianStopSet.add("se");
        serbianStopSet.add("od");
        serbianStopSet.add("do");
        serbianStopSet.add("sa");
        serbianStopSet.add("za");
        serbianStopSet.add("svi");
        serbianStopSet.add("ovo");
        serbianStopSet.add("ovom");
        serbianStopSet.add("ono");
        serbianStopSet.add("mozet");
        serbianStopSet.add("da");
        serbianStopSet.add("ima");
        serbianStopSet.add("iz");
        serbianStopSet.add("saljem");
        serbianStopSet.add("samo");
        serbianStopSet.add("uz");
        serbianStopSet.add("moze");
        serbianStopSet.add("po");

        serbianStopSetAfter.add("ne");
        serbianStopSetAfter.add("ni");
        serbianStopSetAfter.add("no");
        serbianStopSetAfter.add("bez");
    }
}
