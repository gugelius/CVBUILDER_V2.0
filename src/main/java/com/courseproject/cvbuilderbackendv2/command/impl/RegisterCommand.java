package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.Security.JwtUtil;
import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RegisterCommand implements Command {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    public RegisterCommand(UserService userService, JwtUtil jwtUtil){
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }
    @Override
    public Map<String, Object> execute(Map<String, Object> params){
        String userName = params.get("username").toString();
        String userPassword = params.get("pass").toString();
        if(userService.register(userName, userPassword)){
            String token = jwtUtil.generateToken(userName);
            return Map.of("status", "success", "token", token);
        } else{
            return Map.of("status", "error","message", "Incorrect login or password");
        }
    }
}
