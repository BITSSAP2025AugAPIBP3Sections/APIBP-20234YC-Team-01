package com.example.GreenGrub.exception.base;

import com.example.GreenGrub.exception.errorResponse.ErrorDescription;

public class ServiceException extends AppException {
    public ServiceException(ErrorDescription error) {
        super(error);
    }
}
