package com.zarema.langhub.service;

import com.zarema.langhub.model.Skill;
import com.zarema.langhub.model.StudySession;
import com.zarema.langhub.model.Users;
import com.zarema.langhub.repo.StudySessionsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class StudySessionsService {
    private StudySessionsRepo studySessionsRepo;

    @Autowired
    public StudySessionsService(StudySessionsRepo studySessionsRepo) {
        this.studySessionsRepo = studySessionsRepo;
    }

    public StudySession addStudySession(String language, Skill skill, Date studySessionStart, Date studySessionEnd) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = (Users) authentication.getDetails();

        StudySession studySession = new StudySession();
        studySession.setUser(user);
        studySession.setLanguage(language);
        studySession.setStudyTimeStart(studySessionStart);
        studySession.setStudyTimeEnd(studySessionEnd);
       return studySessionsRepo.save(studySession);
    }
}
