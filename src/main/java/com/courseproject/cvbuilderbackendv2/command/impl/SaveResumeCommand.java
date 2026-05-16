package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.command.CommandHistory;
import com.courseproject.cvbuilderbackendv2.command.CommandMemento;
import com.courseproject.cvbuilderbackendv2.entity.Role;
import com.courseproject.cvbuilderbackendv2.entity.User;
import com.courseproject.cvbuilderbackendv2.service.EventService;
import com.courseproject.cvbuilderbackendv2.service.ResumeService;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SaveResumeCommand implements Command {
    private final ResumeService resumeService;
    private final UserService userService;
    private final EventService eventService;
    private final CommandHistory commandHistory;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String ERROR = "error";

    public SaveResumeCommand(ResumeService resumeService, UserService userService,
                             EventService eventService, CommandHistory commandHistory) {
        this.resumeService = resumeService;
        this.userService = userService;
        this.eventService = eventService;
        this.commandHistory = commandHistory;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        String userName = userService.extractUserName();
        if (userName.equals(ERROR)) {
            return Map.of(ERROR, ERROR);
        }

        boolean isUndoRedo = Boolean.TRUE.equals(params.get("_isUndoRedo"));

        JsonNode resumeData;
        Object rawData = params.get("resumeData");
        if (rawData instanceof JsonNode) {
            resumeData = (JsonNode) rawData;
        } else {
            resumeData = objectMapper.valueToTree(rawData);
        }

        boolean isPublic = params.containsKey("isPublic") && Boolean.parseBoolean(params.get("isPublic").toString());
        resumeService.save(userName, resumeData, isPublic);

        if (!isUndoRedo) {
            var resumes = resumeService.findResumesByUserId(userService.findUserId(userName));
            if (!resumes.isEmpty()) {
                int savedId = resumes.get(resumes.size() - 1).getResumeId();
                eventService.saveResumeEvent(savedId, "CREATED", resumeData.toString(), userName);
                commandHistory.pushUndo(userName, new CommandMemento("SAVERESUMECOMMAND",
                        Map.of("resumeId", savedId)));
            }
        }

        return Map.of("status", "success");
    }
}