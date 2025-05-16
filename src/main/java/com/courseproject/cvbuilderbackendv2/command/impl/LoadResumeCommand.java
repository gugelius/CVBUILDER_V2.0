package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.entity.Resume;
import com.courseproject.cvbuilderbackendv2.service.ResumeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Map;

@Component
public class LoadResumeCommand implements Command {
    private final ResumeService resumeService;

    public LoadResumeCommand(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) throws IOException {
        int resumeId = Integer.valueOf((String)params.get("resumeId"));
        Resume resume = resumeService.findResumeByResumeId(resumeId);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> resultMap = objectMapper.convertValue(resume.getResumeData(), Map.class);

        resultMap.put("resumeId", resume.getResumeId());
        System.out.println(resultMap);
        return resultMap;
    }
}