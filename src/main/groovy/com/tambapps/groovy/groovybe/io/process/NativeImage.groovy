package com.tambapps.groovy.groovybe.io.process

import com.tambapps.groovy.groovybe.util.IOUtils
import com.tambapps.groovy.groovybe.util.Utils

/**
 * Class used to execute native-image (graalvm) from a system command
 */
class NativeImage {

  File nativeImageFile

  NativeImage(File nativeImageFile) {
    this.nativeImageFile = nativeImageFile
  }

  File run(File jarFile, String className, File outputDir) {
    List<String> command = [
        nativeImageFile != null ? nativeImageFile.absolutePath : 'native-image',
        '-jar', jarFile.absolutePath,
        '--no-fallback',
        '--allow-incomplete-classpath',
        '--report-unsupported-elements-at-runtime',
        '--initialize-at-build-time',
        '--initialize-at-run-time=org.codehaus.groovy.control.XStreamUtils,groovy.grape.GrapeIvy',
        '--no-server', // may be deprecated?,
        className
    ]

    IOUtils.runProcess(command, "native-image terminated with an error")
    File generatedFile = new File(Utils.CURRENT_DIRECTORY, className)
    File outputFile = new File(outputDir, className)
    if (generatedFile != outputFile) {
      generatedFile.renameTo(outputFile)
    }
    new File(Utils.CURRENT_DIRECTORY, className + ".build_artifacts.txt").delete()
    return outputFile
  }
}
