package com.surfer.codes.url_shortener.domain.services;

import com.surfer.codes.url_shortener.ApplicationProperties;
import com.surfer.codes.url_shortener.domain.entities.ShortUrl;
import com.surfer.codes.url_shortener.domain.repositories.ShortUrlRepository;
import com.surfer.codes.url_shortener.dto.CreateShortUrlCmd;
import com.surfer.codes.url_shortener.dto.ShortUrlDto;
import com.surfer.codes.url_shortener.utils.EntityMapper;
import com.surfer.codes.url_shortener.utils.UrlUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        // TODO: check for duplicates
        if(appConf.validateOriginalUrl()) {
            boolean urlExists = UrlUtils.doesUrlExists(cmd.originalUrl());
            if(!urlExists) {
                throw new RuntimeException("Invalid URL " + cmd.originalUrl());
            }
        }

        String shortKey = generateUniqueShortKey();
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(cmd.originalUrl());
        shortUrl.setShortKey(shortKey);
        shortUrl.setCreatedBy(null);
        shortUrl.setPrivate(false);
        shortUrl.setClickCount(0L);
        shortUrl.setExpiresAt(LocalDateTime.now().plusDays(appConf.defaultExpiryInDays()));
        shortUrl.setCreatedAt(LocalDateTime.now());
        ShortUrl savedShortUrl = shortUrlRepository.save(shortUrl);
        return EntityMapper.toShortUrlDto(savedShortUrl);
    }

    private String generateUniqueShortKey() {
        String shortKey;
        do {
            shortKey = UrlUtils.generateRandomShortKey();
        } while (shortUrlRepository.existsByShortKey(shortKey));
        return shortKey;
    }

    @Transactional
    public Optional<ShortUrlDto> accessShortUrl(String shortKey) {
        Optional<ShortUrl> shortUrlOpt = shortUrlRepository.findByShortKey(shortKey);
        if(shortUrlOpt.isEmpty() || isShortUrlExpired(shortUrlOpt.get())) {
            return Optional.empty();
        }
        ShortUrl shortUrl = shortUrlOpt.get();
        shortUrl.setClickCount(shortUrl.getClickCount() + 1);
        shortUrlRepository.save(shortUrl); // Update click count

        return shortUrlOpt.map(EntityMapper::toShortUrlDto);
    }

    private boolean isShortUrlExpired(ShortUrl shortUrl) {
        return shortUrl.getExpiresAt() != null && shortUrl.getExpiresAt().isBefore(LocalDateTime.now());
    }
}