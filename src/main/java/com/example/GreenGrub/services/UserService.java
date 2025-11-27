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
import java.util.List;
import java.util.Optional;

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

    public UserProfile getProfile(String userId) {
        final var user = userRepository.findById(userId);
        if(!user.isPresent()){
            return UserProfile.builder()
                .name("User does not exist")
                .userId(userId)
                .build();
        }
        UserProfile profile = new UserProfile();
        User u = user.get();
        profile.setUserId(userId);
        Optional.of(u.getName()).ifPresent(item -> profile.setName(item));
        Optional.of(u.getPhoneNumber()).ifPresent(item -> profile.setPhoneNumber(item));
        userRepository.save(u);
        return profile;
        
    }

    public UserProfile updateProfile(String userId, UserProfile updatedProfile) {
        final var user = userRepository.findById(userId);
        if(!user.isPresent()){
            return UserProfile.builder()
                .name("User does not exist")
                .userId(userId)
                .build();
        }
        User u = user.get();
        Optional.of(updatedProfile.getName()).ifPresent(item -> u.setName(item));
        Optional.of(updatedProfile.getPhoneNumber()).ifPresent(item -> u.setPhoneNumber(item));
        userRepository.save(u);
        return updatedProfile;
    }

    public OrganizationResponse manageOrganization(String userId, OrganizationRequest request) {
        
        return OrganizationResponse.builder()
                .orgName(request.getOrgName())
                .verifiedStatus("verified")
                .build()
        ;
        
    }

    public VerificationResponse verifyUser(String userId, String roleType) {
        VerificationResponse response = new VerificationResponse();
        final var userOptional =userRepository.findById(userId);
        if (userOptional.isPresent()) {
            Optional
            .ofNullable(roleType)
            .ifPresentOrElse( role -> {
                if(userOptional.get().getRole().name().equalsIgnoreCase(roleType)){

                    response.setMessage("User with ID : "+userId+" is a valid "+roleType);
                    response.setUserId(userId);
                    response.setVerified(true);
                } else{
                    response.setMessage("User with ID : "+userId+" is a valid user");
                    response.setUserId(userId);
                    response.setVerified(true);
                }
            }, () -> {
                response.setMessage("User with ID : "+userId+" is a valid user");
                response.setUserId(userId);
                response.setVerified(true);
            });
            
            
        }else {
            response.setMessage("User with ID : "+userId+" is a invalid user");
            response.setUserId(userId);
            response.setVerified(false);
        }
        return response;
    }

    public void logout(String token) {
        // TODO Auto-generated method stub
        
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}