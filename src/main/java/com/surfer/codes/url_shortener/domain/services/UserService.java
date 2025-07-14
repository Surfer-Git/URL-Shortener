package com.surfer.codes.url_shortener.domain.services;

import com.surfer.codes.url_shortener.domain.entities.User;
import com.surfer.codes.url_shortener.domain.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
                return List.of((GrantedAuthority) () -> "read");
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
