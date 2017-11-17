package com.sencha.gxt.demo.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.sencha.gxt.demo.client.activity.ApplicationActivityMapper;
import com.sencha.gxt.demo.client.activity.ApplicationPlaceHistoryMapper;
import com.sencha.gxt.demo.client.application.home.HomePlace;
import com.sencha.gxt.demo.client.application.livegrid.LiveGridPlace;
import com.sencha.gxt.demo.client.application.login.LoginPlace;
import com.sencha.gxt.demo.client.application.simplegrid.SimpleGridPlace;

public class ClientFactory {

  /**
   * default start point in application - if nothing is in url
   */
  private Place defaultPlace = new HomePlace("");

  /**
   * for app's global events
   */
  private static final EventBus eventBus = new SimpleEventBus();

  /**
   * place's directory
   */
  private final ActivityMapper activityMapper = new ApplicationActivityMapper(this);

  /**
   * place's director (internal flagger|flagman for places)
   */
  private final ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);

  /**
   * URL tokenizer directory
   */
  private final ApplicationPlaceHistoryMapper historyMapper = GWT.create(ApplicationPlaceHistoryMapper.class);

  /**
   * URL tokenizer's director (URL event flagger|flagman)
   */
  private final PlaceHistoryHandler placeHistoryHandler;

  /**
   * app place's director (internal flagger|flagman) (not deprecated if your
   * seeing it)
   */
  private final PlaceController placeController = new PlaceController(eventBus);

  public ClientFactory() {
    // tell the historyMapper there are tokenizers (below) to use in here.
    historyMapper.setFactory(this);

    placeHistoryHandler = new PlaceHistoryHandler(historyMapper);
    placeHistoryHandler.register(placeController, eventBus, defaultPlace);
  }

  /**
   * Goes to the place represented on URL else default place.
   */
  public void handleCurrentHistory() {
    placeHistoryHandler.handleCurrentHistory();
  }
  
  public void getToken(Place place) {
    historyMapper.getToken(place);
  }

  public EventBus getEventBus() {
    return eventBus;
  }

  public PlaceController getPlaceController() {
    return placeController;
  }

  public ActivityManager getActivityManager() {
    return activityManager;
  }

  /***
   * Used by the HistoryMapper { @link ApplicationPlaceHistoryMapper } Picked up
   * by the historyMapper when needed. this way, I can send in objects like the
   * requestFactory into them and use it otherwise, the HistoryMapper runs
   * decoupled with this here we can push objects into it.
   */
  public HomePlace.Tokenizer getHomeTokenizer() {
    return new HomePlace.Tokenizer();
  }

  public LoginPlace.Tokenizer getLogInTokenizer() {
    return new LoginPlace.Tokenizer();
  }
  
  public SimpleGridPlace.Tokenizer getGridTokenizer() {
    return new SimpleGridPlace.Tokenizer();
  }
  
  public LiveGridPlace.Tokenizer getLiveGridTokenizer() {
    return new LiveGridPlace.Tokenizer();
  }

}
