package com.sencha.gxt.demo.client.application.splitgrid;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.demo.client.application.Presenter;
import com.sencha.gxt.demo.client.application.splitgrid.widgets.SplitGridsWidget;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.IntegerSpinnerField;

public class SplitGridView implements IsWidget {

  private Presenter presenter;

  private VerticalLayoutContainer widget;
  private SplitGridsWidget gridWidget;
  private IntegerSpinnerField columns;
  private IntegerSpinnerField rows;
  private IntegerSpinnerField cacheSize;
  
  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }

  public void start() {
  }

  @Override
  public Widget asWidget() {
    if (widget == null) {
      HTML html = new HTML("This is a Split GXT grid with the basic GridView. This displays only renders data into the visible rows.");
      
      BoxLayoutData layoutData = new BoxLayoutData(new Margins(0, 10, 0, 10));
    
      gridWidget = new SplitGridsWidget();
      
      widget = new VerticalLayoutContainer();
      widget.add(html, new VerticalLayoutData(1, -1, new Margins(0, 10, 0, 10)));
      widget.add(gridWidget, new VerticalLayoutData(1, 1, new Margins(0, 0, 0, 0)));
    }

    return widget;
  }

}
