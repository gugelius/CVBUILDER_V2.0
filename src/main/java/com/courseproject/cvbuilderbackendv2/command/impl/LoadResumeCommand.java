package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.dto.ResumeDTO;
import com.courseproject.cvbuilderbackendv2.entity.Resume;
import com.courseproject.cvbuilderbackendv2.entity.Role;
import com.courseproject.cvbuilderbackendv2.entity.User;
import com.courseproject.cvbuilderbackendv2.service.ResumeService;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LoadResumeCommand implements Command {
    private final ResumeService resumeService;
    private final UserService userService;

    public LoadResumeCommand(ResumeService resumeService, UserService userService) {
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
        int resumeId = Integer.parseInt(params.get("resumeId").toString());
        Resume resume = resumeService.findResumeByResumeId(resumeId);

        if (resume == null) {
            return Map.of("status", "error", "message", "Resume not found");
        }

        if (user.getRole() == Role.ROLE_SEEKER) {
            if (resume.getUser().getUserId() != user.getUserId()) {
                return Map.of("status", "error", "message", "Access denied");
            }
        }

        if (user.getRole() == Role.ROLE_EMPLOYER) {
            if (!resume.isPublic()) {
                return Map.of("status", "error", "message", "This resume is not public");
            }
        }

        ResumeDTO resumeDTO = new ResumeDTO(
                resume.getResumeId(),
                resume.getUser().getUserId(),
                resume.getUser().getUserName(),
                resume.getResumeData(),
                resume.isPublic()
        );

        return Map.of("status", "success", "resume", resumeDTO);
    }
}