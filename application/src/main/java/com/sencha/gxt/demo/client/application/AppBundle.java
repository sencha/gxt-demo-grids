package com.sencha.gxt.demo.client.application;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface AppBundle extends ClientBundle {

  public static AppBundle INSTANCE = GWT.create(AppBundle.class);

  // css selectors
  interface AppStyles extends CssResource {
    String hideScrollbar();
  }

  // access to css selectors
  @Source("app_styles.gss")
  AppStyles appStyles();

}
