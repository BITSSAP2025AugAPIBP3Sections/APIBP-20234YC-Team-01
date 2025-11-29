package com.example.GreenGrub.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO {
    private String senderId;
    private String receiverId;
    private String message;
    private String donationId;
    private Long timestamp;
}