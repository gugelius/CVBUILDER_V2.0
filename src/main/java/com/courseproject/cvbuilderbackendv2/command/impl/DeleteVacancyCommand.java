package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.command.CommandHistory;
import com.courseproject.cvbuilderbackendv2.command.CommandMemento;
import com.courseproject.cvbuilderbackendv2.entity.Role;
import com.courseproject.cvbuilderbackendv2.entity.User;
import com.courseproject.cvbuilderbackendv2.entity.Vacancy;
import com.courseproject.cvbuilderbackendv2.service.EventService;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import com.courseproject.cvbuilderbackendv2.service.VacancyService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DeleteVacancyCommand implements Command {

    private final VacancyService vacancyService;
    private final UserService userService;
    private final EventService eventService;
    private final CommandHistory commandHistory;

    public DeleteVacancyCommand(VacancyService vacancyService, UserService userService,
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
            return Map.of("status", "error", "message", "Only employers can delete vacancies");
        }

        Long vacancyId = Long.parseLong(params.get("vacancyId").toString());
        Vacancy vacancy = vacancyService.findVacancyByVacancyId(vacancyId);

        if (vacancy == null) {
            return Map.of("status", "error", "message", "Vacancy not found");
        }

        if (vacancy.getEmployer().getUserId() != user.getUserId()) {
            return Map.of("status", "error", "message", "You can only delete your own vacancies");
        }

        boolean isUndoRedo = Boolean.TRUE.equals(params.get("_isUndoRedo"));

        Map<String, Object> undoParams = new HashMap<>();
        undoParams.put("title", vacancy.getTitle());
        undoParams.put("description", vacancy.getDescription());
        undoParams.put("requirements", vacancy.getRequirements());
        undoParams.put("companyName", vacancy.getCompanyName());

        if (!isUndoRedo) {
            eventService.saveVacancyEvent(vacancyId, "DELETED", vacancy.getTitle(), vacancy.getDescription(),
                    vacancy.getRequirements(), vacancy.getCompanyName(), userName);
        }

        vacancyService.deleteVacancyByVacancyId(vacancyId);

        if (!isUndoRedo) {
            commandHistory.pushUndo(userName, new CommandMemento("DELETEVACANCYCOMMAND", undoParams));
        }

        return Map.of("status", "success");
    }
}