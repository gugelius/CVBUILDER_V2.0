package com.courseproject.cvbuilderbackendv2.command.impl;


import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import com.courseproject.cvbuilderbackendv2.service.impl.UserServiceImpl;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Map;

@Component
public class LoginCommand implements Command {
    private final UserService userService;
    public LoginCommand(UserService userService){this.userService = userService;}
    @Override
    public String execute(Map<String, String> params, Model model){
        String userName = params.get("username");
        String userPassword = params.get("password");
        if(userService.authenticate(userName, userPassword)){
            return "main";
        } else{
            model.addAttribute("err", "Incorrect login or pass");
            return "index";
        }
    }
}
