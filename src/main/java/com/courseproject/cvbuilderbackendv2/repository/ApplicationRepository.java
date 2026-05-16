package com.courseproject.cvbuilderbackendv2.repository;

import com.courseproject.cvbuilderbackendv2.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByApplicant_UserId(int applicantId);
    List<Application> findByVacancy_Id(Long vacancyId);
    boolean existsByVacancy_IdAndResume_ResumeId(Long vacancyId, int resumeId);
}