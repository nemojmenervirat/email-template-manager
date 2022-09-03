package com.example.application.data.generator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.application.data.Role;
import com.example.application.data.entity.EmailTemplate;
import com.example.application.data.entity.EmailTemplateVariation;
import com.example.application.data.entity.Language;
import com.example.application.data.entity.Market;
import com.example.application.data.entity.User;
import com.example.application.data.service.EmailTemplateRepository;
import com.example.application.data.service.EmailTemplateVariationRepository;
import com.example.application.data.service.UserRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
public class DataGenerator {

  @Bean
  public CommandLineRunner loadData(PasswordEncoder passwordEncoder,
      EmailTemplateRepository templateRepository,
      EmailTemplateVariationRepository variationRepository, UserRepository userRepository) {
    return args -> {
      Logger logger = LoggerFactory.getLogger(getClass());


      logger.info("Generating demo data");

      createUsers(passwordEncoder, userRepository, logger);

      if (templateRepository.count() != 0L) {
        logger.info("Using existing database");
        return;
      }
      logger.info("... generating Email Template entities...");

      // common/v2/header
      {
        Map<String, Object> map = new HashMap<>();
        EmailTemplate template = EmailTemplate.builder().name("header").directoryPath("includes/v2")
            .parameters(map).build();
        templateRepository.save(template);
        {
          EmailTemplateVariation variation = EmailTemplateVariation.builder().template(template)
              .market(Market.RS).language(Language.SR)
              .content(Utils.readContent("includes/v2/header.ftlh")).build();
          variationRepository.save(variation);
        }
      }
      // common/v2/body
      {
        Map<String, Object> map = new HashMap<>();
        EmailTemplate template = EmailTemplate.builder().name("body").directoryPath("includes/v2")
            .parameters(map).build();
        templateRepository.save(template);
        {
          EmailTemplateVariation variation = EmailTemplateVariation.builder().template(template)
              .market(Market.RS).language(Language.SR)
              .content(Utils.readContent("includes/v2/body.ftlh")).build();
          variationRepository.save(variation);
        }
      }
      // common/v2/footer
      {
        Map<String, Object> map = new HashMap<>();
        EmailTemplate template = EmailTemplate.builder().name("footer").directoryPath("includes/v2")
            .parameters(map).build();
        templateRepository.save(template);
        {
          EmailTemplateVariation variation = EmailTemplateVariation.builder().template(template)
              .market(Market.RS).language(Language.SR)
              .content(Utils.readContent("includes/v2/footer.ftlh")).build();
          variationRepository.save(variation);
        }
      }
      // common/v2/footer-image
      {
        Map<String, Object> map = new HashMap<>();
        EmailTemplate template = EmailTemplate.builder().name("footer-image")
            .directoryPath("includes/v2").parameters(map).build();
        templateRepository.save(template);
        {
          EmailTemplateVariation variation = EmailTemplateVariation.builder().template(template)
              .market(Market.RS).language(Language.SR)
              .content(Utils.readContent("includes/v2/footer-image.ftlh")).build();
          variationRepository.save(variation);
        }
      }
      // common/v2/plain
      EmailTemplate plain = null;
      {
        Map<String, Object> map = new HashMap<>();
        EmailTemplate template = EmailTemplate.builder().name("plain").directoryPath("includes/v2")
            .parameters(map).build();
        plain = templateRepository.save(template);
        {
          EmailTemplateVariation variation = EmailTemplateVariation.builder().template(template)
              .market(Market.RS).language(Language.SR)
              .content(Utils.readContent("includes/v2/plain.ftlh")).build();
          variationRepository.save(variation);
        }
      }

      // ananas-express-damaged-shipment
      {
        Map<String, Object> map = new HashMap<>();
        map.put("invoiced", true);
        map.put("suborderId", "P061S-ZK975-DS-1");
        map.put("paymentMethod", "PBC");
        map.put("paymentCompleted", true);
        map.put("customerSupportPhone", "0800 000 111");
        map.put("customerSupportEmail", "support@ananas.rs");
        EmailTemplate template =
            EmailTemplate.builder().name("ananas-express-damaged-shipment").parameters(map).build();
        templateRepository.save(template);
        {
          EmailTemplateVariation variation = EmailTemplateVariation.builder().template(template)
              .market(Market.RS).language(Language.SR).subject("${suborderId} - Oštećena pošiljka")
              .content(Utils.readContent(template.getName())).build();
          variationRepository.save(variation);
        }
        {
          EmailTemplateVariation variation = EmailTemplateVariation.builder().template(template)
              .market(Market.RS).language(Language.EN).subject("${suborderId} - Damaged shippment")
              .content(Utils.readContent(template.getName() + "-en")).build();
          variationRepository.save(variation);
        }
      }
      // merchant-damaged-shipment
      {
        EmailTemplate template = EmailTemplate.builder().name("merchant-damaged-shipment").build();
        templateRepository.save(template);
      }
      // API_EMPLOYEE_CONFIGURATION_EXCEPTION
      {
        Map<String, Object> map = new HashMap<>();
        map.put("merchantName", "Delfi");
        EmailTemplate template = EmailTemplate.builder()
            .name("API_EMPLOYEE_CONFIGURATION_EXCEPTION").parameters(map).parent(plain).build();
        templateRepository.save(template);
        {
          EmailTemplateVariation variation =
              EmailTemplateVariation.builder().template(template).market(Market.RS)
                  .language(Language.SR).subject("${merchantName} API - Greška u proveri kataloga")
                  .content(Utils.readContent(template.getName())).build();
          variationRepository.save(variation);
        }
      }


      logger.info("Generated demo data");
    };
  }

  private void createUsers(PasswordEncoder passwordEncoder, UserRepository userRepository,
      Logger logger) {
    logger.info("... generating 2 User entities...");
    User user = new User();
    user.setName("John Normal");
    user.setUsername("user");
    user.setHashedPassword(passwordEncoder.encode("user"));
    user.setProfilePictureUrl(
        "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
    user.setRoles(Collections.singleton(Role.USER));
    userRepository.save(user);
    User admin = new User();
    admin.setName("Emma Powerful");
    admin.setUsername("admin");
    admin.setHashedPassword(passwordEncoder.encode("admin"));
    admin.setProfilePictureUrl(
        "https://images.unsplash.com/photo-1607746882042-944635dfe10e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
    admin.setRoles(Set.of(Role.USER, Role.ADMIN));
    userRepository.save(admin);
  }

}
