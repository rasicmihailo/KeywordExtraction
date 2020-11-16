package com.elfak.keywordextraction.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "advertisements")
public class Advertisement implements Comparable<Advertisement> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String title;

    @Column(columnDefinition = "longtext")
    private String content;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Keyword> keywords;

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
