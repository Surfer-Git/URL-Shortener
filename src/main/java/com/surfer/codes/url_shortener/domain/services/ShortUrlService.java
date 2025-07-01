package com.surfer.codes.url_shortener.domain.services;

import com.surfer.codes.url_shortener.ApplicationProperties;
import com.surfer.codes.url_shortener.domain.entities.ShortUrl;
import com.surfer.codes.url_shortener.domain.repositories.ShortUrlRepository;
import com.surfer.codes.url_shortener.dto.CreateShortUrlCmd;
import com.surfer.codes.url_shortener.dto.ShortUrlDto;
import com.surfer.codes.url_shortener.utils.EntityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final ApplicationProperties appConf;

    public ShortUrlService(ShortUrlRepository shortUrlRepository, ApplicationProperties appConf) {
        this.shortUrlRepository = shortUrlRepository;
        this.appConf = appConf;
    }

    public List<ShortUrlDto> getAllPublicShortUrls() {
        List<ShortUrl> shortUrls = shortUrlRepository.getAllPublicShortUrls();
        return shortUrls.stream().map(EntityMapper::toShortUrlDto).toList();
    }

    @Transactional
    public ShortUrlDto createShortUrl(CreateShortUrlCmd cmd) {
        // TODO: validate the original URL, check for duplicates, creating a unique short key

        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(cmd.originalUrl());
        shortUrl.setShortKey("testKey");
        shortUrl.setCreatedBy(null);
        shortUrl.setPrivate(false);
        shortUrl.setClickCount(0L);
        shortUrl.setExpiresAt(LocalDateTime.now().plusDays(appConf.defaultExpiryInDays()));
        shortUrl.setCreatedAt(LocalDateTime.now());
        ShortUrl savedShortUrl = shortUrlRepository.save(shortUrl);
        return EntityMapper.toShortUrlDto(savedShortUrl);
    }
}