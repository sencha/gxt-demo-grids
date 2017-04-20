package com.sencha.gxt.demo.client.application.livegrid;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class LiveGridPlace extends Place {
  
  /** 
   * I'm not really using the tokenizer here, but good for example
   */
  @Prefix("livegrid")
  public static class Tokenizer implements PlaceTokenizer<LiveGridPlace> {

    public Tokenizer() {
    }
    
    @Override
    public String getToken(LiveGridPlace place) {
      String token = place.getToken();
      return token;
    }

    @Override
    public LiveGridPlace getPlace(String token) {
      return new LiveGridPlace(token);
    }

  }
  
  private String token;

  public LiveGridPlace(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }
  
}
