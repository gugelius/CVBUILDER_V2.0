package com.courseproject.cvbuilderbackendv2.repository;

import com.courseproject.cvbuilderbackendv2.entity.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
    List<Vacancy> findByEmployer_UserId(int userId);
    List<Vacancy> findByActiveTrue();
}