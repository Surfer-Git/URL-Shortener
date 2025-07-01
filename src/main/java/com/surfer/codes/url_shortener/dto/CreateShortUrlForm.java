package com.surfer.codes.url_shortener.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateShortUrlForm(
        @NotBlank(message = "Original url is required")
        String originalUrl
) {}
