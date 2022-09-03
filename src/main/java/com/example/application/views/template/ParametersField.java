package com.example.application.views.template;

import java.util.HashMap;
import java.util.Map;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class ParametersField extends CustomField<Map<String, Object>> {

  private VerticalLayout vl = new VerticalLayout();

  public ParametersField() {
    vl.setPadding(false);
    vl.setMargin(false);
    add(vl);
  }

  @Override
  protected Map<String, Object> generateModelValue() {
    Map<String, Object> map = new HashMap<>();
    for (int i = 0; i < vl.getComponentCount(); i++) {
      Component component = vl.getComponentAt(i);
      if (component instanceof Checkbox checkbox) {
        map.put(checkbox.getLabel(), checkbox.getValue());
      }
      if (component instanceof TextField textfield) {
        map.put(textfield.getLabel(), textfield.getValue());
      }
    }
    return map;
  }

  @Override
  protected void setPresentationValue(Map<String, Object> map) {
    for (String key : map.keySet()) {
      Object value = map.get(key);
      if (value instanceof Boolean valueBoolean) {
        Checkbox checkbox = new Checkbox(key);
        checkbox.setValue(valueBoolean);
        checkbox.addValueChangeListener(e -> setModelValue(generateModelValue(), e.isFromClient()));
        vl.add(checkbox);
      }
      if (value instanceof String valueString) {
        TextField textField = new TextField(key);
        textField.setValue(valueString);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField
            .addValueChangeListener(e -> setModelValue(generateModelValue(), e.isFromClient()));
        vl.add(textField);
      }
    }
  }

}
