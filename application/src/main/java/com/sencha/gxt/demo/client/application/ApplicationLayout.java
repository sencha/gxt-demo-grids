package com.sencha.gxt.demo.client.application;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.demo.client.ClientFactory;
import com.sencha.gxt.themebuilder.base.client.config.FontDetails;
import com.sencha.gxt.themebuilder.base.client.config.ThemeDetails;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

public class ApplicationLayout implements IsWidget {

  private static Logger logger = Logger.getLogger(ApplicationLayout.class.getSimpleName());

  private ClientFactory clientFactory;

  private SimpleContainer header;

  private SimpleContainer contentPanel;

  private SimpleContainer footer;

  private VerticalLayoutContainer widget;

  public ApplicationLayout(ClientFactory clientFactory) {
    this.clientFactory = clientFactory;
  }

  public void setClientFactory(ClientFactory clientFactory) {
    this.clientFactory = clientFactory;
  }

  @Override
  public Widget asWidget() {
    if (widget == null) {
      // Be sure to inject the gss styles!
      AppBundle.INSTANCE.appStyles().ensureInjected();
      
      header = new SimpleContainer();
      header.add(createHeader());

      contentPanel = new SimpleContainer();
      
      // debugging
      //contentPanel.getElement().getStyle().setProperty("border", "1px solid red");

      footer = new SimpleContainer();
      footer.add(createFooter());

      widget = new VerticalLayoutContainer();
      widget.add(header, new VerticalLayoutData(1, 50));
      widget.add(contentPanel, new VerticalLayoutData(1, 1));
      widget.add(footer, new VerticalLayoutData(1, 75));

      // Set the content panel, for navigation
      clientFactory.getActivityManager()
          .setDisplay(contentPanel);

      // set the global font-family from the theme
      widget.getElement()
          .getStyle()
          .setProperty("fontFamily", getThemeFontFamily());
      
      
      clientFactory.getEventBus().addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
        @Override
        public void onPlaceChange(PlaceChangeEvent event) {
          logger.info("place change, forcing layout");
          
          // force layout when replacinng widgets in child
          widget.forceLayout();
        }
      });
    }

    // navigate to the current history token
    clientFactory.handleCurrentHistory();

    return widget;
  }

  /**
   * Returns the theme font family.
   * 
   * @return the theme font family
   */
  private String getThemeFontFamily() {
    ThemeDetails themeDetails = GWT.create(ThemeDetails.class);
    FontDetails fontDetails = themeDetails.panel()
        .font();
    String family = fontDetails.family();
    return family;
  }

  private Widget createHeader() {
    Anchor anchorHome = new Anchor("Home", "#home:");
    Anchor anchorGrid = new Anchor("Simple Grid", "#simplegrid:");
    Anchor anchorLiveGrid = new Anchor("Live Grid", "#livegrid:");
    Anchor anchorSplitGrid = new Anchor("Split Grid", "#splitgrid:");
    Anchor anchorLiveSplitGrid = new Anchor("Live Split Grid", "#livesplitgrid:");
    Anchor anchorLogin = new Anchor("Login", "#login:");

    BoxLayoutData layoutData = new BoxLayoutData();
    layoutData.setMargins(new Margins(0, 10, 0, 10));

    BoxLayoutData flex = new BoxLayoutData();
    flex.setFlex(1);

    HBoxLayoutContainer header = new HBoxLayoutContainer(HBoxLayoutAlign.MIDDLE);
    header.setEnableOverflow(false);
    header.add(new HTML("Company"), layoutData);
    header.add(anchorHome, layoutData);
    header.add(anchorGrid, layoutData);
    header.add(anchorLiveGrid, layoutData);
    header.add(anchorSplitGrid, layoutData);
    header.add(anchorLiveSplitGrid, layoutData);
    header.add(new SimpleContainer(), flex); // align next item to the right
    header.add(anchorLogin, layoutData);

    return header;
  }

  private Widget createFooter() {
    Anchor anchorSencha = new Anchor("Sencha.com", "http://sencha.com");
    Anchor anchorGuides = new Anchor("GXT 4.0 Guides", "http://docs-devel.sencha.com/gxt/4.x/");
    Anchor anchorSource = new Anchor("This Demo Source", "https://github.com/sencha/gxt-demo-grids");

    BoxLayoutData layoutData = new BoxLayoutData();
    layoutData.setMargins(new Margins(0, 0, 0, 10));

    BoxLayoutData flex = new BoxLayoutData();
    flex.setFlex(1);

    HBoxLayoutContainer footer = new HBoxLayoutContainer(HBoxLayoutAlign.MIDDLE);
    footer.add(anchorSencha, layoutData);
    footer.add(anchorGuides, layoutData);
    footer.add(anchorSource, layoutData);
    footer.add(new SimpleContainer(), flex);

    return footer;
  }

}
