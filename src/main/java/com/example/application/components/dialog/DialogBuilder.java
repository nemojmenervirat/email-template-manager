package com.example.application.components.dialog;

import java.util.function.Consumer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DialogBuilder {

  private Dialog dialog = new Dialog();
  private DialogBuilderEvent event = new DialogBuilderEvent(dialog);
  private VerticalLayout verticalLayout = new VerticalLayout();
  private HorizontalLayout horizontalLayout = new HorizontalLayout();
  private Button buttonOk = new Button("OK", VaadinIcon.CHECK.create());
  private Button buttonCancel = new Button("Cancel", VaadinIcon.CLOSE.create());
  private Button buttonClose = new Button("Close", VaadinIcon.CLOSE.create());
  private Button buttonDelete = new Button("Delete", VaadinIcon.TRASH.create());

  private DialogBuilder(Component title, Component content) {
    dialog.add(verticalLayout);
    dialog.setMinWidth("30%");

    buttonOk.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    buttonDelete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
    horizontalLayout.add(buttonDelete, buttonOk, buttonCancel, buttonClose);
    horizontalLayout.setWidthFull();
    buttonOk.addClassName("ms-auto");
    buttonClose.addClassName("ms-auto");

    buttonOk.setVisible(false);
    buttonDelete.setVisible(false);
    buttonCancel.setVisible(false);
    buttonClose.setVisible(false);

    verticalLayout.setMaxHeight("100%");
    verticalLayout.add(title);
    verticalLayout.add(new Hr());
    verticalLayout.add(content);
    content.getElement().getStyle().set("flex", "1 1 auto").set("min-height", "0px")
        .set("overflow-y", "auto");
    verticalLayout.add(new Hr());
    verticalLayout.add(horizontalLayout);
  }

  public static DialogBuilder from(Component title, Component content) {
    return new DialogBuilder(title, content);
  }

  public static DialogBuilder from(String title, Component content) {
    return new DialogBuilder(new H3(title), content);
  }

  public static DialogBuilder from(String title, String content) {
    return new DialogBuilder(new H3(title), new Paragraph(content));
  }

  public DialogBuilder draggable() {
    dialog.setDraggable(true);
    return this;
  }

  public DialogBuilder modal() {
    dialog.setModal(true);
    return this;
  }

  public DialogBuilder withOk(Runnable okAction) {
    return withOk(e -> {
      okAction.run();
      e.close();
    });
  }

  public DialogBuilder withOk(Consumer<DialogBuilderEvent> okAction) {
    buttonOk.setVisible(true);
    buttonOk.addClickListener(e -> okAction.accept(event));
    return this;
  }

  public DialogBuilder withDelete(Consumer<DialogBuilderEvent> deleteAction) {
    buttonDelete.setVisible(true);
    buttonDelete.addClickListener(e -> deleteAction.accept(event));
    return this;
  }

  public DialogBuilder withCancel() {
    return withCancel(e -> e.close());
  }

  public DialogBuilder withCancel(Runnable cancelAction) {
    return withCancel(e -> {
      cancelAction.run();
      e.close();
    });
  }

  public DialogBuilder withCancel(Consumer<DialogBuilderEvent> cancelAction) {
    buttonCancel.setVisible(true);
    buttonCancel.addClickListener(e -> cancelAction.accept(event));
    return this;
  }

  public DialogBuilder withClose() {
    return withClose(() -> dialog.close());
  }

  public DialogBuilder withClose(Runnable closeAction) {
    buttonClose.setVisible(true);
    buttonClose.addClickListener(e -> closeAction.run());
    return this;
  }

  public void open() {
    dialog.setOpened(true);
  }

  public static class DialogBuilderEvent {

    private final Dialog dialog;

    public DialogBuilderEvent(Dialog dialog) {
      this.dialog = dialog;
    }

    public void close() {
      dialog.close();
    }

  }

}
