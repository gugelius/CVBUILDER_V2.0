package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.Security.JwtUtil;
import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.service.ResumeService;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public class LoadResumesCommand implements Command {
    private final ResumeService resumeService;
    private final UserService userService;
    protected JwtUtil jwtUtil;
    public LoadResumesCommand(ResumeService resumeService, UserService userService, JwtUtil jwtUtil){
        this.resumeService = resumeService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }
    @Override
    public Map<String, Object> execute(Map<String, Object> params){
        System.out.println(params);
        //TODO token validation?
        //TODO token iz header-a)
        String token = params.get("token").toString();
        System.out.println(token);
        String userName = jwtUtil.extractUsername(token);
        System.out.println(userName);
        int userId = userService.findUserId(userName);
        System.out.println(userId);
        System.out.println(resumeService.findResumesByUserId(userId).toString());
        return Map.of("resumes",resumeService.findResumesByUserId(userId));
    }
}
