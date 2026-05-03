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
public class SaveVacancyCommand implements Command {

    private final VacancyService vacancyService;
    private final UserService userService;

    public SaveVacancyCommand(VacancyService vacancyService, UserService userService) {
        this.vacancyService = vacancyService;
        this.userService = userService;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        String userName = userService.extractUserName();

        if ("error".equals(userName)) {
            return Map.of("status", "error", "message", "Not authenticated");
        }

        User user = userService.findUserByUsername(userName);

        if (user.getRole() != Role.ROLE_EMPLOYER) {
            return Map.of("status", "error", "message", "Only employers can create vacancies");
        }

        String title = (String) params.get("title");
        String description = (String) params.get("description");
        String requirements = (String) params.get("requirements");
        String companyName = params.containsKey("companyName") ? params.get("companyName").toString() : "Unknown Company";

        Vacancy vacancy = vacancyService.saveVacancy(title, description, requirements, companyName, userName);
        return Map.of("status", "success", "vacancyId", vacancy.getId());
    }
}