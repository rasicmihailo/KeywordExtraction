package com.elfak.keywordextraction.repository;

import com.elfak.keywordextraction.model.Keyword;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends CrudRepository<Keyword, Long> {
    List<Keyword> findAll();
}
