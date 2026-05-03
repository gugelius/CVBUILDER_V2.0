package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.dto.ResumeDTO;
import com.courseproject.cvbuilderbackendv2.entity.Resume;
import com.courseproject.cvbuilderbackendv2.entity.Role;
import com.courseproject.cvbuilderbackendv2.entity.User;
import com.courseproject.cvbuilderbackendv2.repository.ResumeRepository;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class LoadPublicResumesCommand implements Command {

    private final ResumeRepository resumeRepository;
    private final UserService userService;

    public LoadPublicResumesCommand(ResumeRepository resumeRepository, UserService userService) {
        this.resumeRepository = resumeRepository;
        this.userService = userService;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        List<Resume> publicResumes = resumeRepository.findByIsPublicTrue();
        String userName = userService.extractUserName();
        if ("error".equals(userName)) {
            return Map.of("error", "Not authenticated");
        }

        User user = userService.findUserByUsername(userName);
        if (user.getRole() == Role.ROLE_SEEKER) {
            return Map.of("status", "error", "message", "Access denied");
        }

        List<ResumeDTO> resumeDTOs = publicResumes.stream()
                .map(r -> new ResumeDTO(
                        r.getResumeId(),
                        r.getUser().getUserId(),
                        r.getUser().getUserName(),
                        r.getResumeData(),
                        r.isPublic()))
                .toList();

        return Map.of("status", "success", "resumes", resumeDTOs);
    }
}