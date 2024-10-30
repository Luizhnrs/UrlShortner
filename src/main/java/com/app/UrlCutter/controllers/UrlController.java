package com.app.UrlCutter.controllers;

import com.app.UrlCutter.entities.Url;
import com.app.UrlCutter.repositories.UrlRepository;
import com.app.UrlCutter.requests.UrlRequest;
import com.app.UrlCutter.responses.UrlResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
public class UrlController {
    private final UrlRepository urlRepository;

    public UrlController(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }


    @PostMapping(value = "/urlcutter")
    public ResponseEntity<UrlResponse> urlCutter(@RequestBody UrlRequest request,
                                                 HttpServletRequest servletRequest) {
        String id;
        do {
           id = RandomStringUtils.randomAlphanumeric(5, 10);
        } while (urlRepository.existsById(id));

        urlRepository.save(new Url(id, request.url(), LocalDateTime.now().plusMinutes(1)));
        var redirectUrl = servletRequest.getRequestURL().toString().replace("urlcutter/", id);

        return ResponseEntity.ok(new UrlResponse(redirectUrl));
    }

    @GetMapping("{id}")
    public ResponseEntity<Void> redirect(@PathVariable("id") String id) {

        var url = urlRepository.findById(id);

        if (url.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url.get().getFullUrl()));

        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }


}
