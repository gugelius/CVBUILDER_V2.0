package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.entity.Vacancy;
import com.courseproject.cvbuilderbackendv2.repository.VacancyRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class LoadVacanciesCommand implements Command {

    private final VacancyRepository vacancyRepository;

    public LoadVacanciesCommand(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        List<Vacancy> vacancies = vacancyRepository.findByActiveTrue();
        return Map.of("status", "success", "vacancies", vacancies);
    }
}