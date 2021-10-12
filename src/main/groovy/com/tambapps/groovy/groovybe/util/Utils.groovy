package com.tambapps.groovy.groovybe.util

import groovy.transform.CompileStatic

@CompileStatic
class Utils {

  static String nameWithExtension(File file, String extension) {
    return nameWithExtension(file.getName(), extension)
  }

  static String nameWithExtension(String fileName, String extension) {
    return fileName.substring(0, fileName.indexOf('.')) + extension
  }
}
