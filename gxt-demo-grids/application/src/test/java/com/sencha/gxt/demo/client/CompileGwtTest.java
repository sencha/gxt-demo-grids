package com.sencha.gxt.demo.client;

import com.google.gwt.junit.client.GWTTestCase;

public class CompileGwtTest extends GWTTestCase {
  @Override
  public String getModuleName() {
    return "com.sencha.gxt.demo.GxtDemoGridsProject";
  }

  public void testSandbox() {
    assertTrue(true);
  }
}
