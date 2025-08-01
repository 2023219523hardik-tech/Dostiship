package com.dostiship.repository;

import com.dostiship.model.Event;
import com.dostiship.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE e.eventDateTime >= :now ORDER BY e.eventDateTime ASC")
    Page<Event> findUpcomingEvents(@Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.eventDateTime >= :startDate AND e.eventDateTime <= :endDate ORDER BY e.eventDateTime ASC")
    Page<Event> findEventsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate, 
                                     Pageable pageable);

    @Query("SELECT e FROM Event e WHERE LOWER(e.location) LIKE LOWER(CONCAT('%', :location, '%')) AND e.eventDateTime >= :now ORDER BY e.eventDateTime ASC")
    Page<Event> findEventsByLocation(@Param("location") String location, 
                                    @Param("now") LocalDateTime now, 
                                    Pageable pageable);

    Page<Event> findByCreator(User creator, Pageable pageable);

    @Query("SELECT e FROM Event e JOIN e.attendees a WHERE a.id = :userId ORDER BY e.eventDateTime ASC")
    Page<Event> findEventsByAttendee(@Param("userId") Long userId, Pageable pageable);

    @Query("""
        SELECT e FROM Event e 
        WHERE e.eventDateTime >= :now 
        AND EXISTS (
            SELECT 1 FROM User u 
            JOIN u.interests i 
            WHERE u.id = :userId 
            AND (LOWER(e.title) LIKE LOWER(CONCAT('%', i, '%')) 
                 OR LOWER(e.description) LIKE LOWER(CONCAT('%', i, '%')))
        )
        ORDER BY e.eventDateTime ASC
        """)
    List<Event> findRecommendedEventsForUser(@Param("userId") Long userId, 
                                           @Param("now") LocalDateTime now, 
                                           Pageable pageable);

    @Query("SELECT COUNT(a) FROM Event e JOIN e.attendees a WHERE e.id = :eventId")
    long countAttendeesByEventId(@Param("eventId") Long eventId);
}