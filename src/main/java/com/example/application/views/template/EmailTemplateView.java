package com.example.application.views.template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.security.PermitAll;
import com.example.application.components.dialog.DialogBuilder;
import com.example.application.components.html.HtmlPresenter;
import com.example.application.data.entity.EmailTemplate;
import com.example.application.data.entity.EmailTemplateVariation;
import com.example.application.data.service.EmailTemplateRepository;
import com.example.application.data.service.EmailTemplateVariationRepository;
import com.example.application.data.service.FreemarkerService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;

@PageTitle("Email Template Details")
@Route(value = "email-templates", layout = MainLayout.class)
@PermitAll
@RequiredArgsConstructor
public class EmailTemplateView extends VerticalLayout implements HasUrlParameter<Long> {

  private final EmailTemplateRepository repository;
  private final EmailTemplateVariationRepository variationRepository;
  private final FreemarkerService freemarkerService;

  @Override
  public void setParameter(BeforeEvent event, Long parameter) {
    removeAll();

    EmailTemplate template = repository.findById(parameter).orElseThrow();

    Button buttonAdd = new Button(VaadinIcon.PLUS.create());
    buttonAdd.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    buttonAdd.getStyle().set("margin-left", "auto");

    Button buttonEdit = new Button(VaadinIcon.EDIT.create());
    buttonEdit.setEnabled(false);

    Button buttonDelete = new Button(VaadinIcon.TRASH.create());
    buttonDelete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
    buttonDelete.setEnabled(false);

    HorizontalLayout buttonsLayout =
        new HorizontalLayout(new H3(template.getName()), buttonAdd, buttonEdit, buttonDelete);
    buttonsLayout.setWidthFull();
    add(buttonsLayout);

    Grid<EmailTemplateVariation> grid = new Grid<>(EmailTemplateVariation.class, false);
    grid.addComponentColumn(e -> previewButtonColumn(e)).setHeader("Preview");
    grid.addComponentColumn(e -> previewFullButtonColumn(template, e)).setHeader("Preview Full");
    grid.addColumn(EmailTemplateVariation.Fields.market).setAutoWidth(true);
    grid.addColumn(EmailTemplateVariation.Fields.language).setAutoWidth(true);
    grid.addColumn(EmailTemplateVariation.Fields.subject).setAutoWidth(true);
    grid.addColumn(EmailTemplateVariation.Fields.content).setAutoWidth(true);
    add(grid);

    grid.setSelectionMode(SelectionMode.MULTI);
    grid.asMultiSelect().addValueChangeListener(e -> {
      buttonEdit.setEnabled(e.getValue().size() == 1);
      buttonDelete.setEnabled(e.getValue().size() > 0);
    });

    grid.setItems(variationRepository.findByTemplate(template));

    buttonEdit.addClickListener(e -> {
      EmailTemplateVariation variation = grid.asMultiSelect().getValue().iterator().next();
      VerticalLayout vl = new VerticalLayout();
      TextField tf = new TextField("Subject");
      tf.setWidthFull();
      tf.setValue(variation.getSubject());
      TextArea ta = new TextArea("Content");
      ta.setWidthFull();
      ta.setValue(variation.getContent());
      vl.add(tf, ta);
      vl.setWidth("90vw");
      DialogBuilder.from(variation.getName(), vl).withOk(() -> {
      }).withCancel().open();
    });
  }

  private Button previewFullButtonColumn(EmailTemplate template, EmailTemplateVariation e) {
    Button button = new Button(VaadinIcon.EYE.create());
    button.addClickListener(click -> {
      HorizontalLayout hl = new HorizontalLayout();
      VerticalLayout vl = new VerticalLayout();
      vl.getStyle().set("border-left", "1px solid var(--lumo-body-text-color)");
      // vl.setMargin(false);
      // vl.setPadding(false);
      String subject = freemarkerService.process(e.getNameSubject(), e.getMarket(), e.getLanguage(),
          e.getTemplate().getParameters());
      H3 h3 = new H3(subject);
      h3.getStyle().set("margin-top", "0");
      String content = freemarkerService.process(e.getNameContent(), e.getMarket(), e.getLanguage(),
          e.getTemplate().getParameters());
      List<EmailTemplateVariation> parentVariations =
          variationRepository.findByTemplate(template.getParent());
      EmailTemplateVariation parentVariation = parentVariations.get(0);
      Map<String, Object> parentMap = new HashMap<>();
      parentMap.put("body", content);
      parentMap.put("customerPortalUrl", "https://ananas.rs");
      parentMap.put("termsAndConditionsToggle", true);
      parentMap.put("assetsRootUrl", "https://static.ananas.rs");
      parentMap.put("randomFooterPattern", "green");
      String parentContent = freemarkerService.process(parentVariation.getNameContent(),
          e.getMarket(), e.getLanguage(), parentMap);
      HtmlPresenter presenter = new HtmlPresenter();
      presenter.setValue(parentContent);
      vl.add(h3, presenter);

      ParametersField parametersField = new ParametersField();
      parametersField.setValue(e.getTemplate().getParameters());
      parametersField.addValueChangeListener(e1 -> {
        String s1 = freemarkerService.process(e.getNameSubject(), e.getMarket(), e.getLanguage(),
            e1.getValue());
        h3.setText(s1);
        String c1 = freemarkerService.process(e.getNameContent(), e.getMarket(), e.getLanguage(),
            e1.getValue());
        presenter.setValue(c1);
      });
      hl.add(parametersField, vl);
      hl.setFlexGrow(1, vl);
      hl.setWidth("90vw");
      DialogBuilder.from(e.getName(), hl).withClose().open();
    });
    return button;
  }

  private Button previewButtonColumn(EmailTemplateVariation e) {
    Button button = new Button(VaadinIcon.EYE.create());
    button.addClickListener(click -> {
      HorizontalLayout hl = new HorizontalLayout();
      VerticalLayout vl = new VerticalLayout();
      vl.getStyle().set("border-left", "1px solid var(--lumo-body-text-color)");
      // vl.setMargin(false);
      // vl.setPadding(false);
      String subject = freemarkerService.process(e.getNameSubject(), e.getMarket(), e.getLanguage(),
          e.getTemplate().getParameters());
      H3 h3 = new H3(subject);
      h3.getStyle().set("margin-top", "0");
      String content = freemarkerService.process(e.getNameContent(), e.getMarket(), e.getLanguage(),
          e.getTemplate().getParameters());
      HtmlPresenter presenter = new HtmlPresenter();
      presenter.setValue(content);
      vl.add(h3, presenter);

      ParametersField parametersField = new ParametersField();
      parametersField.setValue(e.getTemplate().getParameters());
      parametersField.addValueChangeListener(e1 -> {
        String s1 = freemarkerService.process(e.getNameSubject(), e.getMarket(), e.getLanguage(),
            e1.getValue());
        h3.setText(s1);
        String c1 = freemarkerService.process(e.getNameContent(), e.getMarket(), e.getLanguage(),
            e1.getValue());
        presenter.setValue(c1);
      });
      hl.add(parametersField, vl);
      hl.setFlexGrow(1, vl);
      hl.setWidth("90vw");
      DialogBuilder.from(e.getName(), hl).withClose().open();
    });
    return button;
  }

}
