package com.dostiship.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class EventCreateRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Event date and time is required")
    private LocalDateTime eventDateTime;

    @NotBlank(message = "Location is required")
    private String location;

    // Constructors
    public EventCreateRequest() {
    }

    public EventCreateRequest(String title, String description, LocalDateTime eventDateTime, String location) {
        this.title = title;
        this.description = description;
        this.eventDateTime = eventDateTime;
        this.location = location;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(LocalDateTime eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}