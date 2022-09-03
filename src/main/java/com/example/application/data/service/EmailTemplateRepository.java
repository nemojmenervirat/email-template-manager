package com.example.application.data.service;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.application.data.entity.EmailTemplate;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {

}
