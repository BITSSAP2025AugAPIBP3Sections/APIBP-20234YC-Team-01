package com.example.GreenGrub.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private String senderId;
    private String receiverId;
    private String message;
    private String donationId;
    private Long timestamp;
}