package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Map;

@Component
public class RegisterCommand implements Command {
    private final UserService userService;
    public RegisterCommand(UserService userService){
        this.userService = userService;
    }
    @Override
    public String execute(Map<String, String> params, Model model){
        String userName = params.get("username");
        String userPassword = params.get("password");
        if(userService.register(userName, userPassword)){
            return "main";
        } else{
            model.addAttribute("err", "Incorrect login or pass");
            return "register";
        }
    }
}
