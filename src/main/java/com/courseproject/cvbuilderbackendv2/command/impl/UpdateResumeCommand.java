package com.courseproject.cvbuilderbackendv2.command.impl;

import com.courseproject.cvbuilderbackendv2.Security.JwtUtil;
import com.courseproject.cvbuilderbackendv2.command.Command;
import com.courseproject.cvbuilderbackendv2.service.ResumeService;
import com.courseproject.cvbuilderbackendv2.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UpdateResumeCommand implements Command {
    private final ResumeService resumeService;
    private final UserService userService;
    protected JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UpdateResumeCommand(ResumeService resumeService, UserService userService, JwtUtil jwtUtil) {
        this.resumeService = resumeService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }
    @Override
    public Map<String, Object> execute(Map<String, Object> params) {
        //todo validation
        //todo вытягивай токен не из тела, придумай как
        //todo настрой бля секурити и не только
        System.out.println("Poluchennie dannie: " + params);
//        String token = params.get("token").toString();
//        System.out.println(token);
//        String userName = jwtUtil.extractUsername(token);
//        System.out.println(userName);
        int resumeId = Integer.valueOf((String)params.get("resumeId"));
        JsonNode resumeData = objectMapper.valueToTree(params.get("resumeData"));
        resumeService.updateResume(resumeId,resumeData);
        return Map.of("status", "success");
    }
}
