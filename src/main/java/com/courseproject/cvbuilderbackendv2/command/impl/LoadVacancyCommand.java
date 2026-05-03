package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.dto.VacancyDTO;
import com.courseproject.cvbuilderbackendv2.entity.Vacancy;
import com.courseproject.cvbuilderbackendv2.service.VacancyService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LoadVacancyCommand implements Command {

    private final VacancyService vacancyService;

    public LoadVacancyCommand(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        Long vacancyId = Long.parseLong(params.get("vacancyId").toString());
        Vacancy vacancy = vacancyService.findVacancyByVacancyId(vacancyId);

        if (vacancy == null) {
            return Map.of("status", "error", "message", "Vacancy not found");
        }

        VacancyDTO vacancyDTO = new VacancyDTO(
                vacancy.getId(),
                vacancy.getTitle(),
                vacancy.getDescription(),
                vacancy.getRequirements(),
                vacancy.getCompanyName(),
                vacancy.getEmployer().getUserId(),
                vacancy.getEmployer().getUserName(),
                vacancy.isActive(),
                vacancy.getCreatedAt()
        );

        return Map.of("status", "success", "vacancy", vacancyDTO);
    }
}