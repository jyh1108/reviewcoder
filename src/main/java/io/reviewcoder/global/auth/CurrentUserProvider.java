package io.reviewcoder.global.auth;

import io.reviewcoder.domain.user.model.User;
import io.reviewcoder.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class CurrentUserProvider {

    private final UserRepository userRepository;

    public Long id(Principal principal) {
        if (principal == null) throw new IllegalStateException("Unauthenticated");
        String email = principal.getName().trim().toLowerCase(); // formLogin -> username == email
        return userRepository.findByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new IllegalStateException("User not found: " + email));
    }
}