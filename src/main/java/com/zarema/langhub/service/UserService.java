package com.zarema.langhub.service;


import com.zarema.langhub.filters.JwtFilter;
import com.zarema.langhub.model.Users;
import com.zarema.langhub.repo.UserRepo;
import com.zarema.langhub.token.Token;
import com.zarema.langhub.token.TokenRepo;
import com.zarema.langhub.token.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepo repo;
    private TokenRepo tokenRepo;
    AuthenticationManager authManager;
    private JWTService jwtService;

    @Autowired
    public UserService(UserRepo repo, TokenRepo tokenRepo, AuthenticationManager authManager,
                       JWTService jwtService, JwtFilter jwtFilter){
        this.repo = repo;
        this.tokenRepo = tokenRepo;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(Users user){
        user.setPassword(encoder.encode(user.getPassword()));
        var jwtToken = jwtService.generateToken(user.getUsername());
        repo.save(user);
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
        Users user1 = repo.findById(user.getId());
        user1.setUsername(user.getUsername());
        user1.setEmail(user.getEmail());
        user1.setPassword(encoder.encode(user.getPassword()));
        user1.setLanguage(user.getLanguage());
        return repo.save(user1);
    }

    public Users getAccount(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);
            return repo.findByUsername(username);
        }
        return new Users();
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
}
