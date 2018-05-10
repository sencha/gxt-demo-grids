package com.sencha.gxt.demo.client.application.livesplitgrid.widgets;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Region;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.demo.client.application.AppBundle;
import com.sencha.gxt.fx.client.DragEndEvent;
import com.sencha.gxt.fx.client.DragStartEvent;
import com.sencha.gxt.widget.core.client.ComponentHelper;
import com.sencha.gxt.widget.core.client.event.XEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnHeader;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.LiveGridView;
import com.sencha.gxt.widget.core.client.menu.Menu;

/**
 * GridView overrides
 */
public class LiveSplitGridView<M> extends LiveGridView<M> {

  public class ColumnHeaderExt<M> extends ColumnHeader<M> {
    public ColumnHeaderExt(Grid<M> container, ColumnModel<M> cm) {
      super(container, cm);
    }

    @Override
    public void setEnableColumnResizing(boolean enable) {
      if (bar == null && enable) {
        bar = new GridSplitBar() {
          @Override
          protected void onDragStart(DragStartEvent e) {
            drag(true, "1px solid black", 1, 1);

            getElement().getStyle().setCursor(Cursor.DEFAULT);

            startX = e.getX();

            int cols = cm.getColumnCount();
            for (int i = 0, len = cols; i < len; i++) {
              if (cm.isHidden(i) || !cm.isResizable(i))
                continue;
              Element hd = getHead(i).getElement();
              if (hd != null) {
                Element hdParent = hd.getParentElement(); // Workaround for bad width when menu is not present
                Region rr = XElement.as(hdParent).getRegion();
                // GWT.log("rr=" + rr + " startX=" + startX + " right=" + (rr.getRight() - 5));
                if (startX > rr.getRight() - 5 && startX < rr.getRight() + 5) {
                  colIndex = heads.indexOf(getHead(i));
                  // GWT.log("~~~~~~DRAG colIndex=" + colIndex);
                  if (colIndex != -1)
                    break;
                }
              }
            }
            // GWT.log("~~~~~~DRAG colIndex=" + colIndex);
            if (colIndex > -1) {
              Element c = getHead(colIndex).getElement();
              int x = startX;
              int minx = x - XElement.as(c).getX() - minColumnWidth;
              int maxx = (XElement.as(container.getElement()).getX()
                  + XElement.as(container.getElement()).getWidth(false))
                  - e.getNativeEvent().<XEvent>cast().getXY().getX();
              d.setXConstraint(minx, maxx);
            }
          }

          @Override
          protected void onDragEnd(DragEndEvent e) {
            drag(false, "none", 0, splitterWidth);

            int endX = e.getX();
            int diff = endX - startX;

            int width = Math.max(getMinColumnWidth(), cm.getColumnWidth(colIndex) + diff);
            cm.setUserResized(true);
            cm.setColumnWidth(colIndex, width);
          }
        };
        container.getElement().appendChild(bar.getElement());
        if (isAttached()) {
          ComponentHelper.doAttach(bar);
        }
        bar.show();
      } else if (bar != null && !enable) {
        ComponentHelper.doDetach(bar);
        bar.getElement().removeFromParent();
        bar = null;
      }
    }
  }

  /**
   * Designates left or right grid
   */
  public enum GridSide {
    LEFT, RIGHT
  }

  // private HashSet<M> selected;
  private GridSide gridSide;
  private LiveSplitGridView<M> leftGridView;
  private LiveSplitGridView<M> rightGridView;

  public LiveSplitGridView(GridSide gridSide) {
    this.gridSide = gridSide;

    vbar = false;
    if (gridSide == GridSide.LEFT) {
      // fix white space by decrementing 1
      scrollOffset = -1;
    }
  }

  public void setLeftGridView(LiveSplitGridView<M> leftGridView) {
    this.leftGridView = leftGridView;
    this.rightGridView = this;
  }

  public void setRightGridView(LiveSplitGridView<M> rightGridView) {
    this.rightGridView = rightGridView;
    this.leftGridView = this;
  }

  @Override
  public void onRowSelect(final int rowIndex) {
    super.onRowSelect(rowIndex);

    if (gridSide == GridSide.LEFT) {
      // ~~~ workaround, there is something that is causing the issue before this frame.
      // TODO This causes a small flicker, but brings it into a working state - do to changing views and reselect
      Scheduler.get().scheduleDeferred(new ScheduledCommand() {
        @Override
        public void execute() {
          rightGridView.onRowSelectSync(rowIndex);
        }
      });
    } else if (gridSide == GridSide.RIGHT) {
      leftGridView.onRowSelectSync(rowIndex);
    }
  }

  @Override
  public void onRowDeselect(int rowIndex) {
    super.onRowDeselect(rowIndex);

    if (gridSide == GridSide.LEFT) {
      rightGridView.onRowDeselectSync(rowIndex);
    } else if (gridSide == GridSide.RIGHT) {
      leftGridView.onRowDeselectSync(rowIndex);
    }
  }

  @Override
  public void onRowOver(Element row) {
    super.onRowOver(row);

    int rowIndex = findRowIndex(row);
    if (rowIndex < 0) {
      return;
    }

    if (gridSide == GridSide.LEFT) {
      Element rightRow = rightGridView.getRow(rowIndex);
      rightGridView.onRowOverSync(rightRow);
    } else if (gridSide == GridSide.RIGHT) {
      Element leftRow = leftGridView.getRow(rowIndex);
      leftGridView.onRowOverSync(leftRow);
    }
  }

  @Override
  public void onRowOut(Element row) {
    super.onRowOut(row);

    int rowIndex = findRowIndex(row);
    if (rowIndex < 0) {
      return;
    }

    if (gridSide == GridSide.LEFT) {
      Element rightRow = rightGridView.getRow(rowIndex);
      rightGridView.onRowOutSync(rightRow);
    } else if (gridSide == GridSide.RIGHT) {
      Element leftRow = leftGridView.getRow(rowIndex);
      leftGridView.onRowOutSync(leftRow);
    }
  }

  private void onRowSelectSync(int rowIndex) {
    super.onRowSelect(rowIndex);
  }

  private void onRowOverSync(Element row) {
    if (row == null) {
      return;
    }
    super.onRowOver(row);
  }

  private void onRowOutSync(Element row) {
    if (row == null) {
      return;
    }
    super.onRowOut(row);
  }

  private void onRowDeselectSync(int rowIndex) {
    super.onRowDeselect(rowIndex);
  }

  @Override
  protected void doSort(int colIndex, SortDir sortDir) {
    // Left delegate to the right Grid loader
    if (gridSide == GridSide.LEFT) {
      ColumnConfig<M, ?> column = cm.getColumn(colIndex);
      ValueProvider<? super M, ?> vp = column.getValueProvider();
      SortInfoBean bean = new SortInfoBean(vp, sortDir);
      if (sortDir == null && sortState != null && vp.getPath().equals(sortState.getSortField())) {
        bean.setSortDir(sortState.getSortDir() == SortDir.ASC ? SortDir.DESC : SortDir.ASC);
      } else if (sortDir == null) {
        bean.setSortDir(SortDir.ASC);
      }

      grid.getLoader().clearSortInfo();
      grid.getLoader().addSortInfo(bean);
      // maskView();
    } else if (gridSide == GridSide.RIGHT) {
      super.doSort(colIndex, sortDir);
    }
  }

  @Override
  protected void initHeader() {
    if (header == null) {
      header = new ColumnHeaderExt<M>(grid, cm) {
        @Override
        protected Menu getContextMenu(int column) {
          return super.getContextMenu(column);
        }
      };
    }
    // ~~~ workaround for setting context menu upstream
    // Use the defined factory in forensics grid
    // header.setMenuFactory(new HeaderContextMenuFactory() {
    // @Override
    // public Menu getMenuForColumn(int columnIndex) {
    // return createContextMenu(columnIndex);
    // }
    // });
    header.setSplitterWidth(splitterWidth);
  }

  @Override
  protected void initElements() {
    super.initElements();

    // hide scroll bar
    if (gridSide == GridSide.LEFT) {
      scroller.addClassName(AppBundle.INSTANCE.appStyles().hideScrollbar());
    }
  }

  /**
   * changed visibility
   */
  @Override
  public void onNoNext(int index) {
    super.onNoNext(index);
  }

  /**
   * changed visibility
   */
  @Override
  protected void onNoPrev() {
    super.onNoPrev();
  }

  /**
   * changed visbility
   */
  @Override
  protected void onHighlightRow(int rowIndex, boolean highlight) {
    super.onHighlightRow(rowIndex, highlight);
  }

  @Override
  public int getVisibleRowCount() {
    return super.getVisibleRowCount();
  }

  @Override
  protected int getLiveScrollerHeight() {
    // Subtract the 1, which is half of the last item
    // This fixes the tearing
    return super.getLiveScrollerHeight() - rowHeight;
  }
}