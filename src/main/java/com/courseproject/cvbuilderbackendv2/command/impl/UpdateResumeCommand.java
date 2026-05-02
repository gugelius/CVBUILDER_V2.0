package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.entity.Resume;
import com.courseproject.cvbuilderbackendv2.entity.Role;
import com.courseproject.cvbuilderbackendv2.entity.User;
import com.courseproject.cvbuilderbackendv2.service.ResumeService;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UpdateResumeCommand implements Command {
    private final ResumeService resumeService;
    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UpdateResumeCommand(ResumeService resumeService, UserService userService) {
        this.resumeService = resumeService;
        this.userService = userService;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        String userName = userService.extractUserName();
        if ("error".equals(userName)) {
            return Map.of("error", "Not authenticated");
        }

        User user = userService.findUserByUsername(userName);
        if (user.getRole() != Role.ROLE_SEEKER) {
            return Map.of("status", "error", "message", "Only seekers can update resumes");
        }

        int resumeId = Integer.parseInt(params.get("resumeId").toString());
        Resume resume = resumeService.findResumeByResumeId(resumeId);

        if (resume == null) {
            return Map.of("status", "error", "message", "Resume not found");
        }

        if (resume.getUser().getUserId() != user.getUserId()) {
            return Map.of("status", "error", "message", "You can only update your own resumes");
        }

        JsonNode resumeData = objectMapper.valueToTree(params.get("resumeData"));
        boolean isPublic = params.containsKey("isPublic") && Boolean.parseBoolean(params.get("isPublic").toString());
        boolean updated = resumeService.updateResume(resumeId, resumeData, isPublic);

        return Map.of("status", updated ? "success" : "error");
    }
}