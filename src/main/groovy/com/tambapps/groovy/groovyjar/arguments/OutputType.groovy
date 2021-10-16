package com.tambapps.groovy.groovyjar.arguments

enum OutputType {
  JAR,
  // requires jpackage
  APPIMAGE,
  // requires native-image (graalvm)
  NATIVE_BINARY
}
