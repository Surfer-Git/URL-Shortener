package com.surfer.codes.url_shortener.utils;

import com.surfer.codes.url_shortener.domain.entities.User;
import com.surfer.codes.url_shortener.domain.repositories.UserRepository;
import com.surfer.codes.url_shortener.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserUtils {

    private final UserRepository userRepository;

    public Optional<User> getCurrentLoggedInUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext.getAuthentication() == null || !securityContext.getAuthentication().isAuthenticated()) {
            return Optional.empty();
        }

        String email = securityContext.getAuthentication().getName();
        return userRepository.findByEmail(email);
    }

    public Long getCurrentUserId() {
        return getCurrentLoggedInUser()
                .map(User::getId)
                .orElseThrow(() -> new IllegalStateException("No user is currently logged in"));
    }
}
