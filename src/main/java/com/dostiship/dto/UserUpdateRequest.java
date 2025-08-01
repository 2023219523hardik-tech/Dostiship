package com.dostiship.dto;

import jakarta.validation.constraints.Size;
import java.util.List;

public class UserUpdateRequest {

    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    private String profileImageUrl;

    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;

    private List<String> interests;

    // Constructors
    public UserUpdateRequest() {
    }

    public UserUpdateRequest(String fullName, String profileImageUrl, String bio, List<String> interests) {
        this.fullName = fullName;
        this.profileImageUrl = profileImageUrl;
        this.bio = bio;
        this.interests = interests;
    }

    // Getters and Setters
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
}