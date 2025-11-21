package com.example.GreenGrub.dto;
import com.example.GreenGrub.enumeration.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// -----------------------------
// ✅ User Response
// -----------------------------
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String userId;
    private String name;
    private String email;
    private UserRole role;
}