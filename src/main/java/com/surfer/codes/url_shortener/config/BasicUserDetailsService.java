package com.surfer.codes.url_shortener.config;

import com.surfer.codes.url_shortener.domain.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BasicUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDetails> userDetails = userService.getUserDetails(username);
        return userDetails.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }


}
