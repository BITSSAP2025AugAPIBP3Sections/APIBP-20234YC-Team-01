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
import com.example.GreenGrub.repositories.UserRepository;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserResponse registerUser(UserRegistrationRequest request) {
        
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .phoneNumber(request.getPhoneNumber())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User createdUser = userRepository.save(user);
        return UserResponse.builder()
            .email(createdUser.getEmail())
            .name(createdUser.getName())
            .role(createdUser.getRole())
            .userId(createdUser.getId())
            .build();
        
    }

    public LoginResponse login(LoginRequest request) {
       Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        return LoginResponse.builder()
            .token(token)
            .build();        
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