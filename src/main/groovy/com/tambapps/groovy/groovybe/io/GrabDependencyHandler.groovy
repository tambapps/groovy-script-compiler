package com.tambapps.groovy.groovybe.io

/**
 * Class reading source file to skip @Grab lines in the output. Dependency jar(s) will be
 * included instead, to allow a faster runtime
 * TODO document that
 */
class GrabDependencyHandler {

  String transform(File file) {
    List<String> lines = file.readLines()
    // TODO parse Grab lines and fetch files
    return lines.join('\n')
  }
}
