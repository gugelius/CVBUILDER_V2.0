package com.courseproject.cvbuilderbackendv2.service;

import com.courseproject.cvbuilderbackendv2.entity.ResumeEvent;
import com.courseproject.cvbuilderbackendv2.entity.VacancyEvent;
import java.util.List;

public interface EventService {
    void saveResumeEvent(int resumeId, String eventType, String payload, String changedBy);
    void saveVacancyEvent(Long vacancyId, String eventType, String title, String description,
                          String requirements, String companyName, String changedBy);
    List<ResumeEvent> getResumeHistory(int resumeId);
    List<VacancyEvent> getVacancyHistory(Long vacancyId);
}