package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.entity.Role;
import com.courseproject.cvbuilderbackendv2.entity.User;
import com.courseproject.cvbuilderbackendv2.entity.Vacancy;
import com.courseproject.cvbuilderbackendv2.repository.VacancyRepository;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SaveVacancyCommand implements Command {

    private final VacancyRepository vacancyRepository;
    private final UserService userService;

    public SaveVacancyCommand(VacancyRepository vacancyRepository, UserService userService) {
        this.vacancyRepository = vacancyRepository;
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

        Vacancy vacancy = new Vacancy(title, description, requirements, companyName, user);
        vacancyRepository.save(vacancy);

        return Map.of("status", "success", "vacancyId", vacancy.getId());
    }
}