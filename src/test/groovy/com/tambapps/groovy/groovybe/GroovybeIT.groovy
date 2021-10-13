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
  void testBuildAppimage() {
    File scriptFile = getResourceFile("/HelloWorld.groovy")
    Groovybe.main(new String[] {scriptFile.path, '-t', 'appimage'})
    File outputDir = new File(Utils.CURRENT_DIRECTORY, "HelloWorld")
    assertTrue(outputDir.exists())
    File executableFile = new File(outputDir, "/bin/HelloWorld")
    assertEquals("Hello World", execute(executableFile.absolutePath))

    outputDir.deleteDir()
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
    return execute("$JAVA_FILE -jar $jarFile")
  }

  private static String execute(String command) {
    Process process = command.execute()
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
