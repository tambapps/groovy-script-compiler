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
    assertEquals("Hello World", java(outputJar))

    outputJar.delete()
  }
  @Test
  void testBuildJarJson() {
    File scriptFile = getResourceFile("/HelloWorldJson.groovy")
    Groovybe.main(new String[] {scriptFile.path, '-s', 'json'})
    File outputJar = new File(Utils.CURRENT_DIRECTORY, "HelloWorldJson-with-dependencies.jar")
    assertTrue(outputJar.exists())
    assertEquals("Pierre", java(outputJar))

    outputJar.delete()
  }

  private static String java(File jarFile) {
    Process process = "$JAVA_FILE -jar $jarFile".execute()
    StringBuilder builder = new StringBuilder()
    process.consumeProcessOutput(builder, builder)
    process.waitFor()
    return builder.toString().trim()
  }

  private static File findJava() {
    File jdkFile = new File("/usr/lib/jvm").listFiles { File f -> f.isDirectory() && !f.name.startsWith('.') }[0]
    return new File(jdkFile, "/bin/java")
  }
}
