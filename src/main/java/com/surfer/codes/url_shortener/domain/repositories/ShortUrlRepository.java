package com.surfer.codes.url_shortener.domain.repositories;

import com.surfer.codes.url_shortener.domain.entities.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    // Custom queries can be added here if needed
}
