package com.example.GreenGrub.filters;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.example.GreenGrub.exception.base.AppException;
import com.example.GreenGrub.exception.errorResponse.APIErrorResponse;
import com.example.GreenGrub.exception.errorResponse.ErrorDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.GreenGrub.repositories.CustomUserDetailsService;
import com.example.GreenGrub.services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{
    
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String token = authHeader.substring(7);
        final String username = jwtService.extractUsername(token);
         if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    } catch (ExpiredJwtException e) {
            ErrorDescription error = new ErrorDescription(
                    "401",
                    "JWT token expired",
                    List.of("Please login again", "Refresh your token if available")
            );

            APIErrorResponse apiError = new APIErrorResponse(
                    error.code(),
                    error.message(),
                    error.suggestions(),
                    request.getRequestURI(),
                    request.getHeader("correlationId"),
                    LocalDateTime.now()
            );

            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(apiError));
        }
    }
    
}
