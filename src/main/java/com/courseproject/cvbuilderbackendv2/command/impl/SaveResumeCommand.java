package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.Security.JwtUtil;
import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.entity.Role;
import com.courseproject.cvbuilderbackendv2.entity.User;
import com.courseproject.cvbuilderbackendv2.service.ResumeService;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SaveResumeCommand implements Command {
    private final ResumeService resumeService;
    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String ERROR = "error";

    public SaveResumeCommand(ResumeService resumeService, UserService userService, JwtUtil jwtUtil) {
        this.resumeService = resumeService;
        this.userService = userService;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        String userName = userService.extractUserName();
        if (userName.equals(ERROR)) {
            return Map.of(ERROR, ERROR);
        }

        User user = userService.findUserByUsername(userName);
        if (user.getRole() != Role.ROLE_SEEKER) {
            return Map.of("status", "error", "message", "Only seekers can create resumes");
        }

        JsonNode resumeData = objectMapper.valueToTree(params.get("resumeData"));
        boolean isPublic = params.containsKey("isPublic") && Boolean.parseBoolean(params.get("isPublic").toString());
        resumeService.save(userName, resumeData, isPublic);
        return Map.of("status", "success");
    }
}