package com.courseproject.cvbuilderbackendv2.command;

import org.springframework.stereotype.Component;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
public class CommandHistory {
    public final Map<String, Deque<CommandMemento>> undoStacks = new ConcurrentHashMap<>();
    public final Map<String, Deque<CommandMemento>> redoStacks = new ConcurrentHashMap<>();

    public void pushUndo(String username, CommandMemento memento) {
        undoStacks.computeIfAbsent(username, k -> new ConcurrentLinkedDeque<>()).push(memento);
        redoStacks.computeIfAbsent(username, k -> new ConcurrentLinkedDeque<>()).clear();
    }

    public CommandMemento popUndo(String username) {
        Deque<CommandMemento> stack = undoStacks.get(username);
        if (stack == null || stack.isEmpty()) return null;
        CommandMemento m = stack.pop();
        redoStacks.computeIfAbsent(username, k -> new ConcurrentLinkedDeque<>()).push(m);
        return m;
    }

    public CommandMemento popRedo(String username) {
        Deque<CommandMemento> stack = redoStacks.get(username);
        if (stack == null || stack.isEmpty()) return null;
        CommandMemento m = stack.pop();
        undoStacks.computeIfAbsent(username, k -> new ConcurrentLinkedDeque<>()).push(m);
        return m;
    }

    public boolean canUndo(String username) {
        Deque<CommandMemento> stack = undoStacks.get(username);
        return stack != null && !stack.isEmpty();
    }

    public boolean canRedo(String username) {
        Deque<CommandMemento> stack = redoStacks.get(username);
        return stack != null && !stack.isEmpty();
    }
}