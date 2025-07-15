package com.surfer.codes.url_shortener.domain.services;

import com.surfer.codes.url_shortener.domain.entities.User;
import com.surfer.codes.url_shortener.domain.repositories.UserRepository;
import com.surfer.codes.url_shortener.security.SecurityAuthority;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<UserDetails> getUserDetails(String username) {
        Optional<User> user = userRepository.findByName(username);
        return user.map(this::mapToUserDetails);
    }

    private UserDetails mapToUserDetails(User user) {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return user.getAuthorities().stream()
                        .map(SecurityAuthority::new)
                        .collect(Collectors.toList());
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getName();
            }
        };
    }
}
