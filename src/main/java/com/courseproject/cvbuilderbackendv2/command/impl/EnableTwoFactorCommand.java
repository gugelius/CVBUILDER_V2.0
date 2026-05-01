package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.entity.User;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EnableTwoFactorCommand implements Command {

    private final UserService userService;

    public EnableTwoFactorCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        String userName = userService.extractUserName();

        if ("error".equals(userName)) {
            return Map.of("status", "error", "message", "Not authenticated");
        }

        User user = userService.findUserByUsername(userName);

        if (user == null) {
            return Map.of("status", "error", "message", "User not found");
        }

        if (user.getUserEmail() == null || user.getUserEmail().isEmpty()) {
            return Map.of("status", "error", "message", "Email is required for 2FA");
        }

        user.setTwoFactorEnabled(true);
        userService.saveUser(user);

        return Map.of("status", "success", "message", "Two-factor authentication enabled");
    }
}