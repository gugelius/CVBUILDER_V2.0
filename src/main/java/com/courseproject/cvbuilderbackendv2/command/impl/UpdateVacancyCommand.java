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
public class UpdateVacancyCommand implements Command {

    private final VacancyRepository vacancyRepository;
    private final UserService userService;

    public UpdateVacancyCommand(VacancyRepository vacancyRepository, UserService userService) {
        this.vacancyRepository = vacancyRepository;
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
        Vacancy vacancy = vacancyRepository.findById(vacancyId).orElse(null);

        if (vacancy == null) {
            return Map.of("status", "error", "message", "Vacancy not found");
        }

        if (vacancy.getEmployer().getUserId() != user.getUserId()) {
            return Map.of("status", "error", "message", "You can only update your own vacancies");
        }

        if (params.containsKey("title")) vacancy.setTitle((String) params.get("title"));
        if (params.containsKey("description")) vacancy.setDescription((String) params.get("description"));
        if (params.containsKey("requirements")) vacancy.setRequirements((String) params.get("requirements"));
        if (params.containsKey("companyName")) vacancy.setCompanyName((String) params.get("companyName"));

        vacancyRepository.save(vacancy);
        return Map.of("status", "success");
    }
}