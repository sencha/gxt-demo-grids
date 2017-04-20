package com.sencha.gxt.demo.client.application.livegrid;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import com.sencha.gxt.demo.client.ClientFactory;

public class LiveGridActivity extends AbstractActivity implements LiveGridPresenter {

  private LiveGridView view;
  private ClientFactory clientFactory;
  private boolean running;

  public LiveGridActivity(LiveGridPlace place, ClientFactory clientFactory) {
    this.clientFactory = clientFactory;
  }

  /**
   * Invoked by the ActivityManager to start a new Activity
   */
  @Override
  public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
    if (view == null) {
      view = new LiveGridView();
    }

    view.setPresenter(this);
    containerWidget.setWidget(view.asWidget());
    view.start();
  }

  @Override
  public String mayStop() {
    String s = null;
    if (running == true) {
      s = "Please hold on. This activity is stopping.";
    }
    
    return s;
  }

  @Override
  public void goTo(Place place) {
    clientFactory.getPlaceController().goTo(place);
  }

  @Override
  public void setRunning(boolean running) {
    this.running = running;
  }

  @Override
  public ClientFactory getClientFactory() {
    return clientFactory;
  }

}
