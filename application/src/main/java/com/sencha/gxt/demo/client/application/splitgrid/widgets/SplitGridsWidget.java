package com.sencha.gxt.demo.client.application.splitgrid.widgets;

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
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.demo.client.application.splitgrid.widgets.SplitGridView.GridSide;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;

public class SplitGridsWidget extends Composite {

  private static final int COLUMNS_SIZE = 20;
  private static final int TOTAL_LENGTH = 5000;

  public SplitGridsWidget() {
    ListStore<Data> listStore = getStore();
    final PagingLoader<PagingLoadConfig, PagingLoadResult<Data>> pagingLoader = getLoader(listStore);
    pagingLoader.setLimit(200);

    List<ColumnConfig<Data, ?>> leftColumns = getColumns(GridSide.LEFT, 0, 3);
    List<ColumnConfig<Data, ?>> rightColumns = getColumns(GridSide.RIGHT, 3, COLUMNS_SIZE);

    SplitGrid<Data> splitGrid = new SplitGrid<Data>(listStore, pagingLoader, leftColumns, rightColumns);

    PagingToolBar pagingBar = new PagingToolBar(50);
    pagingBar.bind(pagingLoader);

    VerticalLayoutContainer vlc = new VerticalLayoutContainer();
    vlc.add(splitGrid, new VerticalLayoutData(1, 1));
    vlc.add(pagingBar, new VerticalLayoutData(1, -1));

    initWidget(vlc);

    // Initially load the data
    addAttachHandler(new Handler() {
      @Override
      public void onAttachOrDetach(AttachEvent event) {
        pagingLoader.load();
      }
    });
  }

  /**
   * Columns
   */
  private List<ColumnConfig<Data, ?>> getColumns(GridSide gridSide, int start, int limit) {
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

  /**
   * The model loader.
   * 
   * TODO don't load on left side
   */
  private PagingLoader<PagingLoadConfig, PagingLoadResult<Data>> getLoader(ListStore<Data> listStore) {
    RpcProxy<PagingLoadConfig, PagingLoadResult<Data>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<Data>>() {
      @Override
      public void load(final PagingLoadConfig loadConfig, final AsyncCallback<PagingLoadResult<Data>> callback) {
        GWT.log("pause loading");

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

    PagingLoader<PagingLoadConfig, PagingLoadResult<Data>> loader = new PagingLoader<PagingLoadConfig, PagingLoadResult<Data>>(
        proxy);
    loader.useLoadConfig(new PagingLoadConfigBean());
    loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, Data, PagingLoadResult<Data>>(listStore));
    loader.setRemoteSort(true);

    return loader;
  }

  private ListStore<Data> getStore() {
    ListStore<Data> store = new ListStore<Data>(new ModelKeyProvider<Data>() {
      @Override
      public String getKey(Data item) {
        return item.getKey();
      }
    });
    return store;
  }

  // Return some pretend data
  private void getDatas(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<Data>> callback) {
    final int offset = loadConfig.getOffset();
    int limit = loadConfig.getLimit();

    GWT.log("getDatas: offset=" + offset + " limit=" + limit);

    final List<Data> datas = new ArrayList<Data>();
    for (int i = offset; i < offset + limit; i++) {
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
        return TOTAL_LENGTH;
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

    String[] values = new String[COLUMNS_SIZE];
    for (int col = 0; col < COLUMNS_SIZE; col++) {
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

  public class StringFilterExt extends StringFilter<Data> {
    public StringFilterExt(int index) {
      super(new ValueProviderExt(index));
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

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((key == null) ? 0 : key.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Data other = (Data) obj;
      if (key == null) {
        if (other.key != null)
          return false;
      } else if (!key.equals(other.key))
        return false;
      return true;
    }

  }

}
