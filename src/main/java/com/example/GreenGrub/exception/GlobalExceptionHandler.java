package com.example.GreenGrub.exception;

import com.example.GreenGrub.exception.base.AppException;
import com.example.GreenGrub.exception.errorResponse.APIErrorResponse;
import com.example.GreenGrub.exception.errorResponse.ErrorDescription;
import com.example.GreenGrub.exception.errorResponse.FieldErrorList;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<APIErrorResponse> handle(NotFoundException e, HttpServletRequest request) {
//        ErrorDescription desc = e.getErrorObject();
//        APIErrorResponse response = new APIErrorResponse(
//                desc.code(),
//                desc.message(),
//                desc.suggestions(),
//                request.getContextPath(),
//                request.getHeader("correlationId"),
//                LocalDateTime.now()
//        );
//        return ResponseEntity.status(Integer.parseInt(response.code())).body(response);
//    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<APIErrorResponse> handle(AppException e, HttpServletRequest request) {
        ErrorDescription desc = e.getErrorObject();
        APIErrorResponse response = new APIErrorResponse(
                desc.code(),
                desc.message(),
                desc.suggestions(),
                request.getContextPath(),
                request.getHeader("correlationId"),
                LocalDateTime.now()
        );
        return ResponseEntity.status(Integer.parseInt(response.code())).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FieldErrorList> handle(MethodArgumentNotValidException e) {
        var errors = new HashMap<String,String>();
        e.getBindingResult()
                .getFieldErrors().forEach(error -> {
                    var fieldName = ((FieldError)error).getField();
                    var errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FieldErrorList(errors));
    }
//
//    private HttpStatus mapToHttpStatus(ErrorCode code) {
//        return switch (code) {
//            case USER_NOT_FOUND -> HttpStatus.NOT_FOUND;
//            case VALIDATION_ERROR -> HttpStatus.BAD_REQUEST;
//            case USER_UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
//            default -> HttpStatus.INTERNAL_SERVER_ERROR;
//        };
//    }
}
