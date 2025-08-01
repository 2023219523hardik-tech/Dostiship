package com.dostiship.controller;

import com.dostiship.dto.UserProfileResponse;
import com.dostiship.dto.UserUpdateRequest;
import com.dostiship.security.UserPrincipal;
import com.dostiship.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "User profile management APIs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get current user profile", description = "Get the profile of the currently authenticated user")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserProfileResponse> getCurrentUser(@AuthenticationPrincipal UserPrincipal currentUser) {
        UserProfileResponse userProfile = userService.getCurrentUser(currentUser.getId());
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user profile", description = "Update the profile of the currently authenticated user")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserProfileResponse> updateCurrentUser(@AuthenticationPrincipal UserPrincipal currentUser,
                                                                @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        UserProfileResponse updatedUser = userService.updateCurrentUser(currentUser.getId(), userUpdateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user profile by ID", description = "Get the public profile of a user by their ID")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserProfileResponse> getUserById(@PathVariable Long id) {
        UserProfileResponse userProfile = userService.getUserById(id);
        return ResponseEntity.ok(userProfile);
    }
}