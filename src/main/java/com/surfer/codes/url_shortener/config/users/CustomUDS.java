package com.surfer.codes.url_shortener.config.users;

import com.surfer.codes.url_shortener.domain.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUDS implements UserDetailsService{

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(this::toCustomUserDetails)
                .orElseThrow( () -> new UsernameNotFoundException("User not found with email: " + username));
    }

    private CustomUserDetails toCustomUserDetails(com.surfer.codes.url_shortener.domain.entities.User user) {
        return CustomUserDetails.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .salt(user.getSalt())
                .roles(user.getRole())
                .build();
    }




}
