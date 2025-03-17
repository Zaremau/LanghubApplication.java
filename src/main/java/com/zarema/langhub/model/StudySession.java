package com.zarema.langhub.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StudySession {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq")
    @SequenceGenerator(name = "token_seq", sequenceName = "token_seq", allocationSize = 1)
    private int id;
    private String language;

    @Enumerated(EnumType.STRING)
    private Skill skill;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date studyTimeStart;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date studyTimeEnd;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
}
