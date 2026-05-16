package com.courseproject.cvbuilderbackendv2.repository;

import com.courseproject.cvbuilderbackendv2.entity.ResumeEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResumeEventRepository extends JpaRepository<ResumeEvent, Long> {
    List<ResumeEvent> findByResumeIdOrderByTimestampDesc(int resumeId);
}