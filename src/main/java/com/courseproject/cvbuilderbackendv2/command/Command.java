package com.courseproject.cvbuilderbackendv2.command;

import java.io.IOException;
import java.util.Map;

@FunctionalInterface
public interface Command {
    Map<String, Object> execute(Map<String, String> params) throws IOException;
    default void refresh() {}
}
