package com.example.GreenGrub.services;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    
    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        userDetails = User.builder()
                .username("test@example.com")
                .password("password")
                .authorities(authorities)
                .build();
    }

    @Test
    void generateToken_shouldReturnNonNullToken() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_shouldReturnCorrectUsername() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);
        assertEquals("test@example.com", username);
    }

    @Test
    void extractExpiration_shouldReturnFutureDate() {
        String token = jwtService.generateToken(userDetails);
        Date expiration = jwtService.extractExpiration(token);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void extractClaim_shouldExtractSubject() {
        String token = jwtService.generateToken(userDetails);
        String subject = jwtService.extractClaim(token, Claims::getSubject);
        assertEquals("test@example.com", subject);
    }

    @Test
    void isTokenValid_shouldReturnTrueForValidToken() {
        String token = jwtService.generateToken(userDetails);
        boolean isValid = jwtService.isTokenValid(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    void isTokenValid_shouldReturnFalseForDifferentUser() {
        String token = jwtService.generateToken(userDetails);
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails differentUser = User.builder()
                .username("different@example.com")
                .password("password")
                .authorities(authorities)
                .build();
        
        boolean isValid = jwtService.isTokenValid(token, differentUser);
        assertFalse(isValid);
    }

    @Test
    void generateToken_shouldIncludeRoleClaim() {
        String token = jwtService.generateToken(userDetails);
        Object role = jwtService.extractClaim(token, claims -> claims.get("role"));
        assertNotNull(role);
    }

    @Test
    void generateToken_shouldHaveIssuedAtClaim() {
        String token = jwtService.generateToken(userDetails);
        Date issuedAt = jwtService.extractClaim(token, Claims::getIssuedAt);
        assertNotNull(issuedAt);
        assertTrue(issuedAt.before(new Date(System.currentTimeMillis() + 1000)));
    }
}
