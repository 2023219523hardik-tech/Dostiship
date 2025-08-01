package com.dostiship.controller;

import com.dostiship.dto.ContactRequest;
import com.dostiship.model.ContactSubmission;
import com.dostiship.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/contact")
@Tag(name = "Contact", description = "Contact form APIs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping
    @Operation(summary = "Submit contact form", description = "Submit a contact form message")
    public ResponseEntity<Map<String, Object>> submitContactForm(@Valid @RequestBody ContactRequest contactRequest) {
        ContactSubmission submission = contactService.submitContactForm(contactRequest);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Thank you for your message! We'll get back to you soon.");
        response.put("submissionId", submission.getId());
        
        return ResponseEntity.ok(response);
    }
}