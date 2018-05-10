package com.sencha.gxt.demo.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.demo.client.application.ApplicationLayout;
import com.sencha.gxt.widget.core.client.container.Viewport;

public class GxtDemoGridsProjectEntryPoint implements EntryPoint {

  private ClientFactory clientFactory;

  @Override
  public void onModuleLoad() {
    clientFactory = new ClientFactory();

    Viewport viewport = new Viewport();
    viewport.add(new ApplicationLayout(clientFactory));

    RootPanel.get().add(viewport);
  }

}
