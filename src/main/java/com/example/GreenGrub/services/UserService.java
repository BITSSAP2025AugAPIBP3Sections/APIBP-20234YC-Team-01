package com.example.GreenGrub.services;

import com.example.GreenGrub.dtos.LoginRequest;
import com.example.GreenGrub.dtos.LoginResponse;
import com.example.GreenGrub.dtos.OrganizationRequest;
import com.example.GreenGrub.dtos.OrganizationResponse;
import com.example.GreenGrub.dtos.UserProfile;
import com.example.GreenGrub.dtos.UserRegistrationRequest;
import com.example.GreenGrub.dtos.UserResponse;
import com.example.GreenGrub.dtos.VerificationResponse;
import com.example.GreenGrub.entity.User;
import com.example.GreenGrub.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public UserResponse registerUser(UserRegistrationRequest request) {
        // TODO Auto-generated method stub
        return null;
        
    }

    public LoginResponse login(LoginRequest request) {
        // TODO Auto-generated method stub
        return null;
        
    }

    public UserProfile getProfile(Long userId) {
        // TODO Auto-generated method stub
        return null;
        
    }

    public UserProfile updateProfile(Long userId, UserProfile updatedProfile) {
        // TODO Auto-generated method stub
        return null;
        
    }

    public OrganizationResponse manageOrganization(Long userId, OrganizationRequest request) {
        // TODO Auto-generated method stub
        return null;
        
    }

    public VerificationResponse verifyUser(Long userId, String roleType) {
        // TODO Auto-generated method stub
        return null;
    }

    public void logout(String token) {
        // TODO Auto-generated method stub
        
    }
}