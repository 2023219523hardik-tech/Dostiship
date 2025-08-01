package com.dostiship.service;

import com.dostiship.dto.*;
import com.dostiship.exception.BadRequestException;
import com.dostiship.exception.ResourceNotFoundException;
import com.dostiship.model.User;
import com.dostiship.repository.UserRepository;
import com.dostiship.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public AuthResponse registerUser(UserRegistrationRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use!");
        }

        // Create new user
        User user = new User();
        user.setFullName(signUpRequest.getFullName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        User result = userRepository.save(user);

        // Authenticate user and generate token
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signUpRequest.getEmail(),
                        signUpRequest.getPassword()
                )
        );

        String jwt = tokenProvider.generateToken(authentication);

        return new AuthResponse(jwt, result.getId(), result.getEmail(), result.getFullName());
    }

    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", loginRequest.getEmail()));

        return new AuthResponse(jwt, user.getId(), user.getEmail(), user.getFullName());
    }

    public UserProfileResponse getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return mapToUserProfileResponse(user);
    }

    public UserProfileResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Return public profile (without sensitive information like email for other users)
        UserProfileResponse response = mapToUserProfileResponse(user);
        // For public profiles, we might want to hide email
        // response.setEmail(null);
        return response;
    }

    public UserProfileResponse updateCurrentUser(Long userId, UserUpdateRequest updateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (updateRequest.getFullName() != null) {
            user.setFullName(updateRequest.getFullName());
        }
        if (updateRequest.getProfileImageUrl() != null) {
            user.setProfileImageUrl(updateRequest.getProfileImageUrl());
        }
        if (updateRequest.getBio() != null) {
            user.setBio(updateRequest.getBio());
        }
        if (updateRequest.getInterests() != null) {
            user.setInterests(updateRequest.getInterests());
        }

        User updatedUser = userRepository.save(user);
        return mapToUserProfileResponse(updatedUser);
    }

    public Page<UserProfileResponse> getMatchingUsers(Long currentUserId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUserId));

        if (currentUser.getInterests().isEmpty()) {
            // If no interests, return all users except current user
            Page<User> users = userRepository.findAllExceptCurrentUser(currentUserId, pageable);
            return users.map(this::mapToUserProfileResponse);
        }

        // Find users with shared interests (this is a simplified version)
        // In a real implementation, you might want to implement a more sophisticated matching algorithm
        List<User> matchingUsers = userRepository.findUsersByInterests(currentUser.getInterests(), currentUserId);
        
        // Convert to Page manually for simplicity (in production, you'd want proper pagination)
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), matchingUsers.size());
        List<User> pageContent = matchingUsers.subList(start, end);
        
        return new org.springframework.data.domain.PageImpl<>(
                pageContent.stream().map(this::mapToUserProfileResponse).collect(Collectors.toList()),
                pageable,
                matchingUsers.size()
        );
    }

    private UserProfileResponse mapToUserProfileResponse(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getProfileImageUrl(),
                user.getBio(),
                user.getInterests() != null ? new ArrayList<>(user.getInterests()) : new ArrayList<>(),
                user.getIsVerified(),
                user.getCreatedAt()
        );
    }
}