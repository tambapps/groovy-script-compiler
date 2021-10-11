package com.tambapps.groovy.groovybe

import static org.junit.jupiter.api.Assertions.assertTrue

import org.junit.jupiter.api.Test

class GroovyCompilerTest {

  @Test
  void test() throws Exception {
    GroovyCompiler compiler = new GroovyCompiler()
    File file = new File(GroovyCompilerTest.class.getResource("/HelloWorld.groovy").toURI())

    List<File> compiledFiles = compiler.compile(file)
    System.out.println(compiledFiles)
    for (File compiledFile : compiledFiles) {
      assertTrue(compiledFile.exists())
      assertTrue(compiledFile.delete())
    }
  }
}
