package com.tambapps.groovy.groovyb;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.io.File;

public class JarGeneratorTest {

  @Test
  public void test() throws Exception {
    File classFile = new File(GroovyCompilerTest.class.getResource("/HelloWorld.class").toURI());
    File outputFile = new File("." + File.separator + "HelloWorld.jar");
    try (ScriptJarOutputStream os = new ScriptJarOutputStream(outputFile, classFile)) {
      os.write();
    }

    assertTrue(outputFile.exists());
  }
}
