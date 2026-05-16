package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.command.CommandHistory;
import com.courseproject.cvbuilderbackendv2.command.CommandMemento;
import com.courseproject.cvbuilderbackendv2.entity.Role;
import com.courseproject.cvbuilderbackendv2.entity.User;
import com.courseproject.cvbuilderbackendv2.entity.Vacancy;
import com.courseproject.cvbuilderbackendv2.service.EventService;
import com.courseproject.cvbuilderbackendv2.service.VacancyService;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UpdateVacancyCommand implements Command {

    private final VacancyService vacancyService;
    private final UserService userService;
    private final EventService eventService;
    private final CommandHistory commandHistory;

    public UpdateVacancyCommand(VacancyService vacancyService, UserService userService,
                                EventService eventService, CommandHistory commandHistory) {
        this.vacancyService = vacancyService;
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
        if (user.getRole() != Role.ROLE_EMPLOYER) {
            return Map.of("status", "error", "message", "Only employers can update vacancies");
        }

        Long vacancyId = Long.parseLong(params.get("vacancyId").toString());
        Vacancy vacancy = vacancyService.findVacancyByVacancyId(vacancyId);

        if (vacancy == null) {
            return Map.of("status", "error", "message", "Vacancy not found");
        }

        if (vacancy.getEmployer().getUserId() != user.getUserId()) {
            return Map.of("status", "error", "message", "You can only update your own vacancies");
        }

        String title = params.containsKey("title") ? (String) params.get("title") : null;
        String description = params.containsKey("description") ? (String) params.get("description") : null;
        String requirements = params.containsKey("requirements") ? (String) params.get("requirements") : null;
        String companyName = params.containsKey("companyName") ? (String) params.get("companyName") : null;

        Map<String, Object> undoParams = new HashMap<>();
        undoParams.put("vacancyId", vacancyId);
        undoParams.put("title", vacancy.getTitle());
        undoParams.put("description", vacancy.getDescription());
        undoParams.put("requirements", vacancy.getRequirements());
        undoParams.put("companyName", vacancy.getCompanyName());

        vacancyService.updateVacancy(vacancyId, title, description, requirements, companyName);

        boolean isUndoRedo = Boolean.TRUE.equals(params.get("_isUndoRedo"));
        if (!isUndoRedo) {
            eventService.saveVacancyEvent(vacancyId, "UPDATED", title, description, requirements, companyName, userName);
            commandHistory.pushUndo(userName, new CommandMemento("UPDATEVACANCYCOMMAND", undoParams));
        }

        return Map.of("status", "success");
    }
}