package com.courseproject.cvbuilderbackendv2.repository;

import com.courseproject.cvbuilderbackendv2.entity.Resume;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeRepository {
    Resume findResumeByResumeId(int resumeId);
    List<Resume> findResumeByUserId(int userId);
    Resume save(Resume resume);
    boolean deleteResumeByResumeId(int resumeId);
    Resume updateResume(Resume resume);
}
