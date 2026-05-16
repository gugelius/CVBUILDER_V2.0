package com.courseproject.cvbuilderbackendv2.repository;

import com.courseproject.cvbuilderbackendv2.entity.VacancyEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VacancyEventRepository extends JpaRepository<VacancyEvent, Long> {
    List<VacancyEvent> findByVacancyIdOrderByTimestampDesc(Long vacancyId);
}