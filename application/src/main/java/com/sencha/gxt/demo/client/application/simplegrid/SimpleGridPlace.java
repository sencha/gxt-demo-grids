package com.sencha.gxt.demo.client.application.simplegrid;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class SimpleGridPlace extends Place {
  
  /** 
   * I'm not really using the tokenizer here, but good for example
   */
  @Prefix("simplegrid")
  public static class Tokenizer implements PlaceTokenizer<SimpleGridPlace> {

    public Tokenizer() {
    }
    
    @Override
    public String getToken(SimpleGridPlace place) {
      String token = place.getToken();
      return token;
    }

    @Override
    public SimpleGridPlace getPlace(String token) {
      return new SimpleGridPlace(token);
    }

  }
  
  private String token;

  public SimpleGridPlace(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }
  
}
