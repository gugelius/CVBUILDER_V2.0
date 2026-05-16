package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.*;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class RedoCommand implements Command {
    private final CommandRegistry commandRegistry;
    private final CommandHistory commandHistory;
    private final UserService userService;

    public RedoCommand(@Lazy CommandRegistry commandRegistry, CommandHistory commandHistory, UserService userService) {
        this.commandRegistry = commandRegistry;
        this.commandHistory = commandHistory;
        this.userService = userService;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        String username = userService.extractUserName();
        if ("error".equals(username)) {
            return Map.of("error", "Not authenticated");
        }

        CommandMemento memento = commandHistory.popRedo(username);
        if (memento == null) {
            return Map.of("status", "error", "message", "Nothing to redo");
        }

        String commandName = memento.getCommandName().replace("COMMAND", "");
        Command command = commandRegistry.getCommand(commandName);

        Map<String, Object> paramsWithFlag = new HashMap<>(memento.getParams());
        paramsWithFlag.put("_isUndoRedo", true);

        try {
            return command.execute(paramsWithFlag);
        } catch (IOException e) {
            return Map.of("status", "error", "message", "Redo failed: " + e.getMessage());
        }
    }
}