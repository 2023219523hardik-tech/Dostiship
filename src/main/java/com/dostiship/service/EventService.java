package com.dostiship.service;

import com.dostiship.dto.EventCreateRequest;
import com.dostiship.dto.EventResponse;
import com.dostiship.exception.BadRequestException;
import com.dostiship.exception.ResourceNotFoundException;
import com.dostiship.model.Event;
import com.dostiship.model.User;
import com.dostiship.repository.EventRepository;
import com.dostiship.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    public EventResponse createEvent(Long creatorId, EventCreateRequest request) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", creatorId));

        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setEventDateTime(request.getEventDateTime());
        event.setLocation(request.getLocation());
        event.setCreator(creator);

        Event savedEvent = eventRepository.save(event);
        return mapToEventResponse(savedEvent);
    }

    public Page<EventResponse> getAllEvents(int page, int size, String location, LocalDateTime startDate, LocalDateTime endDate) {
        Pageable pageable = PageRequest.of(page, size);
        LocalDateTime now = LocalDateTime.now();

        Page<Event> events;
        
        if (location != null && !location.trim().isEmpty()) {
            events = eventRepository.findEventsByLocation(location, now, pageable);
        } else if (startDate != null && endDate != null) {
            events = eventRepository.findEventsByDateRange(startDate, endDate, pageable);
        } else {
            events = eventRepository.findUpcomingEvents(now, pageable);
        }

        return events.map(this::mapToEventResponse);
    }

    public EventResponse getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        return mapToEventResponse(event);
    }

    public EventResponse rsvpToEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        if (event.isAttendee(user)) {
            throw new BadRequestException("User is already registered for this event");
        }

        event.addAttendee(user);
        Event savedEvent = eventRepository.save(event);

        return mapToEventResponse(savedEvent);
    }

    public EventResponse cancelRsvp(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        if (!event.isAttendee(user)) {
            throw new BadRequestException("User is not registered for this event");
        }

        event.removeAttendee(user);
        Event savedEvent = eventRepository.save(event);

        return mapToEventResponse(savedEvent);
    }

    public List<EventResponse> getRecommendedEvents(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        LocalDateTime now = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, 10); // Get top 10 recommendations

        List<Event> recommendedEvents = eventRepository.findRecommendedEventsForUser(userId, now, pageable);

        return recommendedEvents.stream()
                .map(this::mapToEventResponse)
                .collect(Collectors.toList());
    }

    public Page<EventResponse> getUserEvents(Long userId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Pageable pageable = PageRequest.of(page, size);
        Page<Event> events = eventRepository.findEventsByAttendee(userId, pageable);

        return events.map(this::mapToEventResponse);
    }

    private EventResponse mapToEventResponse(Event event) {
        EventResponse response = new EventResponse();
        response.setId(event.getId());
        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setEventDateTime(event.getEventDateTime());
        response.setLocation(event.getLocation());
        response.setCreatorId(event.getCreator().getId());
        response.setCreatorName(event.getCreator().getFullName());
        response.setAttendeeCount(event.getAttendees().size());
        response.setCreatedAt(event.getCreatedAt());
        return response;
    }
}