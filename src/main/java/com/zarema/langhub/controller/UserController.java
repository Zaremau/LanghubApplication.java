package com.zarema.langhub.controller;


import com.zarema.langhub.model.Users;
import com.zarema.langhub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserService service;

    @Autowired
    public UserController(UserService service){
        this.service = service;
    }



    @PostMapping("/register")
    public ResponseEntity<Users> register(@Valid @RequestBody Users user){
        return new ResponseEntity<>(service.register(user), HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users user){
        return new ResponseEntity<>(service.verify(user), HttpStatus.OK);
    }

    //logout
    @PostMapping("/logout")
    public String logout(){
        return "";
    }

    //change username
    //@PutMapping("/user/")
    //change password

    //add language
}
