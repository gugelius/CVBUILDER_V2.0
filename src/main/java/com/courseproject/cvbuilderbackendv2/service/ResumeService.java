package com.courseproject.cvbuilderbackendv2.service;

import com.courseproject.cvbuilderbackendv2.entity.Resume;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

public interface ResumeService {
    Resume findResumeByResumeId(int resumeId);
    List<Resume> findResumesByUserId(int userId);
    boolean save(String userName, JsonNode resumeData);
    boolean deleteResumeByResumeId(int resumeId);
    boolean updateResume(int resumeId, JsonNode resumeData);
}