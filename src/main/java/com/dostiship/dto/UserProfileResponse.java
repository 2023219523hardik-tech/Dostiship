package com.dostiship.dto;

import java.time.LocalDateTime;
import java.util.List;

public class UserProfileResponse {

    private Long id;
    private String email;
    private String fullName;
    private String profileImageUrl;
    private String bio;
    private List<String> interests;
    private Boolean isVerified;
    private LocalDateTime createdAt;

    // Constructors
    public UserProfileResponse() {
    }

    public UserProfileResponse(Long id, String email, String fullName, String profileImageUrl,
                              String bio, List<String> interests, Boolean isVerified, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.profileImageUrl = profileImageUrl;
        this.bio = bio;
        this.interests = interests;
        this.isVerified = isVerified;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean verified) {
        isVerified = verified;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}