package com.surfer.codes.url_shortener.config;

import com.surfer.codes.url_shortener.config.users.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final CustomPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (!(authentication instanceof UsernamePasswordAuthenticationToken authToken)) {
            return null;
        }
        final String receivedUserName = authToken.getName(); // email id
        final String receivedPassword = authToken.getCredentials().toString();

        // authentication logic
        CustomUserDetails userDetails =  (CustomUserDetails) userDetailsService.loadUserByUsername(receivedUserName); // stored credentials

        boolean isPasswordValid = passwordEncoder.matchesWithSalt(
                receivedPassword,
                userDetails.getPassword(),
                userDetails.getSalt()
        );

        if(isPasswordValid) {
            // authentication successful
            return new UsernamePasswordAuthenticationToken(
                    userDetails,
                    receivedPassword,
                    userDetails.getAuthorities()
            );
        } else {
            // authentication failed
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
