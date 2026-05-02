package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.Security.JwtUtil;
import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.entity.User;
import com.courseproject.cvbuilderbackendv2.service.TwoFactorService;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LoginCommand implements Command {
    private final UserService userService;
    private final TwoFactorService twoFactorService;

    public LoginCommand(UserService userService, TwoFactorService twoFactorService) {
        this.userService = userService;
        this.twoFactorService = twoFactorService;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        String userName = params.get("username").toString();
        String userPassword = params.get("pass").toString();
        String twoFactorCode = params.containsKey("twoFactorCode") ?
                params.get("twoFactorCode").toString() : null;

        if (!userService.authenticate(userName, userPassword)) {
            return Map.of("status", "error", "message", "Incorrect login or password");
        }

        User user = userService.findUserByUsername(userName);

        if (user.isTwoFactorEnabled()) {
            if (twoFactorCode == null || twoFactorCode.isEmpty()) {
                String code = twoFactorService.generateCode();
                user.setTwoFactorCode(code);
                user.setTwoFactorCodeExpiry(twoFactorService.calculateExpiryTime());
                userService.saveUser(user);

                twoFactorService.sendTwoFactorCode(user.getUserEmail(), code);

                return Map.of(
                        "status", "2fa_required",
                        "message", "Two-factor code sent to your email"
                );
            }

            if (!twoFactorService.validateCode(user, twoFactorCode)) {
                return Map.of("status", "error", "message", "Invalid or expired two-factor code");
            }

            user.setTwoFactorCode(null);
            user.setTwoFactorCodeExpiry(null);
            userService.saveUser(user);
        }

        String token = JwtUtil.generateToken(userName);

        return Map.of(
                "status", "success",
                "token", token,
                "role", user.getRole().name(),
                "username", user.getUserName()
        );
    }
}