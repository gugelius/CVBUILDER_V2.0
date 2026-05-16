package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.*;
import com.courseproject.cvbuilderbackendv2.entity.Resume;
import com.courseproject.cvbuilderbackendv2.entity.Vacancy;
import com.courseproject.cvbuilderbackendv2.service.ResumeService;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import com.courseproject.cvbuilderbackendv2.service.VacancyService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class UndoCommand implements Command {
    private final CommandRegistry commandRegistry;
    private final CommandHistory commandHistory;
    private final UserService userService;
    private final ResumeService resumeService;
    private final VacancyService vacancyService;

    public UndoCommand(@Lazy CommandRegistry commandRegistry, CommandHistory commandHistory,
                       UserService userService, ResumeService resumeService,
                       VacancyService vacancyService) {
        this.commandRegistry = commandRegistry;
        this.commandHistory = commandHistory;
        this.userService = userService;
        this.resumeService = resumeService;
        this.vacancyService = vacancyService;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        String username = userService.extractUserName();
        if ("error".equals(username)) {
            return Map.of("error", "Not authenticated");
        }

        CommandMemento memento = commandHistory.popUndo(username);
        if (memento == null) {
            return Map.of("status", "error", "message", "Nothing to undo");
        }

        if (memento.getCommandName().equals("UPDATERESUMECOMMAND")) {
            int resumeId = Integer.parseInt(memento.getParams().get("resumeId").toString());
            Resume resume = resumeService.findResumeByResumeId(resumeId);
            if (resume != null) {
                Map<String, Object> correctRedoParams = new HashMap<>();
                correctRedoParams.put("resumeId", resumeId);
                correctRedoParams.put("resumeData", resume.getResumeData().deepCopy());
                correctRedoParams.put("isPublic", resume.isPublic());
                commandHistory.redoStacks.get(username).pop();
                commandHistory.redoStacks.get(username).push(
                        new CommandMemento("UPDATERESUMECOMMAND", correctRedoParams));
            }
        }

        if (memento.getCommandName().equals("UPDATEVACANCYCOMMAND")) {
            Long vacancyId = Long.parseLong(memento.getParams().get("vacancyId").toString());
            Vacancy vacancy = vacancyService.findVacancyByVacancyId(vacancyId);
            if (vacancy != null) {
                Map<String, Object> correctRedoParams = new HashMap<>();
                correctRedoParams.put("vacancyId", vacancyId);
                correctRedoParams.put("title", vacancy.getTitle());
                correctRedoParams.put("description", vacancy.getDescription());
                correctRedoParams.put("requirements", vacancy.getRequirements());
                correctRedoParams.put("companyName", vacancy.getCompanyName());
                commandHistory.redoStacks.get(username).pop();
                commandHistory.redoStacks.get(username).push(
                        new CommandMemento("UPDATEVACANCYCOMMAND", correctRedoParams));
            }
        }

        String reverseCommand = getReverseCommand(memento.getCommandName());
        Command command = commandRegistry.getCommand(reverseCommand);

        Map<String, Object> paramsWithFlag = new HashMap<>(memento.getParams());
        paramsWithFlag.put("_isUndoRedo", true);

        try {
            return command.execute(paramsWithFlag);
        } catch (IOException e) {
            return Map.of("status", "error", "message", "Undo failed: " + e.getMessage());
        }
    }

    private String getReverseCommand(String commandName) {
        return switch (commandName) {
            case "SAVERESUMECOMMAND" -> "DeleteResume";
            case "DELETERESUMECOMMAND" -> "SaveResume";
            case "UPDATERESUMECOMMAND" -> "UpdateResume";
            case "SAVEVACANCYCOMMAND" -> "DeleteVacancy";
            case "DELETEVACANCYCOMMAND" -> "SaveVacancy";
            case "UPDATEVACANCYCOMMAND" -> "UpdateVacancy";
            default -> throw new UnsupportedOperationException("Cannot undo: " + commandName);
        };
    }
}