package com.sencha.gxt.demo.client.application.home;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.demo.client.application.Presenter;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

public class HomeView implements IsWidget {

  private Presenter presenter;

  private VerticalLayoutContainer widget;

  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }

  public void start() {

  }

  @Override
  public Widget asWidget() {
    if (widget == null) {
      widget = new VerticalLayoutContainer();

      widget.add(new HTML("Home"), new VerticalLayoutData(1, 1, new Margins(10)));
      
      // debugging
      //widget.getElement().getStyle().setProperty("border", "1px solid blue");
    }

    return widget;
  }

}
