package com.example.application.data.entity;

import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Entity
@Getter
@Setter
@Builder
@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "json", typeClass = JsonType.class)
public class EmailTemplate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Type(type = "json")
  @Column(columnDefinition = "json")
  private Map<String, Object> parameters;

  private String directoryPath;

  @ManyToOne
  @JoinColumn
  private EmailTemplate parent;

  public String getFullPath() {
    return StringUtils.isBlank(directoryPath) ? name : directoryPath + "/" + name;
  }

}
