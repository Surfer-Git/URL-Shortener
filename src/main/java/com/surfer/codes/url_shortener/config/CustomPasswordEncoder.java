package com.surfer.codes.url_shortener.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.util.Base64;

@Component
public class CustomPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        // Not used for per-user salt, return unsupported
        throw new UnsupportedOperationException("Use encodeWithSalt for per-user salt.");
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // Not used for per-user salt, return unsupported
        throw new UnsupportedOperationException("Use matchesWithSalt for per-user salt.");
    }

    public String encodeWithSalt(CharSequence rawPassword, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashed = md.digest(rawPassword.toString().getBytes());
            return Base64.getEncoder().encodeToString(hashed);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean matchesWithSalt(CharSequence rawPassword, String encodedPassword, String salt) {
        String hashed = encodeWithSalt(rawPassword, salt);
        return hashed.equals(encodedPassword);
    }
}
