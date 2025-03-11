package com.zarema.langhub.controller;


import com.zarema.langhub.model.Users;
import com.zarema.langhub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    //change injection
    private UserService service;

    @Autowired
    public UserController(UserService service){
        this.service = service;
    }


    //change register form by adding email -> change database
    @PostMapping("/register")
    public Users register(@Valid @RequestBody Users user){
        return service.register(user);
    }
    @PostMapping("/login")
    public String login(@RequestBody Users user){
        return service.verify(user);
    }

    //logout
    @PostMapping("/logout")
    public String logout(){
        return "";
    }

    //change username

    //change password

    //add language
}
