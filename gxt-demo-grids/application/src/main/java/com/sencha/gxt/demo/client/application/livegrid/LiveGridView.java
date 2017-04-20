package com.sencha.gxt.demo.client.application.livegrid;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.demo.client.application.Presenter;
import com.sencha.gxt.demo.client.application.livegrid.widgets.LiveGridViewWidget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.IntegerSpinnerField;
import com.sencha.gxt.widget.core.client.grid.LiveToolItem;
import com.sencha.gxt.widget.core.client.toolbar.LabelToolItem;

public class LiveGridView implements IsWidget {

  private Presenter presenter;

  private VerticalLayoutContainer widget;
  private LiveGridViewWidget gridWidget;
  private IntegerSpinnerField columns;
  private IntegerSpinnerField rows;

  private LiveToolItem pagingBar;

  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }

  public void start() {
  }

  @Override
  public Widget asWidget() {
    if (widget == null) {
      HTML html = new HTML("This is a GXT grid with the LiveGridView. This displays only renders data into the visible rows.");
      
      columns = new IntegerSpinnerField();
      columns.setValue(25);
      
      rows = new IntegerSpinnerField();
      rows.setValue(5000);
      
      BoxLayoutData layoutData = new BoxLayoutData(new Margins(0, 10, 0, 10));
      
      TextButton updateButton = new TextButton("Update");
      updateButton.addSelectHandler(new SelectHandler() { 
        @Override
        public void onSelect(SelectEvent event) {
          udpateColsRows();
        }
      });
      
      HBoxLayoutContainer fields = new HBoxLayoutContainer(HBoxLayoutAlign.MIDDLE);
      fields.setEnableOverflow(false);
      fields.add(new LabelToolItem("Number of: Columns:"), layoutData);
      fields.add(columns, layoutData);
      fields.add(new LabelToolItem("Rows:"), layoutData);
      fields.add(rows, layoutData);
      fields.add(updateButton, layoutData);
      
      gridWidget = new LiveGridViewWidget();
      gridWidget.setColRowSize(columns.getCurrentValue(), rows.getCurrentValue());

      widget = new VerticalLayoutContainer();
      widget.add(html, new VerticalLayoutData(1, -1, new Margins(0, 10, 0, 10)));
      widget.add(fields, new VerticalLayoutData(1, -1, new Margins(10, 0, 10, 0)));
      widget.add(gridWidget, new VerticalLayoutData(1, 1, new Margins(0, 0, 0, 0)));
      
      pagingBar = new LiveToolItem(gridWidget.getGrid());
      widget.add(pagingBar, new VerticalLayoutData(1, -1, new Margins(0, 0, 0, 0)));
    }

    return widget;
  }

  protected void udpateColsRows() {
    gridWidget.updateColRowSize(columns.getCurrentValue(), rows.getCurrentValue());
  }

}
