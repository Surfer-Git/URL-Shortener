package com.surfer.codes.url_shortener.security.filters;

import com.surfer.codes.url_shortener.security.authentication.CustomAuthentication;
import com.surfer.codes.url_shortener.security.managers.CustomAuthenticationManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class MyCustomFilter extends OncePerRequestFilter {

    private final CustomAuthenticationManager customAuthenticationManager;
    private final Logger log = LoggerFactory.getLogger(MyCustomFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. create an authentication object which is not yet authenticated
        // 2. delegate the authentication object to the manager
        // 3. get back the authentication from the manager
        // 4. if the object is authenticated then send request to the next filter in the chain
        String key = request.getHeader("key");
        CustomAuthentication authentication = new CustomAuthentication(false, key);
        try{
            var authenticated = customAuthenticationManager.authenticate(authentication);
            if (authenticated.isAuthenticated()) {
                // V-IMP: If authenticated, set the authentication in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authenticated);
                // Continue the filter chain
                filterChain.doFilter(request, response);
            }
        } catch (AuthenticationException e) {
            log.error("Authentication failed: {}", e.getMessage());
            // If authentication fails, set the response status to 401 Unauthorized
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: " + e.getMessage());
            return; // Stop further processing
        } catch (Exception e) {
            log.error("Unexpected error during authentication: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal Server Error: " + e.getMessage());
            return; // Stop further processing
        }
    }
}
