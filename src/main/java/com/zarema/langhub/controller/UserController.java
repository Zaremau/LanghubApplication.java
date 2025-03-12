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



    @PostMapping("/register")
    public ResponseEntity<Users> register(@Valid @RequestBody Users user){
        return new ResponseEntity<>(service.register(user), HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users user){
        return new ResponseEntity<>(service.verify(user), HttpStatus.OK);
    }

    @GetMapping("/account")
    public ResponseEntity<Users> getAccount(HttpServletRequest request){
        Users user = service.getAccount(request);
        if(user.getUsername() != null)
            return new ResponseEntity<>(user, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/account")
    public ResponseEntity<Users> updateAccount(@RequestBody Users user){
        return new ResponseEntity<>(service.updateAccount(user), HttpStatus.OK);
    }

    //logout
    @PostMapping("/logout")
    public String logout(){
        return "";
    }
}
