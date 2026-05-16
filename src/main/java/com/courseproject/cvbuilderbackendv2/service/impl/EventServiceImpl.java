package com.courseproject.cvbuilderbackendv2.service.impl;

import com.courseproject.cvbuilderbackendv2.entity.ResumeEvent;
import com.courseproject.cvbuilderbackendv2.entity.VacancyEvent;
import com.courseproject.cvbuilderbackendv2.repository.ResumeEventRepository;
import com.courseproject.cvbuilderbackendv2.repository.VacancyEventRepository;
import com.courseproject.cvbuilderbackendv2.service.EventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final ResumeEventRepository resumeEventRepository;
    private final VacancyEventRepository vacancyEventRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EventServiceImpl(ResumeEventRepository resumeEventRepository,
                            VacancyEventRepository vacancyEventRepository) {
        this.resumeEventRepository = resumeEventRepository;
        this.vacancyEventRepository = vacancyEventRepository;
    }

    @Override
    public void saveResumeEvent(int resumeId, String eventType, String payload, String changedBy) {
        try {
            JsonNode jsonPayload = objectMapper.readTree(payload);
            resumeEventRepository.save(new ResumeEvent(resumeId, eventType, jsonPayload, changedBy));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse resume event payload", e);
        }
    }

    @Override
    public void saveVacancyEvent(Long vacancyId, String eventType, String title, String description,
                                 String requirements, String companyName, String changedBy) {
        vacancyEventRepository.save(new VacancyEvent(
                vacancyId, eventType, title, description, requirements, companyName, changedBy
        ));
    }

    @Override
    public List<ResumeEvent> getResumeHistory(int resumeId) {
        return resumeEventRepository.findByResumeIdOrderByTimestampDesc(resumeId);
    }

    @Override
    public List<VacancyEvent> getVacancyHistory(Long vacancyId) {
        return vacancyEventRepository.findByVacancyIdOrderByTimestampDesc(vacancyId);
    }
}