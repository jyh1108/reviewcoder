package io.reviewcoder.domain.user.service;

import io.reviewcoder.domain.user.model.User;
import io.reviewcoder.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public Long createUser(String email, String encodedPassword) {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(encodedPassword);
        user = userRepository.save(user);
        return user.getId();
    }
}