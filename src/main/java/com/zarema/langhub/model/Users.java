package com.zarema.langhub.model;

import com.zarema.langhub.token.Token;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Users {
    @Id
    private int id;
    private String username;
    @Email
    private String email;
    private String password;
    private String language;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;
}
