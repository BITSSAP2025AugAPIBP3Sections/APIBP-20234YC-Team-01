package com.example.GreenGrub.exception.error;

public enum ErrorCode {
    @Error(message = "validation.userNotFound", suggestion = {
            "validation.userNotFound.suggestion.one","validation.userNotFound.suggestion.two",
            "validation.userNotFound.suggestion.three","validation.userNotFound.suggestion.four",
            "validation.userNotFound.suggestion.five"
    })
    USER_NOT_FOUND("404"),
    @Error(message = "validation.unauthorizedUser", suggestion = {
            "validation.unauthorizedUser.suggestion.one","validation.unauthorizedUser.suggestion.two",
            "validation.unauthorizedUser.suggestion.three","validation.unauthorizedUser.suggestion.four"
    })
    USER_UNAUTHORIZED("401"),
    @Error(message = "validation.invalidFields", suggestion = {
            "validation.invalidFields.suggestion.one","validation.invalidFields.suggestion.two",
            "validation.invalidFields.suggestion.three","validation.invalidFields.suggestion.four",
            "validation.invalidFields.suggestion.five"
    })
    VALIDATION_ERROR("400"),
    @Error(message = "error.internalServerError", suggestion = {
            "error.internalServerError.suggestion.one","error.internalServerError.suggestion.two",
            "error.internalServerError.suggestion.three","error.internalServerError.suggestion.four",
            "error.internalServerError.suggestion.five"
    })
    INTERNAL_SERVER_ERROR("500"),

    @Error(message = "error.customerAuthError", suggestion = {
            "error.customerAuthError.suggestion.one", "error.customerAuthError.suggestion.two"
    })
    CUSTOMER_AUTHENTICATION_ERROR("401"),

    @Error(message = "error.userUnauthorize",suggestion = {"error.userUnauthorize.suggestion.one"})
    CUSTOMER_AUTHORIZATION_ERROR("403"),
    ;

    private final String code; // this is the error code eg: 400
    ErrorCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }

    public String getErrorMessageTag() {
        return this.getAnnotation(this.name()).message();
    }

    public String[] getErrorSuggestionTag() {
        return this.getAnnotation(this.name()).suggestion();
    }

    Error getAnnotation(String errorCodeName) {
        try {
            return ErrorCode.class.getField(errorCodeName).getAnnotation(Error.class);
        } catch (NoSuchFieldException e) {
            throw new AssertionError("This shouldn't be happening",e);
        }

    }
}
