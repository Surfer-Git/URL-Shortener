package com.surfer.codes.url_shortener.domain.repositories;

import com.surfer.codes.url_shortener.domain.entities.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    @Query("SELECT s FROM ShortUrl s WHERE s.isPrivate = false order by s.createdAt desc")
    List<ShortUrl> getAllPublicShortUrls();
}
