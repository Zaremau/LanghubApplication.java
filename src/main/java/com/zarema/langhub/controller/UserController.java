package com.zarema.langhub.controller;


import com.zarema.langhub.model.Users;
import com.zarema.langhub.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private UserService service;

    @Autowired
    public UserController(UserService service){
        this.service = service;
    }



    @PostMapping("/api/register")
    public ResponseEntity<Users> register(@Valid @RequestBody Users user){
        return new ResponseEntity<>(service.register(user), HttpStatus.OK);
    }
    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody Users user){
        return new ResponseEntity<>(service.verify(user), HttpStatus.OK);
    }

    @GetMapping("/api/account")
    public ResponseEntity<Users> getAccount(@RequestBody Users user){
        return new ResponseEntity<>(service.getAccount(user), HttpStatus.OK);
    }

    @PutMapping("/api/account")
    public ResponseEntity<Users> updateAccount(@RequestBody Users user){
        return new ResponseEntity<>(service.updateAccount(user), HttpStatus.OK);
    }
}
