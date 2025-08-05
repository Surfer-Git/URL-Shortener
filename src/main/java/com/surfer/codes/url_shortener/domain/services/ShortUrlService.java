package com.surfer.codes.url_shortener.domain.services;

import com.surfer.codes.url_shortener.ApplicationProperties;
import com.surfer.codes.url_shortener.domain.entities.ShortUrl;
import com.surfer.codes.url_shortener.domain.entities.User;
import com.surfer.codes.url_shortener.domain.repositories.ShortUrlRepository;
import com.surfer.codes.url_shortener.dto.CreateShortUrlCmd;
import com.surfer.codes.url_shortener.dto.PagedResult;
import com.surfer.codes.url_shortener.dto.ShortUrlDto;
import com.surfer.codes.url_shortener.utils.EntityMapper;
import com.surfer.codes.url_shortener.utils.UrlUtils;
import com.surfer.codes.url_shortener.utils.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final ApplicationProperties appConf;
    private final UserUtils userUtils;

    public PagedResult<ShortUrlDto> getAllPublicShortUrls(int pageNo, int pageSize) {
        Pageable pageable = getPageable(pageNo, pageSize);
        Page<ShortUrl> shortUrls = shortUrlRepository.getAllPublicShortUrls(pageable);
        return PagedResult.from(shortUrls.map(EntityMapper::toShortUrlDto));
    }

    private Pageable getPageable(int page, int size) {
        page = page > 1 ? (page - 1) : 0;
        return PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
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

        Optional<User> curUser = userUtils.getCurrentLoggedInUser();

        String shortKey = generateUniqueShortKey();
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(cmd.originalUrl());
        shortUrl.setShortKey(shortKey);
        shortUrl.setCreatedBy(curUser.orElse(null));
        shortUrl.setPrivate(cmd.isPrivate() != null && cmd.isPrivate());
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

        Optional<User> curUser = userUtils.getCurrentLoggedInUser();

        // if it's public or created by the current user, allow access
        if(!shortUrlOpt.get().isPrivate() || isCreatedByCurrentUser(shortUrlOpt.get()) ){

            ShortUrl shortUrl = shortUrlOpt.get();
            shortUrl.setClickCount(shortUrl.getClickCount() + 1);
            shortUrlRepository.save(shortUrl); // Update click count

            return shortUrlOpt.map(EntityMapper::toShortUrlDto);
        }
        else {
            return Optional.empty();
        }
    }

    private boolean isCreatedByCurrentUser(ShortUrl shortUrl) {
        Optional<User> curUser = userUtils.getCurrentLoggedInUser();
        return curUser.isPresent() && Objects.equals(shortUrl.getCreatedBy().getId(), curUser.get().getId());
    }

    private boolean isShortUrlExpired(ShortUrl shortUrl) {
        return shortUrl.getExpiresAt() != null && shortUrl.getExpiresAt().isBefore(LocalDateTime.now());
    }
}