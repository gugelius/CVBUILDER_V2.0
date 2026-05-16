package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.entity.Role;
import com.courseproject.cvbuilderbackendv2.entity.User;
import com.courseproject.cvbuilderbackendv2.service.ApplicationService;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class RejectApplicationCommand implements Command {

    private final ApplicationService applicationService;
    private final UserService userService;

    public RejectApplicationCommand(ApplicationService applicationService, UserService userService) {
        this.applicationService = applicationService;
        this.userService = userService;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        String userName = userService.extractUserName();
        if ("error".equals(userName)) {
            return Map.of("error", "Not authenticated");
        }

        User user = userService.findUserByUsername(userName);
        if (user.getRole() != Role.ROLE_EMPLOYER) {
            return Map.of("status", "error", "message", "Only employers can reject applications");
        }

        Long applicationId = Long.parseLong(params.get("applicationId").toString());
        applicationService.rejectApplication(applicationId, userName);

        return Map.of("status", "success", "message", "Application rejected");
    }
}