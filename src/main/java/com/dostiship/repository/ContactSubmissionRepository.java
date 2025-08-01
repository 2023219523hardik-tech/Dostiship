package com.dostiship.repository;

import com.dostiship.model.ContactSubmission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactSubmissionRepository extends JpaRepository<ContactSubmission, Long> {

    Page<ContactSubmission> findByProcessedOrderBySubmittedAtDesc(Boolean processed, Pageable pageable);

    Page<ContactSubmission> findAllByOrderBySubmittedAtDesc(Pageable pageable);

    long countByProcessed(Boolean processed);
}