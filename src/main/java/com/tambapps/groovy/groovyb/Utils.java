package com.tambapps.groovy.groovyb;

import java.io.File;

public class Utils {

  public static String nameWithExtension(File file, String extension) {
    return nameWithExtension(file.getName(), extension);
  }

  public static String nameWithExtension(String fileName, String extension) {
    return fileName.substring(0, fileName.indexOf('.')) + extension;
  }
}
