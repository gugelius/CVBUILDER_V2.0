package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.Security.JwtUtil;
import com.courseproject.cvbuilderbackendv2.command.Command;
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
    protected JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String ERROR = "error";

    public SaveResumeCommand(ResumeService resumeService, UserService userService, JwtUtil jwtUtil) {
        this.resumeService = resumeService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }
    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        String userName = userService.extractUserName();
        if (userName.equals(ERROR)){
            return Map.of(ERROR,ERROR);
        }
        JsonNode resumeData = objectMapper.valueToTree(params.get("resumeData"));
        resumeService.save(userName,resumeData);
        return Map.of("status", "success");
    }
}
