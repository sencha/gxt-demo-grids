package com.sencha.gxt.demo.client.application.simplegrid;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.demo.client.application.Presenter;
import com.sencha.gxt.demo.client.application.simplegrid.widgets.SimpleGridViewWidget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.IntegerSpinnerField;
import com.sencha.gxt.widget.core.client.toolbar.LabelToolItem;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;

public class SimpleGridView implements IsWidget {

  private Presenter presenter;

  private VerticalLayoutContainer widget;
  private SimpleGridViewWidget gridWidget;
  private IntegerSpinnerField columns;
  private IntegerSpinnerField rows;
  private IntegerSpinnerField pageSize;

  private PagingToolBar pagingBar;

  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }

  public void start() {
  }

  @Override
  public Widget asWidget() {
    if (widget == null) {
      HTML html = new HTML("This is a GXT grid with the default GridView. This view loads and renders the paging size.");

      columns = new IntegerSpinnerField();
      columns.setValue(25);

      rows = new IntegerSpinnerField();
      rows.setValue(5000);
      
      pageSize = new IntegerSpinnerField();
      pageSize.setValue(200);

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
      fields.add(new LabelToolItem("Page Size:"), layoutData);
      fields.add(pageSize, layoutData);
      fields.add(updateButton, layoutData);

      gridWidget = new SimpleGridViewWidget();
      gridWidget.setColRowSize(columns.getCurrentValue(), rows.getCurrentValue());

      pagingBar = new PagingToolBar(pageSize.getCurrentValue());

      widget = new VerticalLayoutContainer();
      widget.add(html, new VerticalLayoutData(1, -1, new Margins(0, 10, 0, 10)));
      widget.add(fields, new VerticalLayoutData(1, -1, new Margins(10, 0, 10, 0)));
      widget.add(gridWidget, new VerticalLayoutData(1, 1, new Margins(0, 0, 0, 0)));
      widget.add(pagingBar, new VerticalLayoutData(1, -1, new Margins(0, 0, 0, 0)));
      
      pagingBar.bind(gridWidget.getPaingLoader());
    }

    return widget;
  }

  protected void udpateColsRows() {
    pagingBar.setPageSize(pageSize.getCurrentValue());
    pagingBar.bind(gridWidget.getPaingLoader());
    
    gridWidget.updateColRowSize(columns.getCurrentValue(), rows.getCurrentValue());
  }

}
