package com.elfak.keywordextraction.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Data
@Entity
@Table(name = "keywords")
public class Keyword implements Comparable<Keyword> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String stem;

    @OneToMany(mappedBy = "keyword", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Term> terms = new HashSet<>();

    @Column
    private int frequency = 0;

    // broj term-a koji ne sadrze ni jednu brojku u reci pre i posle
    @Column
    private int booleans = 0;

    // true ako je booleans*2 >= frequency
    @Column
    private boolean isBoolean;

    // vrednost koja se dodeljuje u testKeywords(), ako je isBoolean=true onda value moze da bude true ili false, inace se uzima prethodna ili naredna rec (ona koja ima vise brojeva u sebi)
    @Transient
    private String value;

    @ManyToMany(mappedBy = "keywords", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Advertisement> advertisements;

    public Keyword(String stem) {
        this.stem = stem;
    }

    public void add(String term, boolean isBoolean) {
        terms.add(Term.builder().term(term).build());
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

    public Set<Term> getTerms() {
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
}
