package com.surfer.codes.url_shortener.dto;

public record CreateShortUrlCmd(
        String originalUrl,
        Boolean isPrivate,
        Integer expirationDays
) {}
