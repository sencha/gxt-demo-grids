package com.sencha.gxt.demo.client.application.livesplitgrid.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.demo.client.application.splitgrid.widgets.SplitGridView.GridSide;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.LiveToolItem;

public class LiveSplitGridsWidget implements IsWidget {

  private static final int COLUMNS_SIZE = 20;
  private static final int TOTAL_LENGTH = 5000;

  private int columnsSize = COLUMNS_SIZE;
  private int rowsSize = TOTAL_LENGTH;

  private VerticalLayoutContainer widget;

  @Override
  public Widget asWidget() {
    if (widget == null) {
      ListStore<Data> listStore = createStore();

      PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Data>> leftPagingLoader = createLoader(listStore);
      PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Data>> rightPagingLoader = createLoader(listStore);

      List<ColumnConfig<Data, ?>> leftColumns = createColumns(GridSide.LEFT, 0, 3);
      List<ColumnConfig<Data, ?>> rightColumns = createColumns(GridSide.RIGHT, 3, COLUMNS_SIZE);

      LiveSplitGridWidget<Data> splitGrid = new LiveSplitGridWidget<Data>(listStore, leftPagingLoader,
          rightPagingLoader, leftColumns, rightColumns);

      LiveToolItem pagingBar = new LiveToolItem(splitGrid.getRightGrid());

      widget = new VerticalLayoutContainer();
      widget.add(splitGrid, new VerticalLayoutData(1, 1));
      widget.add(pagingBar, new VerticalLayoutData(1, -1));

      // Initially load the data, after it's attached
      widget.addAttachHandler(new Handler() {
        @Override
        public void onAttachOrDetach(AttachEvent event) {
          // After isWidget is called configure the live tool item display
          pagingBar.bindGrid(splitGrid.getRightGrid());

          // load the data after it's attached
          // initially load the cache size
          rightPagingLoader.load(0, 200);
        }
      });
    }

    return widget;
  }

  public void setColRowSize(int columnsSize, int rowsSize) {
    this.columnsSize = columnsSize;
    this.rowsSize = rowsSize;
  }

  private int getColumnsSize() {
    return columnsSize;
  }

  private int getRowsSize() {
    return rowsSize;
  }

  /**
   * Columns
   */
  private List<ColumnConfig<Data, ?>> createColumns(GridSide gridSide, int start, int limit) {
    List<ColumnConfig<Data, ?>> columns = new ArrayList<ColumnConfig<Data, ?>>();

    // TODO move to ClientBundle
    // TODO account for older browser transparency/opacity
    StyleInjector.inject(".headBlack { background-color: black; } .colGray { background-color: rgba(0, 0, 0, 0.1); }");

    for (int c = start; c < limit; c++) {
      String header = "COL" + c;

      SafeHtml safeHtml = SafeHtmlUtils.fromSafeConstant(header);
      ColumnConfig<Data, String> col = new ColumnConfig<Data, String>(new ValueProviderExt(c), 70);
      col.setHeader(safeHtml);

      // Align Column Content
      col.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
      col.setHorizontalHeaderAlignment(HasHorizontalAlignment.ALIGN_LEFT);

      // apply custom styling
      if (c == 3) {
        col.setColumnHeaderClassName("headBlack");
        col.setColumnTextClassName("colGray");
      }

      columns.add(col);
    }
    return columns;
  }

  private PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Data>> createLoader(ListStore<Data> store) {
    RpcProxy<FilterPagingLoadConfig, PagingLoadResult<Data>> proxy = new RpcProxy<FilterPagingLoadConfig, PagingLoadResult<Data>>() {
      @Override
      public void load(FilterPagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<Data>> callback) {
        // Emulate slower loading, pause for half a second and then return some data.
        Timer t = new Timer() {
          @Override
          public void run() {
            GWT.log("load() " + loadConfig.getOffset() + " " + loadConfig.getLimit() + " Loading...");
            getDatas(loadConfig, callback);
          }
        };
        t.schedule(500);
      }
    };

    PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Data>> loader = new PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Data>>(
        proxy);
    loader.useLoadConfig(new FilterPagingLoadConfigBean());
    loader.addLoadHandler(new LoadResultListStoreBinding<FilterPagingLoadConfig, Data, PagingLoadResult<Data>>(store));
    loader.setRemoteSort(true);

    return loader;
  }

  private ListStore<Data> createStore() {
    ListStore<Data> store = new ListStore<Data>(new ModelKeyProvider<Data>() {
      @Override
      public String getKey(Data item) {
        return item.getKey();
      }
    });
    return store;
  }

  private void getDatas(FilterPagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<Data>> callback) {
    final int offset = loadConfig.getOffset();
    int limit = loadConfig.getLimit();
    int end = offset + limit;

    final List<Data> datas = new ArrayList<Data>();
    for (int i = offset; i < end; i++) {
      datas.add(getData(i));
    }

    PagingLoadResult<Data> result = new PagingLoadResult<Data>() {
      @Override
      public List<Data> getData() {
        return datas;
      }

      @Override
      public void setTotalLength(int totalLength) {
      }

      @Override
      public void setOffset(int offset) {
      }

      @Override
      public int getTotalLength() {
        return getRowsSize();
      }

      @Override
      public int getOffset() {
        return offset;
      }
    };
    callback.onSuccess(result);
  }

  private Data getData(int row) {
    String key = "key" + row;

    String[] values = new String[getColumnsSize()];
    for (int col = 0; col < getColumnsSize(); col++) {
      values[col] = "" + col + "," + row;
    }

    Data data = new Data(key, values);
    return data;
  }

  public class ValueProviderExt implements ValueProvider<Data, String> {
    private int index;

    public ValueProviderExt(int index) {
      this.index = index;
    }

    @Override
    public String getValue(Data data) {
      return data.getValue(index);
    }

    @Override
    public void setValue(Data object, String value) {
    }

    @Override
    public String getPath() {
      return "path" + index;
    }
  }

  public class Data {
    private String key;
    private String[] values;

    public Data(String key, String[] values) {
      this.key = key;
      this.values = values;
    }

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public String getValue(int index) {
      return values[index];
    }

    public void setValue(int index, String value) {
      this.values[index] = value;
    }

    @Override
    public String toString() {
      String s = "Data(";
      s += "key=" + key;
      s += ")";
      return s;
    }
  }

}
