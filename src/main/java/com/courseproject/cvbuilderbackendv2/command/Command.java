package com.courseproject.cvbuilderbackendv2.command;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@FunctionalInterface
public interface Command {
    String execute(Map<String, String> params, Model model) throws IOException;// TODO model не нужно будет, оно ж для JSP
    default void refresh(){}
}