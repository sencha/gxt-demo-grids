package com.sencha.gxt.demo.client.application.splitgrid;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class SplitGridPlace extends Place {
  
  /** 
   * I'm not really using the tokenizer here, but good for example
   */
  @Prefix("splitgrid")
  public static class Tokenizer implements PlaceTokenizer<SplitGridPlace> {

    public Tokenizer() {
    }
    
    @Override
    public String getToken(SplitGridPlace place) {
      String token = place.getToken();
      return token;
    }

    @Override
    public SplitGridPlace getPlace(String token) {
      return new SplitGridPlace(token);
    }

  }
  
  private String token;

  public SplitGridPlace(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }
  
}
