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
    File outputJar = new File(Utils.CURRENT_DIRECTORY, "HelloWorld-exec.jar")
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
    File outputJar = new File(Utils.CURRENT_DIRECTORY, "HelloWorldJson-exec.jar")
    assertTrue(outputJar.exists())
    assertEquals("Pierre", java(outputJar))

    outputJar.delete()
  }
  @Test
  void testBuildJarJsonWithAdditionalDependency() {
    File scriptFile = getResourceFile("/HelloWorldHyperPoet.groovy")
    // get hyperpoet jar from maven repository
    File additionalJarDep = new File(Utils.HOME_DIRECTORY, '/.m2/repository/com/tambapps/http/hyperpoet/1.1.0-SNAPSHOT/hyperpoet-1.1.0-SNAPSHOT.jar')
    Groovybe.main(new String[] {scriptFile.path, '-s', 'json', '-a', additionalJarDep.absolutePath})
    File outputJar = new File(Utils.CURRENT_DIRECTORY, "HelloWorldHyperPoet-exec.jar")
    assertTrue(outputJar.exists())
    assertEquals("ContentType", java(outputJar))

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
