package com.tambapps.groovy.groovybe

import com.tambapps.groovy.groovybe.io.stream.ScriptJarOutputStream

import static TestUtils.getResourceFile
import static org.junit.jupiter.api.Assertions.assertTrue

import org.junit.jupiter.api.Test

class JarGeneratorTest {

  @Test
  void test() throws Exception {
    File classFile = getResourceFile("/HelloWorld.class")
    File outputFile = new File("." + File.separator + "HelloWorld.jar")
    try (ScriptJarOutputStream os = new ScriptJarOutputStream(outputFile, classFile)) {
      os.write()
    }

    assertTrue(outputFile.exists())
    assertTrue(outputFile.delete())
  }
}
