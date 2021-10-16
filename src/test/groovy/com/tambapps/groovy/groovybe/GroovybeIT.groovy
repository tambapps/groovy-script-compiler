package com.tambapps.groovy.groovybe

import com.tambapps.groovy.groovybe.util.Utils
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import static TestUtils.getResourceFile
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertTrue

class GroovybeIT {

  private static final File JAVA_FILE = findJava()

  // can also be a directory
  File outputFile

  @AfterEach
  void clean() {
    if (outputFile != null && outputFile.exists()) {
      if (outputFile.directory) {
        outputFile.deleteDir()
      } else {
        outputFile.delete()
      }
    }
    outputFile = null
  }

  @Test
  void testBuildJar() {
    File scriptFile = getResourceFile("/HelloWorld.groovy")
    Groovybe.main(new String[] {scriptFile.path})
    outputFile = new File(Utils.CURRENT_DIRECTORY, "HelloWorld-exec.jar")
    assertTrue(outputFile.exists())
    assertEquals("Hello World", java(outputFile))
  }

  @Test
  void testBuildAppimage() {
    File scriptFile = getResourceFile("/HelloWorld.groovy")
    Groovybe.main(new String[] {scriptFile.path, '-t', 'appimage'})
    outputFile = new File(Utils.CURRENT_DIRECTORY, "HelloWorld")
    assertTrue(outputFile.exists())
    File executableFile = new File(outputFile, "/bin/HelloWorld")
    assertEquals("Hello World", execute(executableFile.absolutePath))
  }

  @Test
  void testBuildNativeBinary() {
    File scriptFile = getResourceFile("/HelloWorld.groovy")
    Groovybe.main(new String[] {scriptFile.path, '-t', 'native-binary'})
    outputFile = new File(Utils.CURRENT_DIRECTORY, "HelloWorld")
    assertTrue(outputFile.exists())
    assertTrue(outputFile.canExecute())
    assertEquals("Hello World", execute(outputFile.absolutePath))
  }

  @Test
  void testBuildJarJson() {
    File scriptFile = getResourceFile("/HelloWorldJson.groovy")
    Groovybe.main(new String[] {scriptFile.path, '-s', 'json'})
    outputFile = new File(Utils.CURRENT_DIRECTORY, "HelloWorldJson-exec.jar")
    assertTrue(outputFile.exists())
    assertEquals("Pierre", java(outputFile))
  }

  @Test
  void testBuildJarGrab() {
    File scriptFile = getResourceFile("/HelloWorldGrab.groovy")
    Groovybe.main(new String[] {scriptFile.path})
    outputFile = new File(Utils.CURRENT_DIRECTORY, "HelloWorldGrab-exec.jar")
    assertTrue(outputFile.exists())
    assertEquals("JdbcTemplate", java(outputFile))
  }

  @Test
  void testBuildJarJsonWithAdditionalDependency() {
    File scriptFile = getResourceFile("/HelloWorldHyperPoet.groovy")
    // get hyperpoet jar from maven repository
    File additionalJarDep = new File(Utils.HOME_DIRECTORY, '/.m2/repository/com/tambapps/http/hyperpoet/1.1.0-SNAPSHOT/hyperpoet-1.1.0-SNAPSHOT.jar')
    Groovybe.main(new String[] {scriptFile.path, '-s', 'json', '-a', additionalJarDep.absolutePath})
    outputFile = new File(Utils.CURRENT_DIRECTORY, "HelloWorldHyperPoet-exec.jar")
    assertTrue(outputFile.exists())
    assertEquals("ContentType", java(outputFile))
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
