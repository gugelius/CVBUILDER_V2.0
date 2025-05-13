package com.courseproject.cvbuilderbackendv2.service.impl;

import com.courseproject.cvbuilderbackendv2.entity.Resume;
import com.courseproject.cvbuilderbackendv2.repository.ResumeRepository;
import com.courseproject.cvbuilderbackendv2.repository.UserRepository;
import com.courseproject.cvbuilderbackendv2.service.ResumeService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeServiceImpl implements ResumeService {
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    public ResumeServiceImpl(ResumeRepository resumeRepository, UserRepository userRepository){
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
    }
    @Override
    public Resume findResumeByResumeId(int resumeId){
        return resumeRepository.findResumeByResumeId(resumeId);
    }
    @Override
    public List<Resume> findResumeByUserId(int userId){
        return resumeRepository.findResumeByUserId(userId);
    }
//    @Override
//    public boolean save(Resume resume){
//        resumeRepository.save(resume);
//        return true;
//    }
    @Override
    public boolean save(String userName, JsonNode resumeData){
        resumeRepository.save(new Resume(userRepository.findByUserName(userName), resumeData);
        return true;
    }

    @Override
    public boolean deleteResumeByResumeId(int resumeId){
        return true;
    }
    @Override
    public boolean updateResume(Resume resume){
        return true;
    }
}
