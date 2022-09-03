package com.example.application.views.template;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import com.example.application.data.entity.EmailTemplate;
import com.example.application.data.service.EmailTemplateRepository;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouterLink;
import lombok.RequiredArgsConstructor;

@PageTitle("Email Templates")
@RouteAlias(value = "", layout = MainLayout.class)
@Route(value = "email-templates", layout = MainLayout.class)
@PermitAll
@RequiredArgsConstructor
public class EmailTemplatesView extends Div {

  private final EmailTemplateRepository repository;

  private Grid<EmailTemplate> grid = new Grid<>(EmailTemplate.class, false);

  @PostConstruct
  void init() {
    addClassNames("master-detail-view");

    // Configure buttons
    Button buttonAdd = new Button(VaadinIcon.PLUS.create());
    buttonAdd.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    buttonAdd.getStyle().set("margin-left", "auto");

    Button buttonEdit = new Button(VaadinIcon.EDIT.create());
    buttonEdit.setEnabled(false);

    Button buttonDelete = new Button(VaadinIcon.TRASH.create());
    buttonDelete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
    buttonDelete.setEnabled(false);

    add(new HorizontalLayout(new Button("DEV -> QA"), new Button("QA -> STAGE"),
        new Button("STAGE -> PROD"), buttonAdd, buttonEdit, buttonDelete));

    // Configure Grid
    grid.addComponentColumn(
        e -> new RouterLink(e.getFullPath(), EmailTemplateView.class, e.getId())).setHeader("Name")
        .setAutoWidth(true);
    grid.addColumn(e -> e.getParent() == null ? "" : e.getParent().getFullPath())
        .setHeader("Parent");
    grid.addColumn(e -> "v1").setHeader("Dev version");
    grid.addColumn(e -> "v1").setHeader("QA version");
    grid.addColumn(e -> "v1").setHeader("Stage version");
    grid.addColumn(e -> "v1").setHeader("Prod version");

    grid.setSelectionMode(SelectionMode.MULTI);
    grid.asMultiSelect().addValueChangeListener(e -> {
      buttonEdit.setEnabled(e.getValue().size() == 1);
      buttonDelete.setEnabled(e.getValue().size() > 0);
    });
    grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    grid.setItems(repository.findAll());

    add(grid);
  }
}
