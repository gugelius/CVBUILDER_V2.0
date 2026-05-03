package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.dto.VacancyDTO;
import com.courseproject.cvbuilderbackendv2.entity.Vacancy;
import com.courseproject.cvbuilderbackendv2.service.VacancyService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class LoadVacanciesCommand implements Command {

    private final VacancyService vacancyService;

    public LoadVacanciesCommand(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        List<Vacancy> vacancies = vacancyService.findActiveVacancies();

        List<VacancyDTO> vacancyDTOs = vacancies.stream()
                .map(v -> new VacancyDTO(
                        v.getId(),
                        v.getTitle(),
                        v.getDescription(),
                        v.getRequirements(),
                        v.getCompanyName(),
                        v.getEmployer().getUserId(),
                        v.getEmployer().getUserName(),
                        v.isActive(),
                        v.getCreatedAt()))
                .toList();

        return Map.of("status", "success", "vacancies", vacancyDTOs);
    }
}