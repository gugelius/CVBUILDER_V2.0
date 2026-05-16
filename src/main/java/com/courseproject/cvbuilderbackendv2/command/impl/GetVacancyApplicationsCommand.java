package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.dto.ApplicationDTO;
import com.courseproject.cvbuilderbackendv2.entity.*;
import com.courseproject.cvbuilderbackendv2.service.ApplicationService;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import com.courseproject.cvbuilderbackendv2.service.VacancyService;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class GetVacancyApplicationsCommand implements Command {

    private final ApplicationService applicationService;
    private final UserService userService;
    private final VacancyService vacancyService;

    public GetVacancyApplicationsCommand(ApplicationService applicationService,
                                         UserService userService,
                                         VacancyService vacancyService) {
        this.applicationService = applicationService;
        this.userService = userService;
        this.vacancyService = vacancyService;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        String userName = userService.extractUserName();
        if ("error".equals(userName)) {
            return Map.of("error", "Not authenticated");
        }

        User user = userService.findUserByUsername(userName);
        if (user.getRole() != Role.ROLE_EMPLOYER) {
            return Map.of("status", "error", "message", "Only employers can view applications");
        }

        Long vacancyId = Long.parseLong(params.get("vacancyId").toString());
        Vacancy vacancy = vacancyService.findVacancyByVacancyId(vacancyId);

        if (vacancy == null || vacancy.getEmployer().getUserId() != user.getUserId()) {
            return Map.of("status", "error", "message", "Access denied");
        }

        List<Application> applications = applicationService.getVacancyApplications(vacancyId);

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