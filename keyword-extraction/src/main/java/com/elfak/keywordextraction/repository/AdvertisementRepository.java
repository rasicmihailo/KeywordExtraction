package com.elfak.keywordextraction.repository;

import com.elfak.keywordextraction.model.Advertisement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisementRepository extends CrudRepository<Advertisement, Long> {
}
