package com.tambapps.groovy.groovyjar

class TestUtils {

  static File getResourceFile(String path) {
    return new File(TestUtils.class.getResource(path).toURI())
  }
}
