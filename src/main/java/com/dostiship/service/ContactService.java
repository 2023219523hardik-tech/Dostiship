package com.dostiship.service;

import com.dostiship.dto.ContactRequest;
import com.dostiship.model.ContactSubmission;
import com.dostiship.repository.ContactSubmissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContactService {

    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);

    @Autowired
    private ContactSubmissionRepository contactSubmissionRepository;

    public ContactSubmission submitContactForm(ContactRequest contactRequest) {
        ContactSubmission submission = new ContactSubmission();
        submission.setName(contactRequest.getName());
        submission.setEmail(contactRequest.getEmail());
        submission.setMessage(contactRequest.getMessage());

        ContactSubmission savedSubmission = contactSubmissionRepository.save(submission);

        // Log the submission (in a real application, you might send an email to admin)
        logger.info("New contact form submission received from: {} ({})", 
                   contactRequest.getName(), contactRequest.getEmail());

        return savedSubmission;
    }
}