package com.courseproject.cvbuilderbackendv2.repository;

import com.courseproject.cvbuilderbackendv2.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Integer> {
    Resume findResumeByResumeId(int resumeId);
    List<Resume> findResumesByUser_UserId(int userId);
    Resume save(Resume resume);
    int deleteResumeByResumeId(int resumeId);
}
