package com.courseproject.cvbuilderbackendv2.service.impl;

import com.courseproject.cvbuilderbackendv2.entity.*;
import com.courseproject.cvbuilderbackendv2.repository.*;
import com.courseproject.cvbuilderbackendv2.service.ApplicationService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final VacancyRepository vacancyRepository;
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    @org.springframework.beans.factory.annotation.Value("${spring.mail.username}")
    private String fromEmail;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository,
                                  VacancyRepository vacancyRepository,
                                  ResumeRepository resumeRepository,
                                  UserRepository userRepository,
                                  JavaMailSender mailSender) {
        this.applicationRepository = applicationRepository;
        this.vacancyRepository = vacancyRepository;
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    @Override
    public Application apply(Long vacancyId, int resumeId, String userName, String coverLetter) {
        User applicant = userRepository.findByUserName(userName);
        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        if (resume.getUser().getUserId() != applicant.getUserId()) {
            throw new RuntimeException("You can only apply with your own resume");
        }

        if (!resume.isPublic()) {
            throw new RuntimeException("You can only apply with a public resume");
        }

        if (!vacancy.isActive()) {
            throw new RuntimeException("You can only apply for active vacancies");
        }

        if (applicationRepository.existsByVacancy_IdAndResume_ResumeId(vacancyId, resumeId)) {
            throw new RuntimeException("Already applied with this resume");
        }

        Application application = new Application(vacancy, resume, applicant, coverLetter);
        return applicationRepository.save(application);
    }

    @Override
    public List<Application> getMyApplications(String userName) {
        User user = userRepository.findByUserName(userName);
        return applicationRepository.findByApplicant_UserId(user.getUserId());
    }

    @Override
    public List<Application> getVacancyApplications(Long vacancyId) {
        return applicationRepository.findByVacancy_Id(vacancyId);
    }

    @Override
    public Application acceptApplication(Long applicationId, String employerName) {
        Application application = applicationRepository.findById(applicationId).orElseThrow();
        application.setStatus(ApplicationStatus.ACCEPTED);
        applicationRepository.save(application);

        User applicant = application.getApplicant();
        if (applicant.getUserEmail() != null) {
            sendEmail(applicant.getUserEmail(),
                    "Ваш отклик принят!",
                    "Здравствуйте, " + applicant.getUserName() + "!\n\n"
                            + "Ваш отклик на вакансию \"" + application.getVacancy().getTitle() + "\" был принят.\n"
                            + "Работодатель: " + employerName + "\n\n"
                            + "Свяжитесь с работодателем для дальнейших инструкций.");
        }

        return application;
    }

    @Override
    public Application rejectApplication(Long applicationId, String employerName) {
        Application application = applicationRepository.findById(applicationId).orElseThrow();
        application.setStatus(ApplicationStatus.REJECTED);
        applicationRepository.save(application);

        User applicant = application.getApplicant();
        if (applicant.getUserEmail() != null) {
            sendEmail(applicant.getUserEmail(),
                    "Ваш отклик отклонён",
                    "Здравствуйте, " + applicant.getUserName() + "!\n\n"
                            + "Ваш отклик на вакансию \"" + application.getVacancy().getTitle() + "\" был отклонён.\n"
                            + "Работодатель: " + employerName + "\n\n"
                            + "Не расстраивайтесь, попробуйте другие вакансии!");
        }

        return application;
    }

    private void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Ошибка отправки email: " + e.getMessage());
        }
        System.out.println("=== EMAIL TO " + to + ": " + subject + " ===");
    }
}