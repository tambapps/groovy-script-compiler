package com.tambapps.groovy.groovybe.arguments

enum OutputType {
  JAR,
  // requires jpackage
  APPIMAGE,
  // requires native-image (graalvm)
  NATIVE_BINARY
}
