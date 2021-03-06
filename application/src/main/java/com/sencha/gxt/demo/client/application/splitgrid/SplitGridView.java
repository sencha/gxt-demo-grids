package com.sencha.gxt.demo.client.application.splitgrid;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.demo.client.application.Presenter;
import com.sencha.gxt.demo.client.application.splitgrid.widgets.SplitGridsWidget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

public class SplitGridView implements IsWidget {

  private Presenter presenter;

  private VerticalLayoutContainer widget;
  private SplitGridsWidget gridWidget;
  
  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }

  public void start() {
  }

  @Override
  public Widget asWidget() {
    if (widget == null) {
      HTML html = new HTML("This is a split grid or locking with the basic GridView. This displays only renders data into the visible rows.");
      
      gridWidget = new SplitGridsWidget();
      
      widget = new VerticalLayoutContainer();
      widget.add(html, new VerticalLayoutData(1, -1, new Margins(0, 10, 0, 10)));
      widget.add(gridWidget, new VerticalLayoutData(1, 1, new Margins(0, 0, 0, 0)));
    }

    return widget;
  }

}
