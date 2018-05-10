package com.sencha.gxt.demo.client.application.livesplitgrid;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class LiveSplitGridPlace extends Place {
  
  /** 
   * I'm not really using the tokenizer here, but good for example
   */
  @Prefix("livesplitgrid")
  public static class Tokenizer implements PlaceTokenizer<LiveSplitGridPlace> {

    public Tokenizer() {
    }
    
    @Override
    public String getToken(LiveSplitGridPlace place) {
      String token = place.getToken();
      return token;
    }

    @Override
    public LiveSplitGridPlace getPlace(String token) {
      return new LiveSplitGridPlace(token);
    }

  }
  
  private String token;

  public LiveSplitGridPlace(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }
  
}
