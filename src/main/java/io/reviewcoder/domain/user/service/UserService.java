package io.reviewcoder.domain.user.service;

import io.reviewcoder.domain.user.model.User;
import io.reviewcoder.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailCodeService emailCodeService;
    private final PasswordEncoder passwordEncoder;

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    @Transactional
    public Long registerUser(String email, String rawPassword) {
        if (!emailCodeService.isVerified(email)) {
            throw new IllegalStateException("이메일이 인증되지 않았습니다. 인증 후 다시 시도해주세요.");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setEmailVerifiedAt(LocalDateTime.now());
        return userRepository.save(user).getId();
    }
}