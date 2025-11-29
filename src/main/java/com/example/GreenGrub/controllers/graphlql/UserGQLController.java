package com.example.GreenGrub.controllers.graphlql;

import com.example.GreenGrub.dto.*;
import com.example.GreenGrub.entity.User;
import com.example.GreenGrub.enumeration.UserRole;
import com.example.GreenGrub.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserGQLController {

    @Autowired
    private UserService userService;


    @QueryMapping
    public UserProfile getUserProfile(@Argument String userId) {
        return userService.getProfile(userId);
    }

    @QueryMapping
    public List<User> listAllUsers() {
        return userService.getAllUsers();
    }

    @QueryMapping
    public String ping() {
        return "pong";
    }

    @MutationMapping
    public UserResponse registerUser(@Argument UserRegistrationRequest request) {
        return userService.registerUser(request);
    }

    @MutationMapping
    public LoginResponse login(@Argument LoginRequest request) {
        return userService.login(request);
    }

    @MutationMapping
    public UserProfile updateUserProfile(@Argument String userId, @Argument UserProfile profile) {
        return userService.updateProfile(userId, profile);
    }

    @MutationMapping
    public OrganizationResponse manageOrganization(@Argument String userId, @Argument OrganizationRequest request) {
        return userService.manageOrganization(userId, request);
    }

    @MutationMapping
    public VerificationResponse verifyUser(@Argument String userId, @Argument UserRole roleType) {
        return userService.verifyUser(userId, roleType.name());
    }

    @MutationMapping
    public String logout(@Argument String token) {
        userService.logout(token);
        return "Logout Successful";
    }
}
