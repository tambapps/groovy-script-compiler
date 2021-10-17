package com.tambapps.groovy.groovyjar.io.process

import com.tambapps.groovy.groovyjar.util.IOUtils
import com.tambapps.groovy.groovyjar.util.Utils

/**
 * Class used to execute jpackage from a system command
 */

class Jpackage {

  private static final String JPACKAGE_PATH = "/bin/jpackage"
  private final File jpackageFile

  Jpackage(File jpackageFile) {
    this.jpackageFile = jpackageFile
  }

  File run(File tempDir, File jarFile, String className, File outputDir) {
    File inputDir = makeInputDir(tempDir, jarFile)
    List<String> command = [
        jpackageFile != null ? jpackageFile.absolutePath : 'jpackage' ,
        '--input', inputDir.absolutePath,
        '--main-jar', jarFile.name,
        '--name', className,
        '--main-class', className,
        '--type', 'app-image',
        '--dest', outputDir.absolutePath
    ]
    IOUtils.runProcess(command, "jpackage terminated with an error")
    // the files are generated in a directory named $className
    return new File(outputDir, className)
  }

  static Jpackage newInstance() {
    String javaHome = System.getenv("JAVA_HOME")
    File jpackageFile
    if (javaHome) {
      jpackageFile = new File(javaHome + JPACKAGE_PATH)
      if (jpackageFile.exists()) {
        return new Jpackage(jpackageFile)
      }
    }

    jpackageFile = findFromJdksPath(new File("/usr/lib/jvm"))
    if (jpackageFile != null) {
      return new Jpackage(jpackageFile)
    }
    // find from intelIJ jdks
    jpackageFile = findFromJdksPath(new File(Utils.HOME_DIRECTORY, ".jdks"))
    if (jpackageFile != null) {
      return new Jpackage(jpackageFile)
    }

    return new Jpackage(jpackageFile)
  }

  private static File makeInputDir(File tempDir, File jarFile) {
    // dir containing all files that will be packaged (should be just the fat jar)
    File jpackageInputDir = new File(tempDir, "jpackage_input")
    jpackageInputDir.mkdir()
    jarFile.renameTo(new File(jpackageInputDir, jarFile.name))
    return jpackageInputDir
  }

  private static File findFromJdksPath(File jdksPath) {
    File[] jdks = jdksPath.listFiles { File f -> f.isDirectory() }
    for (jdk in jdks) {
      int lastDash = jdk.name.lastIndexOf('-')
      if (lastDash < 0) {
        continue
      }
      String sNumber = jdk.name.substring(lastDash + 1).takeWhile {it.isNumber() }
      if (!sNumber.isNumber()) {
        continue
      }
      int version = sNumber.toInteger()
      if (version >= 14) {
        // JDK14+ ? yay, let's still check if it has jpackage
        File jpackageFile = new File(jdk, JPACKAGE_PATH)
        if (jpackageFile.exists()) {
          return jpackageFile
        }
      }
    }
    return null
  }

}
