package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.Security.JwtUtil;
import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.entity.Resume;
import com.courseproject.cvbuilderbackendv2.service.ResumeService;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        //TODO token validation?
        //TODO token iz header-a)
        String token = params.get("token").toString();
        String userName = jwtUtil.extractUsername(token);
        int userId = userService.findUserId(userName);
        List<Resume> resumes = resumeService.findResumesByUserId(userId);
        System.out.println(resumes);
        return Map.of("resumes", resumes);
//        return Map.copyOf(resumes.stream()
//                .collect(Collectors.toMap(resume -> String.valueOf(resume.getResumeId()), resume -> resume)));
    }
}
