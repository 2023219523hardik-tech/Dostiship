package com.dostiship.controller;

import com.dostiship.dto.EventCreateRequest;
import com.dostiship.dto.EventResponse;
import com.dostiship.security.UserPrincipal;
import com.dostiship.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@Tag(name = "Events & Activities", description = "Event management APIs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    @Operation(summary = "Create new event", description = "Create a new event")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<EventResponse> createEvent(@AuthenticationPrincipal UserPrincipal currentUser,
                                                    @Valid @RequestBody EventCreateRequest eventRequest) {
        EventResponse event = eventService.createEvent(currentUser.getId(), eventRequest);
        return ResponseEntity.ok(event);
    }

    @GetMapping
    @Operation(summary = "Get all events", description = "Get paginated list of all upcoming events with optional filtering")
    public ResponseEntity<Page<EventResponse>> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        Page<EventResponse> events = eventService.getAllEvents(page, size, location, startDate, endDate);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get event by ID", description = "Get details for a specific event")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        EventResponse event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @PostMapping("/{id}/rsvp")
    @Operation(summary = "RSVP to event", description = "Register the current user for an event")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<EventResponse> rsvpToEvent(@AuthenticationPrincipal UserPrincipal currentUser,
                                                    @PathVariable Long id) {
        EventResponse event = eventService.rsvpToEvent(currentUser.getId(), id);
        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/{id}/rsvp")
    @Operation(summary = "Cancel RSVP", description = "Cancel the current user's RSVP for an event")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<EventResponse> cancelRsvp(@AuthenticationPrincipal UserPrincipal currentUser,
                                                   @PathVariable Long id) {
        EventResponse event = eventService.cancelRsvp(currentUser.getId(), id);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/recommendations")
    @Operation(summary = "Get recommended events", description = "Get event recommendations based on user interests")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<EventResponse>> getRecommendedEvents(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<EventResponse> recommendedEvents = eventService.getRecommendedEvents(currentUser.getId());
        return ResponseEntity.ok(recommendedEvents);
    }

    @GetMapping("/my-events")
    @Operation(summary = "Get user's events", description = "Get events that the current user is attending")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Page<EventResponse>> getUserEvents(@AuthenticationPrincipal UserPrincipal currentUser,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        Page<EventResponse> userEvents = eventService.getUserEvents(currentUser.getId(), page, size);
        return ResponseEntity.ok(userEvents);
    }
}