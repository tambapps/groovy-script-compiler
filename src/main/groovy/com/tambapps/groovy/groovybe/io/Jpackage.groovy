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

  void run(File inputDir, File jarFile, String className, File outputDir) {
    List<String> command = [
        jpackageFile.absolutePath,
        '--input', inputDir.absolutePath,
        '--main-jar', jarFile.name,
        '--name', className,
        '--main-class', className,
        '--type', 'app-image',
        '--dest', outputDir.absolutePath
    ]

    Process process = command.join(' ').execute()
    def out = new StringBuilder()
    process.consumeProcessOutput(out, out)
    process.waitFor()
    println(out)
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

    throw new JpackageNotFoundException()
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
