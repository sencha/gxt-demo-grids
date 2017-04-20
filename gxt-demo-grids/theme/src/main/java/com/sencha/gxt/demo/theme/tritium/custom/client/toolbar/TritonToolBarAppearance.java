package com.sencha.gxt.demo.theme.tritium.custom.client.toolbar;

import com.google.gwt.core.client.GWT;

import com.sencha.gxt.demo.theme.tritium.client.base.toolbar.Css3ToolBarAppearance;

public class TritonToolBarAppearance extends Css3ToolBarAppearance {

  public interface TritonToolBarResources extends Css3ToolBarResources {

    @Override
    @Source({"com/sencha/gxt/theme/base/client/container/BoxLayout.gss",
            "com/sencha/gxt/demo/theme/tritium/client/base/container/Css3HBoxLayoutContainer.gss",
            "com/sencha/gxt/demo/theme/tritium/client/base/toolbar/Css3ToolBar.gss", 
            "TritonToolBar.gss"})
    TritonToolBarStyle style();
  }

  public interface TritonToolBarStyle extends Css3ToolBarStyle {
  }

  public TritonToolBarAppearance() {
    super(GWT.<TritonToolBarResources> create(TritonToolBarResources.class));
  }
}
