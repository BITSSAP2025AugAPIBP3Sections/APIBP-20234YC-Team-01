package com.example.GreenGrub.exception.base;

import com.example.GreenGrub.exception.errorResponse.ErrorDescription;

public class NotFoundException extends AppException {
    public NotFoundException(ErrorDescription error) {
        super(error);
    }
}
