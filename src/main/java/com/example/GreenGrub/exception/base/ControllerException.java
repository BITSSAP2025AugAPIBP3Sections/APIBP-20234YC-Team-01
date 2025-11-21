package com.example.GreenGrub.exception.base;

import com.example.GreenGrub.exception.errorResponse.ErrorDescription;

public class ControllerException extends AppException {
    public ControllerException(ErrorDescription errorDescription) {
        super(errorDescription);
    }
}
