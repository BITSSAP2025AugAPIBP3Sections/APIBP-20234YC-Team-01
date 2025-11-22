package com.example.GreenGrub.exception.base.security;

import com.example.GreenGrub.exception.ExceptionFactory;
import com.example.GreenGrub.exception.base.AppException;
import com.example.GreenGrub.exception.errorResponse.APIErrorResponse;
import com.example.GreenGrub.exception.errorResponse.ErrorDescription;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class CustomAuthenticationException implements AuthenticationEntryPoint {

    private final ExceptionFactory mExceptionFactory;
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ErrorDescription errorDesc =  mExceptionFactory.customerAuthenticationException();
        APIErrorResponse apiErrorResponse = new APIErrorResponse(
                errorDesc.code(),
                errorDesc.message(),
                errorDesc.suggestions(),
                request.getRequestURI(),
                request.getHeader("correlationId"),
                LocalDateTime.now()
        );

        response.setStatus(Integer.parseInt(errorDesc.code()));
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(apiErrorResponse));
    }
}
