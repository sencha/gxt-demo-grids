package com.sencha.gxt.demo.client.activity;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.sencha.gxt.demo.client.ClientFactory;
import com.sencha.gxt.demo.client.application.home.HomeActivity;
import com.sencha.gxt.demo.client.application.home.HomePlace;
import com.sencha.gxt.demo.client.application.livegrid.LiveGridActivity;
import com.sencha.gxt.demo.client.application.livegrid.LiveGridPlace;
import com.sencha.gxt.demo.client.application.login.LoginActivity;
import com.sencha.gxt.demo.client.application.login.LoginPlace;
import com.sencha.gxt.demo.client.application.simplegrid.SimpleGridActivity;
import com.sencha.gxt.demo.client.application.simplegrid.SimpleGridPlace;

public class ApplicationActivityMapper implements ActivityMapper {
  
  private ClientFactory clientFactory;

  /**
   * AppActivityMapper associates each Place with its corresponding
   * {@link Activity}
   * 
   * @param clientFactory Factory to be passed to activities
   * @param walleteditview 
   * @param walletlistview 
   * @param signinview 
   */
  public ApplicationActivityMapper(ClientFactory clientFactory) {
    super();
    
    this.clientFactory = clientFactory;
  }
  
  /**
   * Map each Place to its corresponding Activity. 
   */
  @Override
  public Activity getActivity(Place place) {
    Activity activity = null;
    
    if (place instanceof HomePlace) {
      activity = new HomeActivity((HomePlace) place, clientFactory);
      
    } else if (place instanceof LoginPlace) {
      activity = new LoginActivity((LoginPlace) place, clientFactory);
      
    } else if (place instanceof SimpleGridPlace) {
      activity = new SimpleGridActivity((SimpleGridPlace) place, clientFactory);
          
    } else if (place instanceof LiveGridPlace) {
      activity = new LiveGridActivity((LiveGridPlace) place, clientFactory);
    }
    
    return activity;
  }

}
