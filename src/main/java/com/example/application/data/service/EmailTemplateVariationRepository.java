package com.example.application.data.service;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.application.data.entity.EmailTemplate;
import com.example.application.data.entity.EmailTemplateVariation;

public interface EmailTemplateVariationRepository
    extends JpaRepository<EmailTemplateVariation, Long> {

  List<EmailTemplateVariation> findByTemplate(EmailTemplate template);

}
