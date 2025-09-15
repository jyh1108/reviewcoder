package io.reviewcoder.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendCodeMail(String to, String code) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("[reviewcoder] 이메일 인증 코드");
        msg.setText("""
                안녕하세요.
                아래 인증 코드를 입력해주세요.

                인증 코드: %s

                (유효시간: 5분)
                """.formatted(code));
        mailSender.send(msg);
    }
}