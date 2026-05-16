package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.dto.ApplicationDTO;
import com.courseproject.cvbuilderbackendv2.entity.*;
import com.courseproject.cvbuilderbackendv2.service.ApplicationService;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class ApplyForVacancyCommand implements Command {

    private final ApplicationService applicationService;
    private final UserService userService;

    public ApplyForVacancyCommand(ApplicationService applicationService, UserService userService) {
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
            return Map.of("status", "error", "message", "Only seekers can apply");
        }

        Long vacancyId = Long.parseLong(params.get("vacancyId").toString());
        int resumeId = Integer.parseInt(params.get("resumeId").toString());
        String coverLetter = params.containsKey("coverLetter") ? params.get("coverLetter").toString() : null;

        try {
            Application application = applicationService.apply(vacancyId, resumeId, userName, coverLetter);

            ApplicationDTO dto = new ApplicationDTO(
                    application.getId(),
                    application.getVacancy().getId(),
                    application.getVacancy().getTitle(),
                    application.getResume().getResumeId(),
                    application.getApplicant().getUserId(),
                    application.getApplicant().getUserName(),
                    application.getCoverLetter(),
                    application.getStatus(),
                    application.getCreatedAt()
            );

            return Map.of("status", "success", "application", dto);
        } catch (RuntimeException e) {
            return Map.of("status", "error", "message", e.getMessage());
        }
    }
}