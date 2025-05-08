package com.courseproject.cvbuilderbackendv2.command;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class CommandRegistry {
    private final static String cmd = "COMMAND";
    private final Map<String, Command> commandMap;

    @Autowired
    public CommandRegistry(Set<Command> commands) {
        commandMap = commands.stream()
                .collect(Collectors.toMap(
                        cmd -> cmd.getClass().getSimpleName().toUpperCase(),
                        Function.identity()
                ));
    }

    public Command getCommand(String commandStr) {
        Command command = commandMap.get(commandStr.toUpperCase()+cmd);
        if (command == null) {
            throw new UnsupportedOperationException("Unknown command: " + commandStr);
        }
        return command;
    }
}
