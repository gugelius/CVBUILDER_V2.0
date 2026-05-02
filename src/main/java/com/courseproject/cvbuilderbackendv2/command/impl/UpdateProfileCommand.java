package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.Security.JwtUtil;
import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.entity.User;
import com.courseproject.cvbuilderbackendv2.service.TwoFactorService;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UpdateProfileCommand implements Command {

    private final UserService userService;
    private final TwoFactorService twoFactorService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UpdateProfileCommand(UserService userService,
                                TwoFactorService twoFactorService,
                                BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.twoFactorService = twoFactorService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        String userName = userService.extractUserName();
        if ("error".equals(userName)) {
            return Map.of("error", "Not authenticated");
        }

        User user = userService.findUserByUsername(userName);
        if (user == null) {
            return Map.of("status", "error", "message", "User not found");
        }

        String currentPassword = params.containsKey("currentPassword") ?
                params.get("currentPassword").toString() : null;

        if (currentPassword == null || !passwordEncoder.matches(currentPassword, user.getUserPassword())) {
            return Map.of("status", "error", "message", "Current password is required and must be correct");
        }

        if (user.isTwoFactorEnabled()) {
            String twoFactorCode = params.containsKey("twoFactorCode") ?
                    params.get("twoFactorCode").toString() : null;

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
        }

        if (params.containsKey("username")) {
            String newUsername = params.get("username").toString();
            User existingUser = userService.findUserByUsername(newUsername);
            if (existingUser != null && existingUser.getUserId() != user.getUserId()) {
                return Map.of("status", "error", "message", "Username already taken");
            }
            user.setUserName(newUsername);
            userName = newUsername;
        }

        if (params.containsKey("email")) {
            String newEmail = params.get("email").toString();
            User existingUser = userService.findByEmail(newEmail);
            if (existingUser != null && existingUser.getUserId() != user.getUserId()) {
                return Map.of("status", "error", "message", "Email already in use");
            }
            user.setUserEmail(newEmail);
        }

        if (params.containsKey("newPassword")) {
            String newPassword = params.get("newPassword").toString();
            user.setUserPassword(passwordEncoder.encode(newPassword));
        }

        userService.saveUser(user);

        String token = JwtUtil.generateToken(userName);

        return Map.of(
                "status", "success",
                "message", "Profile updated",
                "token", token,
                "user", Map.of(
                        "username", user.getUserName(),
                        "email", user.getUserEmail() != null ? user.getUserEmail() : "",
                        "role", user.getRole().name(),
                        "twoFactorEnabled", user.isTwoFactorEnabled()
                )
        );
    }
}