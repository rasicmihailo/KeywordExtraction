package com.elfak.keywordextraction.engine;

import lombok.Builder;

import java.util.List;

@Builder
public class Ad {
    String title;
    String content;
    List<Keyword> keywords;
}
