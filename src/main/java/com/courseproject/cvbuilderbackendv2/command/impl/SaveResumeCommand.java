package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.Security.JwtUtil;
import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.service.ResumeService;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SaveResumeCommand implements Command {
    private final ResumeService resumeService;
    private final UserService userService;
    protected JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SaveResumeCommand(ResumeService resumeService, UserService userService, JwtUtil jwtUtil) {
        this.resumeService = resumeService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }
    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            return Map.of("error", "User is not authenticated");
        }

        String userName = authentication.getName();
        System.out.println("Authenticated user: " + userName);

        System.out.println(userName);
        JsonNode resumeData = objectMapper.valueToTree(params.get("resumeData"));
        resumeService.save(userName,resumeData);
        return Map.of("status", "success");
    }
}
