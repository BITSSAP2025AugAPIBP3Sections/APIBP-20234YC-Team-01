package com.example.GreenGrub.controllers;

import com.example.GreenGrub.dto.*;
import com.example.GreenGrub.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "Endpoints for user registration, login, verification and profile management")
public class UserController {

    @Autowired
    private UserService userService;

    // ---------------------------------------------
    // 1️⃣ User Registration
    // ---------------------------------------------
    @Operation(summary = "Register new user", description = "Registers a new user (individual or organization)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRegistrationRequest request) {
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ---------------------------------------------
    // 2️⃣ User Login
    // ---------------------------------------------
    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------
    // 3️⃣ Profile Management
    // ---------------------------------------------
    @Operation(summary = "Get user profile", description = "Endpoints for manage and handle the details of a user,Access by [ADMIN,DONOR,RECIPIENT,DELIVERY]")
    @ApiResponse(responseCode = "200", description = "Profile retrieved successfully")
    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfile> getProfile(@PathVariable Long userId) {
        UserProfile profile = userService.getProfile(userId);
        return ResponseEntity.ok(profile);
    }

    @Operation(summary = "Update user profile", description = "Updates profile details of a user")
    @ApiResponse(responseCode = "200", description = "Profile updated successfully")
    @PutMapping("/{userId}/profile")
    public ResponseEntity<UserProfile> updateProfile(@PathVariable Long userId, @RequestBody UserProfile updatedProfile) {
        UserProfile profile = userService.updateProfile(userId, updatedProfile);
        return ResponseEntity.ok(profile);
    }

    // ---------------------------------------------
    // 4️⃣ Manage Organization Account
    // ---------------------------------------------
    @Operation(summary = "Manage organization account", description = "Creates or updates organization details for a user")
    @ApiResponse(responseCode = "200", description = "Organization details updated successfully")
    @PutMapping("/{userId}/organization")
    public ResponseEntity<OrganizationResponse> manageOrganization(
            @PathVariable Long userId,
            @RequestBody OrganizationRequest request) {
        OrganizationResponse response = userService.manageOrganization(userId, request);
        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------
    // 5️⃣ Verify Donor / Recipient
    // ---------------------------------------------
    @Operation(summary = "Verify donor or recipient", description = "Verifies the authenticity of a user as a donor or recipient")
    @ApiResponse(responseCode = "200", description = "User verification completed")
    @PostMapping("/{userId}/verify")
    public ResponseEntity<VerificationResponse> verifyUser(
            @PathVariable Long userId,
            @RequestParam String roleType // DONOR / RECIPIENT
    ) {
        VerificationResponse response = userService.verifyUser(userId, roleType);
        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------
    // 6️⃣ Logout
    // ---------------------------------------------
    @Operation(summary = "Logout user", description = "Logs out the current user and invalidates the token")
    @ApiResponse(responseCode = "200", description = "Logout successful")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        userService.logout(token);
        return ResponseEntity.ok("Logout successful");
    }
}
