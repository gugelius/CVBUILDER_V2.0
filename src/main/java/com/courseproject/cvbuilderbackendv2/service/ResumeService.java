package com.courseproject.cvbuilderbackendv2.service;

import com.courseproject.cvbuilderbackendv2.entity.Resume;

import java.util.List;

public interface ResumeService {
    Resume findResumeByResumeId(int resumeId);
    List<Resume> findResumeByUserId(int userId);
    boolean save(Resume resume);
    boolean deleteResumeByResumeId(int resumeId);
    boolean updateResume(Resume resume);
}
