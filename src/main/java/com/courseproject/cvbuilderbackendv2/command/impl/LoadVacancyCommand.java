package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.entity.Vacancy;
import com.courseproject.cvbuilderbackendv2.repository.VacancyRepository;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LoadVacancyCommand implements Command {

    private final VacancyRepository vacancyRepository;

    public LoadVacancyCommand(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        Long vacancyId = Long.parseLong(params.get("vacancyId").toString());
        Vacancy vacancy = vacancyRepository.findById(vacancyId).orElse(null);

        if (vacancy == null) {
            return Map.of("status", "error", "message", "Vacancy not found");
        }

        return Map.of("status", "success", "vacancy", vacancy);
    }
}