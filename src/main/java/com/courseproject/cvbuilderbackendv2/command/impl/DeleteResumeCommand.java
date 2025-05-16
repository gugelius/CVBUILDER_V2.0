package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.Security.JwtUtil;
import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.service.ResumeService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DeleteResumeCommand implements Command {
    private final ResumeService resumeService;
    protected JwtUtil jwtUtil;

    public DeleteResumeCommand(ResumeService resumeService, JwtUtil jwtUtil) {
        this.resumeService = resumeService;
        this.jwtUtil = jwtUtil;
    }
    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        int resumeId = (int) params.get("resumeId");
        resumeService.deleteResumeByResumeId(resumeId);
        return Map.of("status", "success");
    }
}
