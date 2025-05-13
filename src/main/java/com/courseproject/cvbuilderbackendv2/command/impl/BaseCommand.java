package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.Security.JwtUtil;
import com.courseproject.cvbuilderbackendv2.command.Command;
import org.springframework.ui.Model;

import java.util.Map;

public abstract class BaseCommand implements Command {
    protected JwtUtil jwtUtil;

    public BaseCommand(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    protected boolean isTokenValid(Map<String, String> params, Model model) {
        String token = params.get("token");
        System.out.println(token);
        if (token == null || !jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
            model.addAttribute("err", "Неверный токен. Доступ запрещён.");
            return false;
        }
        return true;
    }
}
