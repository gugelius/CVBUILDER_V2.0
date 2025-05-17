package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.Security.JwtUtil;
import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LoginCommand implements Command {
    private final UserService userService;

    public LoginCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        String userName = params.get("username").toString();
        String userPassword = params.get("pass").toString();

        if (userService.authenticate(userName, userPassword)) {
            String token = JwtUtil.generateToken(userName);
            return Map.of("status", "success", "token", token);
        } else {
            return Map.of("status", "error", "message", "Incorrect login or password");
        }
    }
}
