package com.tambapps.groovy.groovybe.io

import com.tambapps.groovy.groovybe.exception.JpackageNotFoundException
import com.tambapps.groovy.groovybe.util.Utils

/**
 * Class used to execute jpackage from a system command
 */

class Jpackage {

  private static final String JPACKAGE_PATH = "/bin/jpackage"
  private final File jpackageFile

  Jpackage(File jpackageFile) {
    this.jpackageFile = jpackageFile
  }

  // TODO make function to run jpackage

  static Jpackage newInstance() {
    String javaHome = System.getenv("JAVA_HOME")
    File jpackageFile
    if (javaHome) {
      jpackageFile = new File(javaHome + JPACKAGE_PATH)
      if (jpackageFile.exists()) {
        return new Jpackage(jpackageFile)
      }
    }
    jpackageFile = findFromIntelIjJdksPlusHome()
    if (jpackageFile != null) {
      return new Jpackage(jpackageFile)
    }

    throw new JpackageNotFoundException()
  }

  private static File findFromIntelIjJdksPlusHome() {
    File jdksPath = new File(Utils.HOME_DIRECTORY, ".jdks")

    File[] jdks = jdksPath.listFiles()
    for (jdk in jdks) {
      int lastDash = jdk.name.lastIndexOf('-')
      int version = jdk.name.substring(lastDash + 1).takeWhile {it.isNumber() }.toInteger()
      if (version >= 14) {
        // JDK14+ ? yay, let's see check if it has jpackage
        File jpackageFile = new File(jdk, JPACKAGE_PATH)
        if (jpackageFile.exists()) {
          return jpackageFile
        }
      }
    }
    return null
  }
}
