package com.zarema.langhub.controller;

import com.zarema.langhub.model.Skill;
import com.zarema.langhub.model.StudySession;
import com.zarema.langhub.service.StudySessionsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class StudySessionsController {

    private StudySessionsService studySessionsService;

    @Autowired
    public StudySessionsController(StudySessionsService studySessionsService) {
        this.studySessionsService = studySessionsService;
    }
    @PostMapping("/api/user/study-session")
    public ResponseEntity<StudySession> addStudySession(String language, Skill skill, Date studySessionStart, Date studySessionEnd){
        return new ResponseEntity<>(studySessionsService. addStudySession(language, skill,studySessionStart, studySessionEnd), HttpStatus.OK);
    }

}
