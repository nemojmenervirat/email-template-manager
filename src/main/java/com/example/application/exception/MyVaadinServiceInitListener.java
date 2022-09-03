package com.example.application.exception;

import com.example.application.exception.ExceptionHandler.ProcessedException;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.auth.ViewAccessChecker;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.RequiredArgsConstructor;

@SpringComponent
@RequiredArgsConstructor
public class MyVaadinServiceInitListener implements VaadinServiceInitListener {

  private final ExceptionHandler exceptionHandler;
  private final ViewAccessChecker viewAccessChecker;

  @Override
  public void serviceInit(ServiceInitEvent event) {

    event.getSource().addUIInitListener(
        uiInitEvent -> uiInitEvent.getUI().addBeforeEnterListener(viewAccessChecker));

    event.getSource().addSessionInitListener(e -> {
      e.getSession().setErrorHandler(errorEvent -> {
        ProcessedException processedException = exceptionHandler.process(errorEvent.getThrowable());
        Notification notification = new Notification();
        VerticalLayout content = new VerticalLayout();
        content.addClassNames("m-0", "p-0", "mt-3");
        H4 title = new H4("An error happened");
        title.addClassNames("text-white");
        content.add(title);
        content.add(new Span(processedException.getDescription()));
        content.add(new Span("Error ID: " + processedException.getUuid()));
        notification.add(content);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setDuration(10 * 1000);
        notification.open();
      });
    });
  }
}
