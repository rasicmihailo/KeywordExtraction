package com.elfak.keywordextraction.repository;

import com.elfak.keywordextraction.model.Term;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermRepository extends CrudRepository<Term, Long> {
}
