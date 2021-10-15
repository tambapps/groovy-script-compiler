package com.tambapps.groovy.groovybe.io

import com.tambapps.groovy.groovybe.TestUtils
import com.tambapps.groovy.groovybe.util.Utils

import static org.junit.jupiter.api.Assertions.assertTrue

import org.junit.jupiter.api.Test

class GroovyCompilerTest {

  @Test
  void test() throws Exception {
    GroovyCompiler compiler = new GroovyCompiler(Utils.CURRENT_DIRECTORY, [])
    File file = TestUtils.getResourceFile("/HelloWorld.groovy")

    File compiledFile = compiler.compile(file)
    assertTrue(compiledFile.exists())
    assertTrue(compiledFile.delete())
  }
}