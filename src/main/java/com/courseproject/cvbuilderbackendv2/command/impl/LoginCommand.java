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
    public LoginCommand(UserService userService){
        this.userService = userService;
    }
    @Override
    public String execute(Map<String, String> params, Model model){
//        UserService userService = UserServiceImpl.getInstance();
        if(userService.login(params)){
            return "main";
        } else{
            model.addAttribute("err", "Incorrect login or pass");
            return "index";
        }
    }
}
