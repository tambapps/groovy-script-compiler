package com.tambapps.groovy.groovybe

import com.tambapps.groovy.groovybe.io.GroovyCompiler
import com.tambapps.groovy.groovybe.util.Utils

import static TestUtils.getResourceFile
import static org.junit.jupiter.api.Assertions.assertTrue

import org.junit.jupiter.api.Test

class GroovyCompilerTest {

  @Test
  void test() throws Exception {
    GroovyCompiler compiler = new GroovyCompiler(Utils.CURRENT_DIRECTORY, [])
    File file = getResourceFile("/HelloWorld.groovy")

    File compiledFile = compiler.compile(file)
    assertTrue(compiledFile.exists())
    assertTrue(compiledFile.delete())
  }
}
