package com.zarema.langhub.service;


import com.zarema.langhub.filters.JwtFilter;
import com.zarema.langhub.model.Users;
import com.zarema.langhub.repo.UserRepo;
import com.zarema.langhub.token.Token;
import com.zarema.langhub.token.TokenRepo;
import com.zarema.langhub.token.TokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final TokenRepo tokenRepo;
    AuthenticationManager authManager;
    private final JWTService jwtService;

    @Autowired
    public UserService(UserRepo repo, TokenRepo tokenRepo, AuthenticationManager authManager,
                       JWTService jwtService, JwtFilter jwtFilter){
        this.userRepo = repo;
        this.tokenRepo = tokenRepo;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }


    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(Users user){
        user.setPassword(encoder.encode(user.getPassword()));
        var jwtToken = jwtService.generateToken(user.getUsername());
        userRepo.save(user);
        saveUserToken(user, jwtToken);
        return user;
    }

    public String verify(Users user) {
        String jwtToken;
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if(authentication.isAuthenticated()) {
            jwtToken = jwtService.generateToken(user.getUsername());
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            return jwtToken;
        }
        return "Fail";
    }

    public Users updateAccount(Users user) {
        Users user1 = userRepo.findById(user.getId());
        user1.setUsername(user.getUsername());
        user1.setEmail(user.getEmail());
        user1.setPassword(encoder.encode(user.getPassword()));
        user1.setLanguage(user.getLanguage());
        return userRepo.save(user1);
    }

    public Users getAccount(Users user) {
      return userRepo.findByUsername(user.getUsername());
    }

    private void revokeAllUserTokens(Users user){
        var validUserTokens = tokenRepo.findAllValidTokensByUser(user.getId());
        if(validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepo.saveAll(validUserTokens);
    }

    private void saveUserToken(Users user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepo.save(token);
    }

    public Optional<Users> findPrinciple(Principal principal) {
        return Optional.ofNullable(userRepo.findByUsername(principal.getName()));
    }
}
