package io.reviewcoder.domain.user.controller;

import io.reviewcoder.domain.user.dto.*;
import io.reviewcoder.domain.user.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final EmailCodeService emailCodeService;
    private final PasswordEncoder passwordEncoder;

    // 이메일 인증 코드 발송
    @PostMapping("/email/send")
    public ResponseEntity<?> sendCode(@RequestParam String email) {
        emailCodeService.send(email); // TTL 5분
        return ResponseEntity.ok(Map.of("message", "code sent"));
    }

    // 이메일 인증 코드 검증
    @PostMapping("/email/verify")
    public ResponseEntity<?> verify(@RequestParam String email, @RequestParam String code) {
        boolean ok = emailCodeService.verify(email, code);
        if (!ok) return ResponseEntity.badRequest().body(Map.of("error", "INVALID_CODE"));
        return ResponseEntity.ok(Map.of("message", "verified"));
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest req) {
        try {
            userService.registerUser(req.email(), req.password());
            return ResponseEntity.ok(Map.of("message", "회원가입 성공"));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
