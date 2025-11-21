package com.example.GreenGrub.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransactionReviewDTO {

    @NotNull
    private String transactionId;

    @Min(1)
    @Max(5)
    private Integer rating;

    private String review;

    public @NotNull String getTransactionId()
    {
        return transactionId;
    }

    public @Min(1) @Max(5) Integer getRating()
    {
        return rating;
    }

    public String getReview()
    {
        return review;
    }

}
