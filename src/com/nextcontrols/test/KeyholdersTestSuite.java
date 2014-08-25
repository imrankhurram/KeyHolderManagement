package com.nextcontrols.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class KeyholdersTestSuite {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(NewKeyholderTest.class);
    suite.addTestSuite(EditKeyholderTest.class);
    suite.addTestSuite(DeleteKeyholderTest.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
