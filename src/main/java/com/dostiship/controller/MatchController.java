package com.dostiship.controller;

import com.dostiship.dto.UserProfileResponse;
import com.dostiship.security.UserPrincipal;
import com.dostiship.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
@Tag(name = "Matching Engine", description = "User matching and recommendation APIs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MatchController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Get matching users", description = "Get a paginated list of recommended user profiles based on shared interests")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Page<UserProfileResponse>> getMatchingUsers(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<UserProfileResponse> matchingUsers = userService.getMatchingUsers(currentUser.getId(), page, size);
        return ResponseEntity.ok(matchingUsers);
    }
}