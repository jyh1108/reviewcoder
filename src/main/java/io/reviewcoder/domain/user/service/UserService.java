package io.reviewcoder.domain.user.service;

import io.reviewcoder.domain.user.model.User;
import io.reviewcoder.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailCodeService emailCodeService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        String norm = normalizeEmail(email);
        return userRepository.existsByEmail(norm);
    }

    @Transactional
    public Long registerUser(String email, String rawPassword) {
        String norm = normalizeEmail(email);
        // 1) 이메일 인증 여부 확인
        if (!emailCodeService.isVerified(norm)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일이 인증되지 않았습니다. 인증 후 다시 시도해주세요.");
        }
        // 2) 사전 중복 체크
        if (userRepository.existsByEmail(norm)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다.");
        }
        //저장
        try {
            User user = new User();
            user.setEmail(norm);
            user.setPasswordHash(passwordEncoder.encode(rawPassword));
            user.setCreatedAt(LocalDateTime.now());
            user.setEmailVerifiedAt(LocalDateTime.now());
            return userRepository.save(user).getId();
        } catch (DataIntegrityViolationException e) {
            // 동시성으로 UNIQUE(email) 충돌 시
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다.");
        }
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일이 올바르지 않습니다.");
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
