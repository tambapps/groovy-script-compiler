package com.tambapps.groovy.groovybe

class TestUtils {

  static File getResourceFile(String path) {
    return new File(TestUtils.class.getResource(path).toURI())
  }
}
