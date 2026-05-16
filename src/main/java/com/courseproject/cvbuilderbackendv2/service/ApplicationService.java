package com.courseproject.cvbuilderbackendv2.service;

import com.courseproject.cvbuilderbackendv2.entity.Application;
import java.util.List;

public interface ApplicationService {
    Application apply(Long vacancyId, int resumeId, String userName, String coverLetter);
    List<Application> getMyApplications(String userName);
    List<Application> getVacancyApplications(Long vacancyId);
    Application acceptApplication(Long applicationId, String employerName);
    Application rejectApplication(Long applicationId, String employerName);
}