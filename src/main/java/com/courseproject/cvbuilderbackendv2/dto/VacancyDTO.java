package com.courseproject.cvbuilderbackendv2.dto;

import java.time.LocalDateTime;

public class VacancyDTO {
    private Long id;
    private String title;
    private String description;
    private String requirements;
    private String companyName;
    private int employerId;
    private String employerName;
    private boolean active;
    private LocalDateTime createdAt;

    public VacancyDTO() {}

    public VacancyDTO(Long id, String title, String description, String requirements,
                      String companyName, int employerId, String employerName,
                      boolean active, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.companyName = companyName;
        this.employerId = employerId;
        this.employerName = employerName;
        this.active = active;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public int getEmployerId() { return employerId; }
    public void setEmployerId(int employerId) { this.employerId = employerId; }

    public String getEmployerName() { return employerName; }
    public void setEmployerName(String employerName) { this.employerName = employerName; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}