package com.courseproject.cvbuilderbackendv2.service.impl;

import com.courseproject.cvbuilderbackendv2.entity.User;
import com.courseproject.cvbuilderbackendv2.entity.Vacancy;
import com.courseproject.cvbuilderbackendv2.repository.UserRepository;
import com.courseproject.cvbuilderbackendv2.repository.VacancyRepository;
import com.courseproject.cvbuilderbackendv2.service.VacancyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VacancyServiceImpl implements VacancyService {

    private final VacancyRepository vacancyRepository;
    private final UserRepository userRepository;

    public VacancyServiceImpl(VacancyRepository vacancyRepository, UserRepository userRepository) {
        this.vacancyRepository = vacancyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Vacancy findVacancyByVacancyId(Long vacancyId) {
        return vacancyRepository.findById(vacancyId).orElse(null);
    }

    @Override
    public List<Vacancy> findVacanciesByEmployerUserId(int userId) {
        return vacancyRepository.findByEmployer_UserId(userId);
    }

    @Override
    public Vacancy saveVacancy(String title, String description, String requirements, String companyName, String userName) {
        User user = userRepository.findByUserName(userName);
        Vacancy vacancy = new Vacancy(title, description, requirements, companyName, user);
        return vacancyRepository.save(vacancy);
    }

    @Override
    public void deleteVacancyByVacancyId(Long vacancyId) {
        vacancyRepository.deleteById(vacancyId);
    }

    @Override
    public Vacancy updateVacancy(Long vacancyId, String title, String description, String requirements, String companyName) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId).orElseThrow();
        if (title != null) vacancy.setTitle(title);
        if (description != null) vacancy.setDescription(description);
        if (requirements != null) vacancy.setRequirements(requirements);
        if (companyName != null) vacancy.setCompanyName(companyName);
        return vacancyRepository.save(vacancy);
    }

    @Override
    public List<Vacancy> findActiveVacancies() {
        return vacancyRepository.findByActiveTrue();
    }
}