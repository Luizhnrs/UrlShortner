package com.app.UrlCutter.controllers;

import com.app.UrlCutter.entities.Url;
import com.app.UrlCutter.repositories.UrlRepository;
import com.app.UrlCutter.requests.UrlRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class UrlController {
    private final UrlRepository urlRepository;

    public UrlController(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }


    @PostMapping(value = "/urlcutter")
    public ResponseEntity<Void> urlCutter(@RequestBody UrlRequest request) {
        String id;
        do {
           id = RandomStringUtils.randomAlphanumeric(5, 10);
        } while (urlRepository.existsById(id));

        urlRepository.save(new Url(id, request.url(), LocalDateTime.now().plusMinutes(1)));
        return ResponseEntity.ok().build();
    }
}
