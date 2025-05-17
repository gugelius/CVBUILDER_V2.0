package com.courseproject.cvbuilderbackendv2.service;

import com.courseproject.cvbuilderbackendv2.entity.Resume;
import com.courseproject.cvbuilderbackendv2.repository.ResumeRepository;
import com.courseproject.cvbuilderbackendv2.repository.UserRepository;
import com.courseproject.cvbuilderbackendv2.service.impl.ResumeServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResumeServiceImplTest {

    @Mock
    private ResumeRepository resumeRepository;

    @InjectMocks
    private ResumeServiceImpl resumeService;

    @Test
    void givenValidResumeId_whenFindResumeByResumeId_thenReturnResume() {
        int resumeId = 1;
        Resume mockResume = new Resume();
        mockResume.setResumeId(resumeId);

        when(resumeRepository.findResumeByResumeId(resumeId)).thenReturn(mockResume);

        Resume result = resumeService.findResumeByResumeId(resumeId);

        assertNotNull(result);
        assertEquals(resumeId, result.getResumeId());
        verify(resumeRepository, times(1)).findResumeByResumeId(resumeId);
    }

    @Test
    void givenValidResumeData_whenUpdateResume_thenResumeIsUpdated() {
        int resumeId = 1;
        Resume mockResume = new Resume();
        JsonNode mockData = new ObjectMapper().createObjectNode().put("title", "Updated");

        when(resumeRepository.findById(resumeId)).thenReturn(Optional.of(mockResume));

        boolean result = resumeService.updateResume(resumeId, mockData);

        assertTrue(result);
        assertEquals(mockData, mockResume.getResumeData());
        verify(resumeRepository, times(1)).save(mockResume);
    }

    @Test
    void givenValidResumeId_whenDeleteResumeByResumeId_thenReturnTrue() {
        int resumeId = 1;
        when(resumeRepository.deleteResumeByResumeId(resumeId)).thenReturn(1);

        boolean result = resumeService.deleteResumeByResumeId(resumeId);

        assertTrue(result);
        verify(resumeRepository, times(1)).deleteResumeByResumeId(resumeId);
    }

    @Test
    void givenInvalidResumeId_whenDeleteResumeByResumeId_thenReturnFalse() {
        int resumeId = 999;
        when(resumeRepository.deleteResumeByResumeId(resumeId)).thenReturn(0);

        boolean result = resumeService.deleteResumeByResumeId(resumeId);

        assertFalse(result);
        verify(resumeRepository, times(1)).deleteResumeByResumeId(resumeId);
    }

}
