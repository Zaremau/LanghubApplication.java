package com.zarema.langhub.repo;

import com.zarema.langhub.model.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StudySessionsRepo extends JpaRepository<StudySession, Integer> {
    @Query("""
            select s from StudySession s inner join Users u on s.user.id = u.id
            where u.id = :userId
            """)
    List<StudySession> findAllStudySessionsByUser(Integer userId);
}
