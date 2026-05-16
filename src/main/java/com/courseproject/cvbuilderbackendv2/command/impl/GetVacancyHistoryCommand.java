package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.entity.VacancyEvent;
import com.courseproject.cvbuilderbackendv2.service.EventService;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class GetVacancyHistoryCommand implements Command {
    private final EventService eventService;

    public GetVacancyHistoryCommand(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        Long vacancyId = Long.parseLong(params.get("vacancyId").toString());
        List<VacancyEvent> events = eventService.getVacancyHistory(vacancyId);
        return Map.of("status", "success", "events", events);
    }
}