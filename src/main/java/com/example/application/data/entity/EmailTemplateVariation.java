package com.example.application.data.entity;

import java.io.File;
import java.nio.charset.Charset;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostPersist;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldNameConstants
public class EmailTemplateVariation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn
  private EmailTemplate template;

  @Enumerated(EnumType.STRING)
  private Market market;

  @Enumerated(EnumType.STRING)
  private Language language;

  private String subject;

  @Column(length = 100000)
  private String content;

  public String getName() {
    String result = "";
    if (StringUtils.isNotBlank(template.getDirectoryPath())) {
      result += template.getDirectoryPath() + "/";
    }
    result += template.getName() + "_" + language.toString().toLowerCase() + "_" + market;
    return result;
  }

  public String getNameSubject() {
    return getName() + "__subject.ftlh";
  }

  public String getNameContent() {
    return getName() + ".ftlh";
  }


  @PostPersist
  public void logNewUserAdded() {
    try {
      String root = "C:\\Users\\milan\\Desktop\\email-template-manager\\local-templates\\";
      FileUtils.write(new File(root + getNameContent()), content, Charset.forName("UTF-8"));
      if (StringUtils.isNotBlank(subject)) {
        FileUtils.write(new File(root + getNameSubject()), subject, Charset.forName("UTF-8"));
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
