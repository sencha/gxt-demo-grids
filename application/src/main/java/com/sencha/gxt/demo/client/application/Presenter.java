package com.sencha.gxt.demo.client.application;

import com.google.gwt.place.shared.Place;

import com.sencha.gxt.demo.client.ClientFactory;

public interface Presenter {
  
  /**
   * Navigate to a new Place in the browser
   */
  void goTo(Place place);
  
  /**
   * Setting the activity as running, then if navigation event occurs a warning dialog will occur
   */
  void setRunning(boolean running);
  
  ClientFactory getClientFactory();
  
}
