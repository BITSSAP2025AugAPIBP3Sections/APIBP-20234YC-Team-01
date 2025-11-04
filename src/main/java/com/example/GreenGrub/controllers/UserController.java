package com.example.GreenGrub.controllers;

import com.example.GreenGrub.entity.User;
import com.example.GreenGrub.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public String saveUser(@RequestBody User user){
        try {
            userService.saveUser(user);
            return "Success!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
