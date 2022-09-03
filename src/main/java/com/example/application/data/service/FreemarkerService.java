package com.example.application.data.service;

import java.io.File;
import java.util.Locale;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import com.example.application.data.entity.Language;
import com.example.application.data.entity.Market;
import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class FreemarkerService {

  public String process(String templateName, Market market, Language language,
      Map<String, Object> parameters) {
    try {
      Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
      Locale locale = new Locale(language.toString(), market.toString());
      cfg.setLocale(locale);
      cfg.setLocalizedLookup(true);
      cfg.setDirectoryForTemplateLoading(
          new File("C:\\Users\\milan\\Desktop\\email-template-manager\\local-templates\\"));
      Template template = cfg.getTemplate(templateName);
      return FreeMarkerTemplateUtils.processTemplateIntoString(template, parameters);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
