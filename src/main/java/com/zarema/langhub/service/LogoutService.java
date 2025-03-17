package com.zarema.langhub.service;

import com.zarema.langhub.token.TokenRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private TokenRepo tokenRepo;

    @Autowired
    public LogoutService(TokenRepo tokenRepo){
        this.tokenRepo = tokenRepo;
    }
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            var storedToken = tokenRepo.findByToken(token)
                    .orElse(null);
            if(storedToken != null){
                storedToken.setRevoked(true);
                storedToken.setExpired(true);
                tokenRepo.save(storedToken);
            }
        }
        else return;
    }
}
