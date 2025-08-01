package com.dostiship.repository;

import com.dostiship.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id != :currentUserId")
    Page<User> findAllExceptCurrentUser(@Param("currentUserId") Long currentUserId, Pageable pageable);

    @Query("""
        SELECT u, 
               (SELECT COUNT(ui1.interest) 
                FROM User u1 
                JOIN u1.interests ui1 
                WHERE u1.id = :currentUserId 
                AND ui1 IN (SELECT ui2.interest FROM User u2 JOIN u2.interests ui2 WHERE u2.id = u.id)) as sharedInterests
        FROM User u 
        WHERE u.id != :currentUserId
        ORDER BY sharedInterests DESC
        """)
    List<Object[]> findUsersBySharedInterests(@Param("currentUserId") Long currentUserId, Pageable pageable);

    @Query("SELECT DISTINCT u FROM User u JOIN u.interests i WHERE i IN :interests AND u.id != :currentUserId")
    List<User> findUsersByInterests(@Param("interests") List<String> interests, 
                                   @Param("currentUserId") Long currentUserId);
}