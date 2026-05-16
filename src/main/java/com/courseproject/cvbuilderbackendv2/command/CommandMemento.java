package com.courseproject.cvbuilderbackendv2.command;

import java.util.HashMap;
import java.util.Map;

public class CommandMemento {
    private final String commandName;
    private final Map<String, Object> params;
    private final long timestamp;

    public CommandMemento(String commandName, Map<String, Object> params) {
        this.commandName = commandName;
        this.params = new HashMap<>(params);
        this.timestamp = System.currentTimeMillis();
    }

    public String getCommandName() { return commandName; }
    public Map<String, Object> getParams() { return params; }
    public long getTimestamp() { return timestamp; }
}