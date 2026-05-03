package com.courseproject.cvbuilderbackendv2.service;

import com.courseproject.cvbuilderbackendv2.entity.Vacancy;

import java.util.List;
import java.util.Optional;

public interface VacancyService {
    Vacancy findVacancyByVacancyId(Long vacancyId);
    List<Vacancy> findVacanciesByEmployerUserId(int userId);
    Vacancy saveVacancy(String title, String description, String requirements, String companyName, String userName);
    void deleteVacancyByVacancyId(Long vacancyId);
    Vacancy updateVacancy(Long vacancyId, String title, String description, String requirements, String companyName);
    List<Vacancy> findActiveVacancies();
}