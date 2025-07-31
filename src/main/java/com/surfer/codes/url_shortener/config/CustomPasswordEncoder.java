package com.surfer.codes.url_shortener.config;

import org.springframework.security.crypto.password.PasswordEncoder;


public class CustomPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return "";
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return false;
    }
}
