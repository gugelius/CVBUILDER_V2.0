package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.Security.JwtUtil;
import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.entity.Role;
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
        String userName = params.get("username").toString();
        String userPassword = params.get("pass").toString();
        String userEmail = params.containsKey("email") ? params.get("email").toString() : null;
        String roleStr = params.containsKey("role") ? params.get("role").toString() : "SEEKER";

        Role role;
        try {
            role = Role.valueOf("ROLE_" + roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Map.of("status", "error", "message", "Invalid role: " + roleStr);
        }

        boolean registered = userService.register(userName, userPassword, userEmail, role);

        if(registered){
            String token = JwtUtil.generateToken(userName);
            return Map.of("status", "success", "token", token, "role", role.name());
        } else{
            return Map.of("status", "error","message", "User already exists or registration failed");
        }
    }
}