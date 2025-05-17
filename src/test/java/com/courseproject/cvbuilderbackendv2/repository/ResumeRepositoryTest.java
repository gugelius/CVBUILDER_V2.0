package com.courseproject.cvbuilderbackendv2.repository;

import com.courseproject.cvbuilderbackendv2.entity.Resume;
import com.courseproject.cvbuilderbackendv2.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class ResumeRepositoryTest {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void givenResume_whenFindResumeByResumeId_thenReturnResume() {
        User user = new User("testUser", "hashedPassword");
        userRepository.save(user);

        Resume resume = new Resume(user, null);
        resumeRepository.save(resume);

        Resume foundResume = resumeRepository.findResumeByResumeId(resume.getResumeId());

        assertNotNull(foundResume);
        assertEquals(resume.getResumeId(), foundResume.getResumeId());
    }

    @Test
    void givenUserWithResumes_whenFindResumesByUserId_thenReturnResumes() {
        User user = new User("testUser", "hashedPassword");
        userRepository.save(user);

        Resume resume1 = new Resume(user, null);
        Resume resume2 = new Resume(user, null);
        resumeRepository.saveAll(List.of(resume1, resume2));

        List<Resume> resumes = resumeRepository.findResumesByUser_UserId(user.getUserId());

        assertEquals(2, resumes.size());
    }

    @Test
    void givenResumeId_whenDeleteResumeByResumeId_thenResumeIsDeleted() {
        User user = new User("testUser", "hashedPassword");
        userRepository.save(user);

        Resume resume = new Resume(user, null);
        resumeRepository.save(resume);

        int deletedCount = resumeRepository.deleteResumeByResumeId(resume.getResumeId());

        assertEquals(1, deletedCount);
        assertNull(resumeRepository.findResumeByResumeId(resume.getResumeId()));
    }
}
