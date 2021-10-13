package com.tambapps.groovy.groovybe

import com.tambapps.groovy.groovybe.util.Utils
import org.junit.jupiter.api.Test

import static TestUtils.getResourceFile
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertTrue

class GroovybeIT {

  private static final File JAVA_FILE = findJava()

  @Test
  void testBuildJar() {
    File scriptFile = getResourceFile("/HelloWorld.groovy")
    Groovybe.main(new String[] {scriptFile.path})
    File outputJar = new File(Utils.CURRENT_DIRECTORY, "HelloWorld-with-dependencies.jar")
    assertTrue(outputJar.exists())
    Process process = "$JAVA_FILE -jar $outputJar".execute()
    StringBuilder builder = new StringBuilder()
    process.consumeProcessOutput(builder, builder)
    process.waitFor()
    assertEquals("Hello World", builder.toString())

    outputJar.delete()
  }
  @Test
  void testBuildJarJson() {
    File scriptFile = getResourceFile("/HelloWorldJson.groovy")
    Groovybe.main(new String[] {scriptFile.path, '-s', 'json'})
    File outputJar = new File(Utils.CURRENT_DIRECTORY, "HelloWorldJson-with-dependencies.jar")
    assertTrue(outputJar.exists())
    Process process = "$JAVA_FILE -jar $outputJar".execute()
    StringBuilder builder = new StringBuilder()
    process.consumeProcessOutput(builder, builder)
    process.waitFor()
    assertEquals("Pierre", builder.toString())

    outputJar.delete()
  }

  private static File findJava() {
    File jdkFile = new File("/usr/lib/jvm").listFiles { File f -> f.isDirectory() && !f.name.startsWith('.') }[0]
    return new File(jdkFile, "/bin/java")
  }
}
