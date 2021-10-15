package com.tambapps.groovy.groovybe.io

import com.tambapps.maven.dependency.resolver.data.Artifact

import java.util.regex.Matcher

/**
 * Class reading source file to skip @Grab lines in the output. Dependency jar(s) will be
 * included instead, to allow a faster runtime
 * TODO document that
 */
class SourceDependencyGrabber {

  private static final GRAB_PATTERN = ~/@Grab\(['"]([\w-.:]+)['"]\)/
  List<Artifact> grabbedArtifacts = []

  String transform(String text) {
    return transform(text.readLines())
  }

  String transform(File sourceFile) {
   return transform(sourceFile.readLines())
  }

  String transform(List<String> lines) {
    return lines.findAll(this.&detectGrabLine).join('\n')
  }

  /**
   * Check if the line is a grab line and parse it if it is one
   * @param line the line
   * @return true if the line was NOT a grab line
   */
  private boolean detectGrabLine(String line) {
    Matcher matcher = line =~ GRAB_PATTERN
    if (!matcher) {
      return true
    }
    grabbedArtifacts.add(Artifact.from(matcher.group(1)))
    return false
  }
}
