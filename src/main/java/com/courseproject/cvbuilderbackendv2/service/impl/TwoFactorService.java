package com.courseproject.cvbuilderbackendv2.service.impl;

import com.courseproject.cvbuilderbackendv2.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Random;

@Service
public class TwoFactorService {

    private static final int CODE_LENGTH = 6;
    private static final int CODE_EXPIRY_MINUTES = 5;

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.email.enabled:true}")
    private boolean emailEnabled;

    public TwoFactorService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    public void sendTwoFactorCode(String email, String code) {
        if (emailEnabled && email != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(fromEmail);
                message.setTo(email);
                message.setSubject("Код двухфакторной аутентификации");
                message.setText("Ваш код для входа: " + code +
                        "\nКод действителен в течение " + CODE_EXPIRY_MINUTES + " минут.");
                mailSender.send(message);
            } catch (Exception e) {
                System.err.println("Ошибка отправки email: " + e.getMessage());
            }
        }

        System.out.println("=== 2FA CODE FOR " + email + ": " + code + " ===");
    }

    public boolean validateCode(User user, String code) {
        if (user == null || code == null) return false;
        if (user.getTwoFactorCode() == null || user.getTwoFactorCodeExpiry() == null) return false;

        return user.getTwoFactorCode().equals(code) &&
                OffsetDateTime.now(ZoneOffset.UTC).isBefore(user.getTwoFactorCodeExpiry());
    }

    public OffsetDateTime calculateExpiryTime() {
        return OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(CODE_EXPIRY_MINUTES);
    }
}