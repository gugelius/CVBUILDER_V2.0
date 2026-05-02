package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.entity.Role;
import com.courseproject.cvbuilderbackendv2.entity.User;
import com.courseproject.cvbuilderbackendv2.entity.Vacancy;
import com.courseproject.cvbuilderbackendv2.repository.VacancyRepository;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class LoadMyVacanciesCommand implements Command {

    private final VacancyRepository vacancyRepository;
    private final UserService userService;

    public LoadMyVacanciesCommand(VacancyRepository vacancyRepository, UserService userService) {
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
            return Map.of("status", "error", "message", "Only employers can view their vacancies");
        }

        List<Vacancy> vacancies = vacancyRepository.findByEmployer_UserId(user.getUserId());
        return Map.of("status", "success", "vacancies", vacancies);
    }
}