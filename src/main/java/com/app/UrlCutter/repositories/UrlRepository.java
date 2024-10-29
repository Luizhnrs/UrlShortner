package com.app.UrlCutter.repositories;

import com.app.UrlCutter.entities.Url;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlRepository extends MongoRepository<Url, String> {

}
