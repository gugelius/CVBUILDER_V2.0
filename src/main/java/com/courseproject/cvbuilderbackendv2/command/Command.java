package com.courseproject.cvbuilderbackendv2.command;

import java.io.IOException;
import java.util.Map;

public interface Command {
    Map<String, Object> execute(Map<String, Object> params) throws IOException;
    default String getName() { return this.getClass().getSimpleName().replace("Command", ""); }
    default void refresh() {}
}