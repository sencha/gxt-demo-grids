package com.sencha.gxt.demo.client.application.grid;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class GridPlace extends Place {
  
  /** 
   * I'm not really using the tokenizer here, but good for example
   */
  @Prefix("grid")
  public static class Tokenizer implements PlaceTokenizer<GridPlace> {

    public Tokenizer() {
    }
    
    @Override
    public String getToken(GridPlace place) {
      String token = place.getToken();
      return token;
    }

    @Override
    public GridPlace getPlace(String token) {
      return new GridPlace(token);
    }

  }
  
  private String token;

  public GridPlace(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }
  
}
