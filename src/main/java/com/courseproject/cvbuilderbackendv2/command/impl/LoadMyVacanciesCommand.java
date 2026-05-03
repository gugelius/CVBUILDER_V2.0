package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.dto.VacancyDTO;
import com.courseproject.cvbuilderbackendv2.entity.Role;
import com.courseproject.cvbuilderbackendv2.entity.User;
import com.courseproject.cvbuilderbackendv2.entity.Vacancy;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import com.courseproject.cvbuilderbackendv2.service.VacancyService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class LoadMyVacanciesCommand implements Command {

    private final VacancyService vacancyService;
    private final UserService userService;

    public LoadMyVacanciesCommand(VacancyService vacancyService, UserService userService) {
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
            return Map.of("status", "error", "message", "Only employers can view their vacancies");
        }

        List<Vacancy> vacancies = vacancyService.findVacanciesByEmployerUserId(user.getUserId());

        List<VacancyDTO> vacancyDTOs = vacancies.stream()
                .map(vacancy -> new VacancyDTO(
                        vacancy.getId(),
                        vacancy.getTitle(),
                        vacancy.getDescription(),
                        vacancy.getRequirements(),
                        vacancy.getCompanyName(),
                        vacancy.getEmployer().getUserId(),
                        vacancy.getEmployer().getUserName(),
                        vacancy.isActive(),
                        vacancy.getCreatedAt()
                ))
                .toList();

        return Map.of("status", "success", "vacancies", vacancyDTOs);
    }
}