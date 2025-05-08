package com.courseproject.cvbuilderbackendv2.command.impl;


import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import com.courseproject.cvbuilderbackendv2.service.impl.UserServiceImpl;
import org.apache.catalina.User;
import org.springframework.ui.Model;

import java.util.Map;

public class LoginCommand implements Command {
    @Override
    public String execute(Map<String, String> params, Model model){
        UserService userService = UserServiceImpl.getInstance();
        if(userService.login(params)){
            return "main";
        } else{
            model.addAttribute("err", "Incorrect login or pass");
            return "index";
        }
    }
}
