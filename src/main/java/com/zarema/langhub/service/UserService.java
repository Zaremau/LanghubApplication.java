package com.zarema.langhub.service;


import com.zarema.langhub.model.Users;
import com.zarema.langhub.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepo repo;
    AuthenticationManager authManager;
    private JWTService jwtService;

    @Autowired
    public UserService(UserRepo repo, AuthenticationManager authManager, JWTService jwtService){
        this.repo = repo;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(Users user){
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public String verify(Users user) {
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if(authentication.isAuthenticated())
            return jwtService.generateToken(user.getUsername());
        return "Fail";
    }
}
