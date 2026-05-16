package com.courseproject.cvbuilderbackendv2.dto;

import com.courseproject.cvbuilderbackendv2.entity.ApplicationStatus;
import java.time.LocalDateTime;

public class ApplicationDTO {
    private Long id;
    private Long vacancyId;
    private String vacancyTitle;
    private int resumeId;
    private int applicantId;
    private String applicantName;
    private String coverLetter;
    private ApplicationStatus status;
    private LocalDateTime createdAt;

    public ApplicationDTO() {}

    public ApplicationDTO(Long id, Long vacancyId, String vacancyTitle, int resumeId,
                          int applicantId, String applicantName, String coverLetter,
                          ApplicationStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.vacancyId = vacancyId;
        this.vacancyTitle = vacancyTitle;
        this.resumeId = resumeId;
        this.applicantId = applicantId;
        this.applicantName = applicantName;
        this.coverLetter = coverLetter;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getVacancyId() { return vacancyId; }
    public void setVacancyId(Long vacancyId) { this.vacancyId = vacancyId; }

    public String getVacancyTitle() { return vacancyTitle; }
    public void setVacancyTitle(String vacancyTitle) { this.vacancyTitle = vacancyTitle; }

    public int getResumeId() { return resumeId; }
    public void setResumeId(int resumeId) { this.resumeId = resumeId; }

    public int getApplicantId() { return applicantId; }
    public void setApplicantId(int applicantId) { this.applicantId = applicantId; }

    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }

    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}