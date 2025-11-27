package com.example.GreenGrub.filters;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.example.GreenGrub.exception.base.AppException;
import com.example.GreenGrub.exception.errorResponse.APIErrorResponse;
import com.example.GreenGrub.exception.errorResponse.ErrorDescription;
import com.example.GreenGrub.repositories.CustomUserDetailsService;
import com.example.GreenGrub.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException {

        try {
            final String authHeader = request.getHeader("Authorization");

            // No token → just continue the chain (public endpoints will be allowed by SecurityConfig)
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String token = authHeader.substring(7);
            final String username = jwtService.extractUsername(token);

            if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                        );

                    authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            // Build an ErrorDescription for expired token
            ErrorDescription error = new ErrorDescription(
                "401",
                "JWT token expired",
                List.of("Please login again", "Refresh your token if available")
            );
            writeError(response, request, error);

        }
    }

    private void writeError(HttpServletResponse response,
                            HttpServletRequest request,
                            ErrorDescription error) throws IOException {

        APIErrorResponse apiError = new APIErrorResponse(
            error.code(),
            error.message(),
            error.suggestions(),
            request.getRequestURI(),
            request.getHeader("correlationId"),
            LocalDateTime.now()
        );

        // Your ErrorDescription.code() is already a string HTTP status (e.g. "401")
        response.setStatus(Integer.parseInt(error.code()));
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(apiError));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/v1/users/login")
            || path.startsWith("/api/v1/users/register")
            || path.startsWith("/v3/api-docs")
            || path.startsWith("/swagger-ui");
    }
}
