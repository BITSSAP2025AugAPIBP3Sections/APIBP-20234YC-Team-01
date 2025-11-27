package com.example.GreenGrub.exception.base;

import com.example.GreenGrub.exception.errorResponse.ErrorDescription;

public class AppException extends RuntimeException {

    private final ErrorDescription error;

    public AppException(ErrorDescription error) {
        // Use the message from ErrorDescription
        super(error.message());
        this.error = error;
    }

    public ErrorDescription getErrorObject() {
        return error;
    }
}
