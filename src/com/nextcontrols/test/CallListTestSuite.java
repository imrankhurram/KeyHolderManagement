package com.nextcontrols.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class CallListTestSuite {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(NewCallListTest.class);
    suite.addTestSuite(EditCallListTest.class);
    suite.addTestSuite(DeleteCallListTest.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
