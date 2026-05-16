package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.dto.ApplicationDTO;
import com.courseproject.cvbuilderbackendv2.entity.*;
import com.courseproject.cvbuilderbackendv2.service.ApplicationService;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class GetMyApplicationsCommand implements Command {

    private final ApplicationService applicationService;
    private final UserService userService;

    public GetMyApplicationsCommand(ApplicationService applicationService, UserService userService) {
        this.applicationService = applicationService;
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
            return Map.of("status", "error", "message", "Only seekers can view applications");
        }

        List<Application> applications = applicationService.getMyApplications(userName);

        List<ApplicationDTO> dtos = applications.stream()
                .map(a -> new ApplicationDTO(
                        a.getId(),
                        a.getVacancy().getId(),
                        a.getVacancy().getTitle(),
                        a.getResume().getResumeId(),
                        a.getApplicant().getUserId(),
                        a.getApplicant().getUserName(),
                        a.getCoverLetter(),
                        a.getStatus(),
                        a.getCreatedAt()
                ))
                .toList();

        return Map.of("status", "success", "applications", dtos);
    }
}