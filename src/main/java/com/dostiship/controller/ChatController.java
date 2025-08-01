package com.dostiship.controller;

import com.dostiship.dto.ChatMessageDto;
import com.dostiship.security.UserPrincipal;
import com.dostiship.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chats")
@Tag(name = "Chat & Messaging", description = "Real-time messaging APIs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping
    @Operation(summary = "Get user conversations", description = "Get a list of all conversations for the current user")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Map<String, Object>>> getUserConversations(@AuthenticationPrincipal UserPrincipal currentUser) {
        List<Map<String, Object>> conversations = chatService.getUserConversations(currentUser.getId());
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get conversation with user", description = "Get paginated message history between the authenticated user and specified user")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Page<ChatMessageDto>> getConversation(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Page<ChatMessageDto> conversation = chatService.getConversation(currentUser.getId(), userId, page, size);
        return ResponseEntity.ok(conversation);
    }

    @PostMapping("/send")
    @Operation(summary = "Send message", description = "Send a message to another user (also available via WebSocket)")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ChatMessageDto> sendMessage(@AuthenticationPrincipal UserPrincipal currentUser,
                                                     @Valid @RequestBody ChatMessageDto messageDto) {
        ChatMessageDto sentMessage = chatService.sendMessage(currentUser.getId(), messageDto);
        return ResponseEntity.ok(sentMessage);
    }
}

@Controller
class WebSocketChatController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat")
    public void sendMessage(@Payload ChatMessageDto messageDto, Principal principal) {
        // Extract user ID from principal (this would be set by WebSocket security)
        // For now, we'll assume the sender ID is included in the message
        chatService.sendMessage(messageDto.getSenderId(), messageDto);
    }
}