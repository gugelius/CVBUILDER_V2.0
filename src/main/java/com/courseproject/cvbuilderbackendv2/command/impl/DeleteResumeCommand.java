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
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DeleteResumeCommand implements Command {
    private final ResumeService resumeService;
    private final UserService userService;
    private final EventService eventService;
    private final CommandHistory commandHistory;

    public DeleteResumeCommand(ResumeService resumeService, UserService userService,
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
            return Map.of("status", "error", "message", "Only seekers can delete resumes");
        }

        int resumeId = Integer.parseInt(params.get("resumeId").toString());
        Resume resume = resumeService.findResumeByResumeId(resumeId);

        if (resume == null) {
            return Map.of("status", "error", "message", "Resume not found");
        }

        if (resume.getUser().getUserId() != user.getUserId()) {
            return Map.of("status", "error", "message", "You can only delete your own resumes");
        }

        boolean isUndoRedo = Boolean.TRUE.equals(params.get("_isUndoRedo"));
        Map<String, Object> undoParams = new HashMap<>();
        undoParams.put("resumeData", resume.getResumeData().deepCopy());
        undoParams.put("isPublic", resume.isPublic());

        if (!isUndoRedo) {
            eventService.saveResumeEvent(resumeId, "DELETED", resume.getResumeData().toString(), userName);
        }

        boolean deleted = resumeService.deleteResumeByResumeId(resumeId);

        if (deleted && !isUndoRedo) {
            commandHistory.pushUndo(userName, new CommandMemento("DELETERESUMECOMMAND", undoParams));
        }

        return Map.of("status", deleted ? "success" : "error");
    }
}