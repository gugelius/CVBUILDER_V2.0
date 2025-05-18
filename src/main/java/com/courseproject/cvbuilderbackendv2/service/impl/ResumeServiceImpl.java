package com.courseproject.cvbuilderbackendv2.service.impl;

import com.courseproject.cvbuilderbackendv2.entity.Resume;
import com.courseproject.cvbuilderbackendv2.repository.ResumeRepository;
import com.courseproject.cvbuilderbackendv2.repository.UserRepository;
import com.courseproject.cvbuilderbackendv2.service.ResumeService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    public List<Resume> findResumesByUserId(int userId){
        return CompletableFuture.supplyAsync(() -> resumeRepository.findResumesByUser_UserId(userId))
                .join();
    }

    @Async
    @Override
    public CompletableFuture<Boolean> save(String userName, JsonNode resumeData){
        resumeRepository.save(new Resume(userRepository.findByUserName(userName), resumeData));
        return CompletableFuture.completedFuture(true);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean deleteResumeByResumeId(int resumeId){
        return (resumeRepository.deleteResumeByResumeId(resumeId)>0);
    }
  
    @Override
    @Lock(LockModeType.OPTIMISTIC)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean updateResume(int resumeId, JsonNode resumeData) {
        return resumeRepository.findById(resumeId)
                .map(resume -> {
                    resume.setResumeData(resumeData);
                    resumeRepository.save(resume);
                    return true;
                })
                .orElse(false);
    }
}