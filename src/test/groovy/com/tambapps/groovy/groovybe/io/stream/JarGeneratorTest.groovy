package com.tambapps.groovy.groovybe.io.stream

import com.tambapps.groovy.groovybe.TestUtils

import static org.junit.jupiter.api.Assertions.assertTrue

import org.junit.jupiter.api.Test

class JarGeneratorTest {

  @Test
  void test() throws Exception {
    File classFile = TestUtils.getResourceFile("/HelloWorld.class")
    File outputFile = new File("." + File.separator + "HelloWorld.jar")
    try (ScriptJarOutputStream os = new ScriptJarOutputStream(outputFile, classFile)) {
      os.write()
    }

    assertTrue(outputFile.exists())
    assertTrue(outputFile.delete())
  }
}
