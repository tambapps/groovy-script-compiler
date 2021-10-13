package com.tambapps.groovy.groovybe

import com.tambapps.groovy.groovybe.io.GroovyCompiler
import com.tambapps.groovy.groovybe.util.Utils

import static org.junit.jupiter.api.Assertions.assertTrue

import org.junit.jupiter.api.Test

class GroovyCompilerTest {

  @Test
  void test() throws Exception {
    GroovyCompiler compiler = new GroovyCompiler(Utils.CURRENT_DIRECTORY)
    File file = new File(GroovyCompilerTest.class.getResource("/HelloWorld.groovy").toURI())

    List<File> compiledFiles = compiler.compile(file)
    System.out.println(compiledFiles)
    for (File compiledFile : compiledFiles) {
      assertTrue(compiledFile.exists())
      assertTrue(compiledFile.delete())
    }
  }
}
