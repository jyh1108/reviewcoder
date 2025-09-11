package io.reviewcoder.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailCodeService {
    private final MailService mailService;
    private final StringRedisTemplate redis;
    private final Random random = new Random();

    // 키 규칙
    private String codeKey(String email) { return "email:code:" + email; }
    private String verifiedKey(String email) { return "email:verified:" + email; }
    private String cooldownKey(String email) { return "email:cooldown:" + email; }

    // 설정값
    private static final Duration CODE_TTL = Duration.ofMinutes(5);
    private static final Duration VERIFIED_TTL = Duration.ofMinutes(10);
    private static final Duration COOLDOWN_TTL = Duration.ofSeconds(30);

    public void send(String email) {
        if (Boolean.TRUE.equals(redis.hasKey(cooldownKey(email)))) {
            throw new IllegalStateException("TOO_FREQUENT_REQUEST");
        }
        String code = String.format("%06d", random.nextInt(1_000_000));

        // Redis 저장
        redis.opsForValue().set(codeKey(email), code, CODE_TTL);
        redis.opsForValue().set(cooldownKey(email), "1", COOLDOWN_TTL);

        mailService.sendCodeMail(email, code);
    }
    
    //코드검증
    public boolean verify(String email, String code) {
        String saved = redis.opsForValue().get(codeKey(email));
        if (saved == null || !saved.equals(code)) return false;
        
        redis.opsForValue().set(verifiedKey(email), "1", VERIFIED_TTL);
        redis.delete(codeKey(email));
        return true;
    }

    //인증완료한 이메일인지 확인
    public boolean isVerified(String email) {
        return Boolean.TRUE.equals(redis.hasKey(verifiedKey(email)));
    }
}