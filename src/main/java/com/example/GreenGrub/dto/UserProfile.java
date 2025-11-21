package com.example.GreenGrub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    private Long userId;
    private String name;
    private static String contact;
    private String address;
    private String profileImage;
    public static String getContact(Long userId)
    {
        return contact;
    }
}
