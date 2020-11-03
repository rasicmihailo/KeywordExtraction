package com.elfak.keywordextraction.engine;

import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Advertisement implements Comparable<Advertisement> {
    String title;
    String content;
    List<Keyword> keywords;

    @Override
    public int compareTo(Advertisement advertisement) {
        Set<Keyword> result = this.keywords.stream()
                .distinct()
                .filter(advertisement.keywords::contains)
                .collect(Collectors.toSet());

        // sortiranje u opadajucem redosledu, zato negativan znak
        return -result.size();
    }

    @Override
    public String toString() {
        return "Ad{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", keywords=" + keywords +
                '}';
    }
}
