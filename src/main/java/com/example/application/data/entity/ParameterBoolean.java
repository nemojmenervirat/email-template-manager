package com.example.application.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
public class ParameterBoolean extends Parameter {

  private boolean defaultValue;

}
