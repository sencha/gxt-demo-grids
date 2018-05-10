package com.sencha.gxt.demo.client.application.livesplitgrid.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.demo.client.application.livesplitgrid.widgets.LiveSplitGridView.GridSide;
import com.sencha.gxt.demo.client.application.livesplitgrid.widgets.LiveSplitGridsWidget.Data;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;

public class LiveSplitGridWidget<M> implements IsWidget {

  public class GridExt<M> extends Grid<M> {

    public GridExt(ListStore<M> store, ColumnModel<M> cm, GridView<M> view) {
      super(store, cm, view);
    }

  }

  // views
  private LiveSplitGridView<M> leftGridView;
  private LiveSplitGridView<M> rightGridView;

  // grids
  private Grid<M> leftGrid;
  private Grid<M> rightGrid;
  private HorizontalLayoutContainer grids;
  private HorizontalLayoutData leftGridScrollSpacer;
  private Widget widget;

  // selection
  private ListStore<M> listStore;

  // grid configs
  private PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Data>> rightPagingLoader;
  private PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Data>> leftPagingLoader;
  private List<ColumnConfig<M, ?>> leftColumns;
  private List<ColumnConfig<M, ?>> rightColumns;

  /**
   * This is used to eliminate recursive calls when setting scroll top
   */
  private int scrollLeftRigthTrack = 0;

  public LiveSplitGridWidget(ListStore<M> listStore,
      PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Data>> leftPagingLoader,
      PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Data>> rightPagingLoader,
      List<ColumnConfig<M, ?>> leftColumns, List<ColumnConfig<M, ?>> rightColumns) {
    this.listStore = listStore;
    this.leftPagingLoader = leftPagingLoader;
    this.rightPagingLoader = rightPagingLoader;
    this.leftColumns = leftColumns;
    this.rightColumns = rightColumns;
  }

  @Override
  public Widget asWidget() {
    if (widget == null) {
      widget = initSplitGrid();
    }
    return widget;
  }

  private Widget initSplitGrid() {
    leftGridView = new LiveSplitGridView<M>(GridSide.LEFT);
    rightGridView = new LiveSplitGridView<M>(GridSide.RIGHT);
    leftGridView.setRightGridView(rightGridView);
    rightGridView.setLeftGridView(leftGridView);

    leftGrid = initLeftGrid();
    rightGrid = initRightGrid();

    String borderColorClassName = "#cccccc";

    // grid center splitter line
    FlowPanel centerSplitter = new FlowPanel();
    centerSplitter.setWidth("2px");
    centerSplitter.setHeight("100%");
    centerSplitter.getElement().addClassName(borderColorClassName);

    // grid bottom border
    FlowPanel bottomBorder = new FlowPanel();
    bottomBorder.setHeight("2px");
    bottomBorder.setWidth("100%");
    bottomBorder.getElement().addClassName(borderColorClassName);

    // left spacer for windows
    leftGridScrollSpacer = new HorizontalLayoutData(300, 1);
    leftGridScrollSpacer.setMargins(new Margins(0, 0, getLeftGridMargin(), 0));

    // horz - grids
    grids = new HorizontalLayoutContainer();
    grids.add(leftGrid, leftGridScrollSpacer);
    grids.add(centerSplitter, new HorizontalLayoutData(-1, 1));
    grids.add(rightGrid, new HorizontalLayoutData(1, 1));
    grids.addResizeHandler(new ResizeHandler() {
      @Override
      public void onResize(ResizeEvent event) {
        resizeLeftGridMargin();
      }
    });

    // blank space on the left for windows
    grids.getElement().getStyle().setBackgroundColor("#b9b9b9");

    // leftGrid.setMaskOnWidget(grids);
    // rightGrid.setMaskOnWidget(grids);

    // vert - toolbar/grid
    VerticalLayoutContainer widgetLayout = new VerticalLayoutContainer();
    widgetLayout.add(grids, new VerticalLayoutData(1, 1));
    widgetLayout.add(bottomBorder, new VerticalLayoutData(1, -1));

    // Debugging
    //widgetLayout.getElement().getStyle().setProperty("border", "1px solid red");

    // Make sure both column headers are the same height
    // leftGridView.getHeader().setHeight(28);
    // rightGridView.getHeader().setHeight(28);

    return widgetLayout;
  }

  /**
   * Determine if the left grid should have a margin based on the right grid having a scroll bar or not.
   */
  private void resizeLeftGridMargin() {
    if (!GXT.isWindows()) {
      return;
    }

    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      @Override
      public void execute() {
        leftGridScrollSpacer.setMargins(new Margins(0, 0, getLeftGridMargin(), 0));
        grids.forceLayout();
      }
    });
  }

  private int getLeftGridMargin() {
    if (!GXT.isWindows()) {
      return 0;
    }

    int margin = 0;
    XElement dataTable = getDataTable(rightGrid.getView());
    // does the right side have a scroll bar
    if (dataTable != null && rightGrid.getView().getScroller().getScrollWidth() <= dataTable.getOffsetWidth()) {
      margin = XDOM.getScrollBarWidth() - 1; // 1px fix, like a border causing it to be higher
    }

    return margin;
  }

  private native XElement getDataTable(GridView<?> view) /*-{
    return view.@com.sencha.gxt.widget.core.client.grid.GridView::dataTable;
  }-*/;

  /**
   * Left Grid
   */
  private Grid<M> initLeftGrid() {
    ColumnModel<M> leftCM = new ColumnModel<M>(getLeftColumns());

    final Grid<M> leftGrid = new GridExt<M>(listStore, leftCM, leftGridView) {
      @Override
      public void onBrowserEvent(Event ce) {
        if (ce.getTypeInt() == Event.ONSCROLL && scrollLeftRigthTrack == 0) {
          scrollLeftRigthTrack++;
          rightGrid.getView().getScroller().setScrollTop(getView().getScroller().getScrollTop());
        } else if (ce.getTypeInt() == Event.ONSCROLL) {
          scrollLeftRigthTrack = 0;
        }
        super.onBrowserEvent(ce);
      }
    };
    // leftGrid.setSelectionModel(selectionModel);
    leftGrid.setLoader(leftPagingLoader);

    // styles
    leftGrid.getView().setForceFit(true);
    leftGrid.setBorders(false);
    leftGrid.setColumnReordering(true);
    leftGrid.getView().setStripeRows(true);
    leftGrid.getView().setColumnLines(false);
    leftGrid.setLoadMask(true);

    return leftGrid;
  }

  protected void clearSelection() {
    // selectionModel.deselectAll();
  }

  /**
   * Right Grid
   */
  private Grid<M> initRightGrid() {
    ColumnModel<M> rightCM = new ColumnModel<M>(getRightColumns());

    Grid<M> rightGrid = new GridExt<M>(listStore, rightCM, rightGridView) {
      @Override
      public void onBrowserEvent(Event ce) {
        if (ce.getTypeInt() == Event.ONSCROLL && scrollLeftRigthTrack == 0) {
          scrollLeftRigthTrack++;
          leftGrid.getView().getScroller().setScrollTop(getView().getScroller().getScrollTop());
        } else if (ce.getTypeInt() == Event.ONSCROLL) {
          scrollLeftRigthTrack = 0;
        }
        super.onBrowserEvent(ce);
      }
    };
    // rightGrid.setSelectionModelRight(selectionModel);
    rightGrid.setLoader(rightPagingLoader);

    // grid styles
    rightGrid.setBorders(false);
    rightGrid.setColumnReordering(true);
    rightGrid.getView().setStripeRows(true);
    rightGrid.getView().setColumnLines(false);
    rightGrid.setLoadMask(true);

    return rightGrid;
  }

  /**
   * Get left columns but include the checkbox column
   */
  private List<ColumnConfig<M, ?>> getLeftColumns() {
    List<ColumnConfig<M, ?>> columns = new ArrayList<>();

    // dev columns
    columns.addAll(leftColumns);

    return columns;
  }

  private List<ColumnConfig<M, ?>> getRightColumns() {
    return rightColumns;
  }

  public ListStore<M> getStore() {
    return listStore;
  }

  // public LiveSplitGridView<M> getLeftView() {
  // return leftGridView;
  // }
  //
  // public LiveSplitGridView<M> getRightView() {
  // return rightGridView;
  // }
  //
  // public void setEmptyText(String emptyText) {
  // leftGridView.setEmptyText("");
  // rightGridView.setEmptyText(emptyText);
  // }
  //
  // public void refresh(boolean headerToo) {
  // leftGridView.refresh(headerToo);
  // rightGridView.refresh(headerToo);
  //
  // clearSelection();
  // }

  public Grid<M> getLeftGrid() {
    return leftGrid;
  }

  public Grid<M> getRightGrid() {
    return rightGrid;
  }

  public void reconfigure(List<ColumnConfig<M, ?>> columnsLeft, List<ColumnConfig<M, ?>> columnsRight) {
    this.leftColumns = columnsLeft;
    this.rightColumns = columnsRight;

    ColumnModel<M> leftCM = new ColumnModel<M>(getLeftColumns());
    ColumnModel<M> rightCM = new ColumnModel<M>(getRightColumns());

    leftGrid.reconfigure(listStore, leftCM);
    rightGrid.reconfigure(listStore, rightCM);

    resizeLeftGridMargin();
  }

}