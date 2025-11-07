package com.example.GreenGrub.services;

import com.example.GreenGrub.dto.LoginRequest;
import com.example.GreenGrub.dto.LoginResponse;
import com.example.GreenGrub.dto.OrganizationRequest;
import com.example.GreenGrub.dto.OrganizationResponse;
import com.example.GreenGrub.dto.UserProfile;
import com.example.GreenGrub.dto.UserRegistrationRequest;
import com.example.GreenGrub.dto.UserResponse;
import com.example.GreenGrub.dto.VerificationResponse;
import com.example.GreenGrub.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {



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