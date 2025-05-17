package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.Security.JwtUtil;
import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.entity.Resume;
import com.courseproject.cvbuilderbackendv2.service.ResumeService;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class LoadResumesCommand implements Command {
    private final ResumeService resumeService;
    private final UserService userService;
    protected JwtUtil jwtUtil;
    private static final String ERROR = "error";
    public LoadResumesCommand(ResumeService resumeService, UserService userService, JwtUtil jwtUtil){
        this.resumeService = resumeService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }
    @Override
    public Map<String, Object> execute(Map<String, Object> params){
        String userName = userService.extractUserName();
        if (userName.equals(ERROR)){
            return Map.of(ERROR,ERROR);
        }
        int userId = userService.findUserId(userName);
        List<Resume> resumes = resumeService.findResumesByUserId(userId);
        return Map.of("resumes", resumes);
    }
}