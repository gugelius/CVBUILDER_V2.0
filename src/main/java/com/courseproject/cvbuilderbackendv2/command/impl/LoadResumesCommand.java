package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.Security.JwtUtil;
import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.entity.Resume;
import com.courseproject.cvbuilderbackendv2.service.ResumeService;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.util.List;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            return Map.of("error", "User is not authenticated");
        }

        String userName = authentication.getName();
        System.out.println("Authenticated user: " + userName);
        int userId = userService.findUserId(userName);
        List<Resume> resumes = resumeService.findResumesByUserId(userId);
        System.out.println(resumes);
        return Map.of("resumes", resumes);
    }
}