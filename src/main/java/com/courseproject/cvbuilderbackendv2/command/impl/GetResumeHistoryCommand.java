package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.entity.ResumeEvent;
import com.courseproject.cvbuilderbackendv2.service.EventService;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class GetResumeHistoryCommand implements Command {
    private final EventService eventService;

    public GetResumeHistoryCommand(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        int resumeId = Integer.parseInt(params.get("resumeId").toString());
        List<ResumeEvent> events = eventService.getResumeHistory(resumeId);
        return Map.of("status", "success", "events", events);
    }
}