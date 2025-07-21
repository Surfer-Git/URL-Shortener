package com.surfer.codes.url_shortener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity.httpBasic(Customizer.withDefaults()) // flow will be: authManager -> authProvider -> UserDetailsService/PasswordEncoder
                // post authentication success, the user will be added to the SecurityContext
                // and the authorization filter will take over to check the user's authorities.
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(HttpMethod.GET, "/api/v1/**").hasAuthority("read")
                                .requestMatchers(HttpMethod.POST,"/api/v1/short-urls").hasAuthority("write")
                                .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable) // FIXME: only for demo, never disable CSRF in production!
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var uds =  new InMemoryUserDetailsManager(); // only for demo purpose.

        var user1 = User.withUsername("alice")
                .password("12345")
                .authorities("read")
                .build();

        var user2 = User.withUsername("bob")
                .password("12345")
                .authorities("read", "write")
                .build();

        uds.createUser(user1);
        uds.createUser(user2);

        return uds;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // only for demo purpose.
    }
}
