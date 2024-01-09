package com.jwt.example.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.example.models.User;
import com.jwt.example.services.UserService;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")    
    public List<User> getUser(){
        System.out.println("getting users");
        return this.userService.getUsers();
    }   

    @GetMapping("/current-user")
    public String getLoggedInUser(Principal principal){        // Principal is an interface that return the current username and the roles 
        System.out.println("getting current user");
        return principal.getName();
    }
}
