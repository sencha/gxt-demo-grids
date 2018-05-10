package com.sencha.gxt.demo.client.application.livesplitgrid;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.demo.client.application.Presenter;
import com.sencha.gxt.demo.client.application.livesplitgrid.widgets.LiveSplitGridsWidget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

public class LiveSplitGridView implements IsWidget {

  private Presenter presenter;

  private VerticalLayoutContainer widget;
  private LiveSplitGridsWidget gridWidget;
  
  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }

  public void start() {
  }

  @Override
  public Widget asWidget() {
    if (widget == null) {
      HTML html = new HTML("This is a split or locking grid with the LiveGridView. This displays only renders data into the visible rows.");
      
      gridWidget = new LiveSplitGridsWidget();
      
      widget = new VerticalLayoutContainer();
      widget.add(html, new VerticalLayoutData(1, -1, new Margins(0, 10, 0, 10)));
      widget.add(gridWidget, new VerticalLayoutData(1, 1, new Margins(0, 0, 0, 0)));
    }

    return widget;
  }

}
