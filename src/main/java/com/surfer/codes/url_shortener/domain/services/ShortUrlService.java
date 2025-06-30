package com.surfer.codes.url_shortener.domain.services;

import com.surfer.codes.url_shortener.domain.entities.ShortUrl;
import com.surfer.codes.url_shortener.domain.repositories.ShortUrlRepository;
import com.surfer.codes.url_shortener.dto.ShortUrlDto;
import com.surfer.codes.url_shortener.utils.EntityMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;

    public ShortUrlService(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    public List<ShortUrlDto> getAllPublicShortUrls() {
        List<ShortUrl> shortUrls = shortUrlRepository.getAllPublicShortUrls();
        return shortUrls.stream().map(EntityMapper::toShortUrlDto).toList();
    }
}