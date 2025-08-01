package com.dostiship.repository;

import com.dostiship.model.ChatMessage;
import com.dostiship.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("""
        SELECT cm FROM ChatMessage cm 
        WHERE (cm.sender.id = :user1Id AND cm.recipient.id = :user2Id) 
           OR (cm.sender.id = :user2Id AND cm.recipient.id = :user1Id)
        ORDER BY cm.timestamp ASC
        """)
    Page<ChatMessage> findConversationBetweenUsers(@Param("user1Id") Long user1Id, 
                                                   @Param("user2Id") Long user2Id, 
                                                   Pageable pageable);

    @Query("""
        SELECT DISTINCT CASE 
            WHEN cm.sender.id = :userId THEN cm.recipient 
            ELSE cm.sender 
        END as otherUser,
        MAX(cm.timestamp) as lastMessageTime
        FROM ChatMessage cm 
        WHERE cm.sender.id = :userId OR cm.recipient.id = :userId
        GROUP BY otherUser
        ORDER BY lastMessageTime DESC
        """)
    List<Object[]> findConversationsForUser(@Param("userId") Long userId);

    @Query("""
        SELECT cm FROM ChatMessage cm 
        WHERE (cm.sender.id = :user1Id AND cm.recipient.id = :user2Id) 
           OR (cm.sender.id = :user2Id AND cm.recipient.id = :user1Id)
        ORDER BY cm.timestamp DESC
        """)
    List<ChatMessage> findLatestMessageBetweenUsers(@Param("user1Id") Long user1Id, 
                                                    @Param("user2Id") Long user2Id, 
                                                    Pageable pageable);

    long countByRecipientAndStatus(User recipient, ChatMessage.MessageStatus status);
}