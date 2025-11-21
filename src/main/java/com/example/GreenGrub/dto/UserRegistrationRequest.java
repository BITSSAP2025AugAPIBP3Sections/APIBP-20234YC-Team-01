package com.example.GreenGrub.dto;
import com.example.GreenGrub.enumeration.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    private String name;
    private String email;
    private String password;
    private UserRole role; 
    private String phoneNumber;
}
