package com.example.application.views;

import java.util.Optional;
import com.example.application.components.appnav.AppNav;
import com.example.application.components.appnav.AppNavItem;
import com.example.application.data.entity.User;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.template.EmailTemplatesView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

  private H1 viewTitle;

  private AuthenticatedUser authenticatedUser;
  private AccessAnnotationChecker accessChecker;

  public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
    this.authenticatedUser = authenticatedUser;
    this.accessChecker = accessChecker;

    setPrimarySection(Section.DRAWER);
    addToNavbar(true, createHeaderContent());
    addToDrawer(createDrawerContent());
  }

  private Component createHeaderContent() {
    DrawerToggle toggle = new DrawerToggle();
    toggle.addClassNames("view-toggle");
    toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
    toggle.getElement().setAttribute("aria-label", "Menu toggle");

    viewTitle = new H1();
    viewTitle.addClassNames("view-title");

    Header header = new Header(toggle, viewTitle);
    header.addClassNames("view-header");
    return header;
  }

  private Component createDrawerContent() {
    H2 appName = new H2("Email Template Manager");
    appName.addClassNames("app-name");

    com.vaadin.flow.component.html.Section section =
        new com.vaadin.flow.component.html.Section(appName, createNavigation(), createFooter());
    section.addClassNames("drawer-section");
    return section;
  }

  private AppNav createNavigation() {
    // AppNav is not yet an official component.
    // For documentation, visit https://github.com/vaadin/vcf-nav#readme
    AppNav nav = new AppNav();
    nav.addClassNames("app-nav");

    if (accessChecker.hasAccess(EmailTemplatesView.class)) {
      nav.addItem(new AppNavItem("Email Templates", EmailTemplatesView.class, "la la-columns"));
    }

    return nav;
  }

  private Footer createFooter() {
    Footer layout = new Footer();
    layout.addClassNames("app-nav-footer");

    Optional<User> maybeUser = authenticatedUser.get();
    if (maybeUser.isPresent()) {
      User user = maybeUser.get();

      Avatar avatar = new Avatar(user.getName(), user.getProfilePictureUrl());
      avatar.addClassNames("me-xs");

      ContextMenu userMenu = new ContextMenu(avatar);
      userMenu.setOpenOnClick(true);
      userMenu.addItem("Logout", e -> {
        authenticatedUser.logout();
      });

      Span name = new Span(user.getName());
      name.addClassNames("font-medium", "text-s", "text-secondary");

      layout.add(avatar, name);
    } else {
      Anchor loginLink = new Anchor("login", "Sign in");
      layout.add(loginLink);
    }

    return layout;
  }

  @Override
  protected void afterNavigation() {
    super.afterNavigation();
    viewTitle.setText(getCurrentPageTitle());
  }

  private String getCurrentPageTitle() {
    PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
    return title == null ? "" : title.value();
  }
}
