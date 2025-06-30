package com.surfer.codes.url_shortener.dto;

import java.time.LocalDateTime;

public record ShortUrlDto(
        Long id,
        String shortKey,
        String originalUrl,
        boolean isPrivate,
        LocalDateTime expiresAt,
        UserDto createdBy,
        Long clickCount,
        LocalDateTime createdAt
) {}
