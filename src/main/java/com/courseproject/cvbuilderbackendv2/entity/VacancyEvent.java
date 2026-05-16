package com.courseproject.cvbuilderbackendv2.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vacancy_events")
public class VacancyEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long vacancyId;

    @Column(nullable = false)
    private String eventType;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    private String companyName;

    @Column(nullable = false)
    private String changedBy;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    public VacancyEvent() {}

    public VacancyEvent(Long vacancyId, String eventType, String title, String description,
                        String requirements, String companyName, String changedBy) {
        this.vacancyId = vacancyId;
        this.eventType = eventType;
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.companyName = companyName;
        this.changedBy = changedBy;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getVacancyId() { return vacancyId; }
    public void setVacancyId(Long vacancyId) { this.vacancyId = vacancyId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}