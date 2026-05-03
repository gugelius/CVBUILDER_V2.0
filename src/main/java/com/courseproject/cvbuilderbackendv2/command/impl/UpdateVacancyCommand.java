package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.entity.Role;
import com.courseproject.cvbuilderbackendv2.entity.User;
import com.courseproject.cvbuilderbackendv2.entity.Vacancy;
import com.courseproject.cvbuilderbackendv2.service.VacancyService;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UpdateVacancyCommand implements Command {

    private final VacancyService vacancyService;
    private final UserService userService;

    public UpdateVacancyCommand(VacancyService vacancyService, UserService userService) {
        this.vacancyService = vacancyService;
        this.userService = userService;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        String userName = userService.extractUserName();
        if ("error".equals(userName)) {
            return Map.of("error", "Not authenticated");
        }

        User user = userService.findUserByUsername(userName);
        if (user.getRole() != Role.ROLE_EMPLOYER) {
            return Map.of("status", "error", "message", "Only employers can update vacancies");
        }

        Long vacancyId = Long.parseLong(params.get("vacancyId").toString());
        Vacancy vacancy = vacancyService.findVacancyByVacancyId(vacancyId);

        if (vacancy == null) {
            return Map.of("status", "error", "message", "Vacancy not found");
        }

        if (vacancy.getEmployer().getUserId() != user.getUserId()) {
            return Map.of("status", "error", "message", "You can only update your own vacancies");
        }

        String title = params.containsKey("title") ? (String) params.get("title") : null;
        String description = params.containsKey("description") ? (String) params.get("description") : null;
        String requirements = params.containsKey("requirements") ? (String) params.get("requirements") : null;
        String companyName = params.containsKey("companyName") ? (String) params.get("companyName") : null;

        vacancyService.updateVacancy(vacancyId, title, description, requirements, companyName);
        return Map.of("status", "success");
    }
}