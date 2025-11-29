package com.example.GreenGrub.services;

import com.example.GreenGrub.dto.*;
import com.example.GreenGrub.entity.User;
import com.example.GreenGrub.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_shouldSaveUserAndReturnResponse() {
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .role(null)
                .phoneNumber("1234567890")
                .build();
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password("encoded")
                .role(request.getRole())
                .phoneNumber(request.getPhoneNumber())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        User savedUser = User.builder()
                .id("userId")
                .name(request.getName())
                .email(request.getEmail())
                .role(request.getRole())
                .build();
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.registerUser(request);
        assertEquals("userId", response.getUserId());
        assertEquals("Test User", response.getName());
        assertEquals("test@example.com", response.getEmail());
    }

    @Test
    void login_shouldAuthenticateAndReturnToken() {
        LoginRequest request = LoginRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("jwt-token");

        LoginResponse response = userService.login(request);
        assertEquals("jwt-token", response.getToken());
    }

    @Test
    void getProfile_shouldReturnProfileIfUserExists() {
        User user = User.builder().id("userId").name("Test User").phoneNumber("1234567890").build();
        when(userRepository.findById("userId")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserProfile profile = userService.getProfile("userId");
        assertEquals("userId", profile.getUserId());
        assertEquals("Test User", profile.getName());
        assertEquals("1234567890", profile.getPhoneNumber());
    }

    @Test
    void getProfile_shouldReturnDefaultProfileIfUserNotExists() {
        when(userRepository.findById("userId")).thenReturn(Optional.empty());
        UserProfile profile = userService.getProfile("userId");
        assertEquals("userId", profile.getUserId());
        assertEquals("User does not exist", profile.getName());
    }

    @Test
    void updateProfile_shouldUpdateAndReturnProfileIfUserExists() {
        User user = User.builder().id("userId").name("Old Name").phoneNumber("0000000000").build();
        UserProfile updatedProfile = UserProfile.builder().userId("userId").name("New Name").phoneNumber("9999999999").build();
        when(userRepository.findById("userId")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserProfile result = userService.updateProfile("userId", updatedProfile);
        assertEquals("New Name", result.getName());
        assertEquals("9999999999", result.getPhoneNumber());
    }

    @Test
    void updateProfile_shouldReturnDefaultProfileIfUserNotExists() {
        UserProfile updatedProfile = UserProfile.builder().userId("userId").name("New Name").phoneNumber("9999999999").build();
        when(userRepository.findById("userId")).thenReturn(Optional.empty());
        UserProfile result = userService.updateProfile("userId", updatedProfile);
        assertEquals("User does not exist", result.getName());
        assertEquals("userId", result.getUserId());
    }

    @Test
    void manageOrganization_shouldReturnVerifiedResponse() {
        OrganizationRequest request = OrganizationRequest.builder().orgName("OrgName").build();
        OrganizationResponse response = userService.manageOrganization("userId", request);
        assertEquals("OrgName", response.getOrgName());
        assertEquals("verified", response.getVerifiedStatus());
    }

    @Test
    void verifyUser_shouldReturnVerifiedIfUserExistsAndRoleMatches() {
        com.example.GreenGrub.enumeration.UserRole adminRole = com.example.GreenGrub.enumeration.UserRole.ADMIN;
        User user = User.builder().id("userId").role(adminRole).build();
        when(userRepository.findById("userId")).thenReturn(Optional.of(user));
        VerificationResponse response = userService.verifyUser("userId", "ADMIN");
        assertTrue(response.isVerified());
        assertEquals("User with ID : userId is a valid ADMIN", response.getMessage());
    }

    @Test
    void verifyUser_shouldReturnVerifiedIfUserExistsAndRoleNotMatches() {
        com.example.GreenGrub.enumeration.UserRole donorRole = com.example.GreenGrub.enumeration.UserRole.DONOR;
        User user = User.builder().id("userId").role(donorRole).build();
        when(userRepository.findById("userId")).thenReturn(Optional.of(user));
        VerificationResponse response = userService.verifyUser("userId", "ADMIN");
        assertTrue(response.isVerified());
        assertEquals("User with ID : userId is a valid user", response.getMessage());
    }

    @Test
    void verifyUser_shouldReturnNotVerifiedIfUserNotExists() {
        when(userRepository.findById("userId")).thenReturn(Optional.empty());
        VerificationResponse response = userService.verifyUser("userId", "ADMIN");
        assertFalse(response.isVerified());
        assertEquals("User with ID : userId is a invalid user", response.getMessage());
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        List<User> users = Arrays.asList(
                User.builder().id("1").name("A").build(),
                User.builder().id("2").name("B").build()
        );
        when(userRepository.findAll()).thenReturn(users);
        List<User> result = userService.getAllUsers();
        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getName());
        assertEquals("B", result.get(1).getName());
    }

    @Test
    void logout_shouldNotThrowException() {
        assertDoesNotThrow(() -> userService.logout("token"));
    }
}
