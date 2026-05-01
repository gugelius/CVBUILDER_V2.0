package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.Security.JwtUtil;
import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RegisterCommand implements Command {
    private final UserService userService;

    public RegisterCommand(UserService userService){
        this.userService = userService;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params){
        boolean registered;

        String userName = params.get("username").toString();
        String userPassword = params.get("pass").toString();
        String userEmail = params.containsKey("email") ? params.get("email").toString() : null;

        if (userEmail != null && !userEmail.isEmpty()) {
            registered = userService.register(userName, userPassword, userEmail);
        } else {
            registered = userService.register(userName, userPassword);
        }

        if(registered){
            String token = JwtUtil.generateToken(userName);
            return Map.of("status", "success", "token", token);
        } else{
            return Map.of("status", "error","message", "User already exists or registration failed");
        }
    }
}
