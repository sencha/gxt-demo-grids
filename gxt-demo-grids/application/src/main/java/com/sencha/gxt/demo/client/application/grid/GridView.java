package com.sencha.gxt.demo.client.application.grid;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.demo.client.application.Presenter;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

public class GridView implements IsWidget {

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

      widget.add(new HTML("Grid 1"), new VerticalLayoutData(1, 1));
    }

    return widget;
  }

}
