package com.dostiship.service;

import com.dostiship.dto.ChatMessageDto;
import com.dostiship.exception.ResourceNotFoundException;
import com.dostiship.model.ChatMessage;
import com.dostiship.model.User;
import com.dostiship.repository.ChatMessageRepository;
import com.dostiship.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public ChatMessageDto sendMessage(Long senderId, ChatMessageDto messageDto) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", senderId));

        User recipient = userRepository.findById(messageDto.getRecipientId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", messageDto.getRecipientId()));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(sender);
        chatMessage.setRecipient(recipient);
        chatMessage.setContent(messageDto.getContent());

        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        // Convert to DTO for response
        ChatMessageDto responseDto = mapToChatMessageDto(savedMessage);

        // Send real-time notification to recipient
        messagingTemplate.convertAndSendToUser(
                recipient.getId().toString(),
                "/queue/messages",
                responseDto
        );

        return responseDto;
    }

    public Page<ChatMessageDto> getConversation(Long userId, Long otherUserId, int page, int size) {
        // Verify both users exist
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        userRepository.findById(otherUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", otherUserId));

        Pageable pageable = PageRequest.of(page, size);
        Page<ChatMessage> messages = chatMessageRepository.findConversationBetweenUsers(userId, otherUserId, pageable);

        return messages.map(this::mapToChatMessageDto);
    }

    public List<Map<String, Object>> getUserConversations(Long userId) {
        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        List<Object[]> conversationsData = chatMessageRepository.findConversationsForUser(userId);

        return conversationsData.stream().map(data -> {
            User otherUser = (User) data[0];
            LocalDateTime lastMessageTime = (LocalDateTime) data[1];

            // Get the last message for preview
            List<ChatMessage> lastMessages = chatMessageRepository.findLatestMessageBetweenUsers(
                    userId, otherUser.getId(), PageRequest.of(0, 1));

            Map<String, Object> conversation = new HashMap<>();
            conversation.put("userId", otherUser.getId());
            conversation.put("userName", otherUser.getFullName());
            conversation.put("userProfileImage", otherUser.getProfileImageUrl());
            conversation.put("lastMessageTime", lastMessageTime);

            if (!lastMessages.isEmpty()) {
                ChatMessage lastMessage = lastMessages.get(0);
                conversation.put("lastMessage", lastMessage.getContent());
                conversation.put("lastMessageSender", lastMessage.getSender().getFullName());
            }

            return conversation;
        }).collect(Collectors.toList());
    }

    public void markMessagesAsRead(Long userId, Long senderId) {
        // This could be implemented to mark messages as read
        // For now, we'll skip this implementation
    }

    private ChatMessageDto mapToChatMessageDto(ChatMessage message) {
        ChatMessageDto dto = new ChatMessageDto();
        dto.setId(message.getId());
        dto.setRecipientId(message.getRecipient().getId());
        dto.setContent(message.getContent());
        dto.setSenderId(message.getSender().getId());
        dto.setSenderName(message.getSender().getFullName());
        dto.setTimestamp(message.getTimestamp());
        dto.setStatus(message.getStatus());
        return dto;
    }
}