package com.surfer.codes.url_shortener.domain.repositories;

import com.surfer.codes.url_shortener.domain.entities.ShortUrl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    @Query("SELECT su FROM ShortUrl su LEFT JOIN FETCH su.createdBy WHERE su.isPrivate = false")
    Page<ShortUrl> getAllPublicShortUrls(Pageable pageable);

    @Query("SELECT su FROM ShortUrl su LEFT JOIN FETCH su.createdBy WHERE su.createdBy.id = :userId")
    Page<ShortUrl> getUrlsByUserId(Long userId, Pageable pageable);

    boolean existsByShortKey(String shortKey);

    Optional<ShortUrl> findByShortKey(String shortKey);
}
