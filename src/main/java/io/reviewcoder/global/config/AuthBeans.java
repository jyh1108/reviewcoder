package io.reviewcoder.global.config;

import io.reviewcoder.domain.user.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

@Configuration
public class AuthBeans {

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return (String raw) -> {
            String email = raw.trim().toLowerCase();
            var u = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
            return org.springframework.security.core.userdetails.User.builder()
                    .username(u.getEmail())
                    .password(u.getPasswordHash())
                    .authorities(Collections.emptyList())
                    .accountLocked(false)
                    .disabled(false)
                    .build();
        };
    }
}