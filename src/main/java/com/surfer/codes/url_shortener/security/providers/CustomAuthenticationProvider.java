package com.surfer.codes.url_shortener.security.providers;

import com.surfer.codes.url_shortener.ApplicationProperties;
import com.surfer.codes.url_shortener.security.authentication.CustomAuthentication;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final ApplicationProperties appConf;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomAuthentication ca = (CustomAuthentication) authentication;
        String headKey = ca.getKey();

        if(appConf.securityKey().equals(headKey)){
            return new CustomAuthentication(true, null);
        }

        throw new BadCredentialsException("invalid key");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthentication.class.equals(authentication);
    }
}
