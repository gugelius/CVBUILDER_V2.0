package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.command.CommandHistory;
import com.courseproject.cvbuilderbackendv2.command.CommandMemento;
import com.courseproject.cvbuilderbackendv2.entity.Resume;
import com.courseproject.cvbuilderbackendv2.entity.Role;
import com.courseproject.cvbuilderbackendv2.entity.User;
import com.courseproject.cvbuilderbackendv2.service.EventService;
import com.courseproject.cvbuilderbackendv2.service.ResumeService;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UpdateResumeCommand implements Command {
    private final ResumeService resumeService;
    private final UserService userService;
    private final EventService eventService;
    private final CommandHistory commandHistory;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UpdateResumeCommand(ResumeService resumeService, UserService userService,
                               EventService eventService, CommandHistory commandHistory) {
        this.resumeService = resumeService;
        this.userService = userService;
        this.eventService = eventService;
        this.commandHistory = commandHistory;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        String userName = userService.extractUserName();
        if ("error".equals(userName)) {
            return Map.of("error", "Not authenticated");
        }

        User user = userService.findUserByUsername(userName);
        if (user.getRole() != Role.ROLE_SEEKER) {
            return Map.of("status", "error", "message", "Only seekers can update resumes");
        }

        int resumeId = Integer.parseInt(params.get("resumeId").toString());
        Resume resume = resumeService.findResumeByResumeId(resumeId);

        if (resume == null) {
            return Map.of("status", "error", "message", "Resume not found");
        }

        if (resume.getUser().getUserId() != user.getUserId()) {
            return Map.of("status", "error", "message", "You can only update your own resumes");
        }

        Map<String, Object> undoParams = new HashMap<>();
        undoParams.put("resumeId", resumeId);
        undoParams.put("resumeData", resume.getResumeData().deepCopy());
        undoParams.put("isPublic", resume.isPublic());

        JsonNode resumeData = objectMapper.valueToTree(params.get("resumeData"));
        boolean isPublic = params.containsKey("isPublic") && Boolean.parseBoolean(params.get("isPublic").toString());
        boolean updated = resumeService.updateResume(resumeId, resumeData, isPublic);

        if (updated) {
            boolean isUndoRedo = Boolean.TRUE.equals(params.get("_isUndoRedo"));
            if (!isUndoRedo) {
                eventService.saveResumeEvent(resumeId, "UPDATED", resumeData.toString(), userName);
                commandHistory.pushUndo(userName, new CommandMemento("UPDATERESUMECOMMAND", undoParams));
            }
        }

        return Map.of("status", updated ? "success" : "error");
    }
}