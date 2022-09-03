package com.example.application.components.html;

import java.util.function.Function;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Style;

public class HtmlPresenter extends Div {

  private static final String emptyHtml = "<div></div>";
  private static final String errorHtml = "<div>Error</div>";
  private static final String wrapperHtml = "<div>%s</div>";
  private final Function<Style, Style> styleSetter;

  public HtmlPresenter() {
    this(e -> e.set("overflow-y", "auto"));
  }

  public HtmlPresenter(Function<Style, Style> styleSetter) {
    this.styleSetter = styleSetter;
    add(newHtml(emptyHtml));
  }

  public boolean setValue(String content) {
    removeAll();
    try {
      add(newHtml(String.format(wrapperHtml, content)));
      return true;
    } catch (Exception ex) {
      add(newHtml(errorHtml));
      return false;
    }
  }

  private Html newHtml(String content) {
    Html html = new Html(content);
    styleSetter.apply(html.getElement().getStyle());
    html.getElement().getStyle();
    return html;
  }

}
