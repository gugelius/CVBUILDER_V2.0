package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.Security.JwtUtil;
import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.service.ResumeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UpdateResumeCommand implements Command {
    private final ResumeService resumeService;
    protected JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UpdateResumeCommand(ResumeService resumeService, JwtUtil jwtUtil) {
        this.resumeService = resumeService;
        this.jwtUtil = jwtUtil;
    }
    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        int resumeId = Integer.valueOf((String)params.get("resumeId"));
        JsonNode resumeData = objectMapper.valueToTree(params.get("resumeData"));
        resumeService.updateResume(resumeId,resumeData);
        return Map.of("status", "success");
    }
}
