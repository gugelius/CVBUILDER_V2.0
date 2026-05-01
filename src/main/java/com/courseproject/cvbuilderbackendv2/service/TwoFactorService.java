package com.courseproject.cvbuilderbackendv2.service;

import com.courseproject.cvbuilderbackendv2.entity.User;

import java.time.OffsetDateTime;

public interface TwoFactorService {
    String generateCode();
    void sendTwoFactorCode(String email, String code);
    boolean validateCode(User user, String code);
    OffsetDateTime calculateExpiryTime();
}
