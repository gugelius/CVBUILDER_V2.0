package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.Security.JwtUtil;
import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.entity.Resume;
import com.courseproject.cvbuilderbackendv2.service.ResumeService;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoadResumeCommand implements Command {
    private final ResumeService resumeService;
    private final UserService userService;
    protected JwtUtil jwtUtil;
    public LoadResumeCommand(ResumeService resumeService, UserService userService, JwtUtil jwtUtil) {
        this.resumeService = resumeService;
        this.userService = userService;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) throws IOException {
        int resumeId = Integer.valueOf((String)params.get("resumeId"));
        Resume resume = resumeService.findResumeByResumeId(resumeId);
//        return Map.of("resume",resumeService.findResumeByResumeId(resumeId));
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> resultMap = objectMapper.convertValue(resume.getResumeData(), Map.class);

        // Добавляем ID, если он нужен
        resultMap.put("resumeId", resume.getResumeId());

        return resultMap;
    }
}
