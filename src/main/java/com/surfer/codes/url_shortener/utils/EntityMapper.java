package com.surfer.codes.url_shortener.utils;

import com.surfer.codes.url_shortener.domain.entities.ShortUrl;
import com.surfer.codes.url_shortener.domain.entities.User;
import com.surfer.codes.url_shortener.dto.ShortUrlDto;
import com.surfer.codes.url_shortener.dto.UserDto;

public class EntityMapper {

    public static UserDto toUserDto(User user){
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static ShortUrlDto toShortUrlDto(ShortUrl shortUrl) {
        return new ShortUrlDto(
                shortUrl.getId(),
                shortUrl.getShortKey(),
                shortUrl.getOriginalUrl(),
                shortUrl.isPrivate(),
                shortUrl.getExpiresAt(),
                toUserDto(shortUrl.getCreatedBy()),
                shortUrl.getClickCount(),
                shortUrl.getCreatedAt()
        );
    }
}
