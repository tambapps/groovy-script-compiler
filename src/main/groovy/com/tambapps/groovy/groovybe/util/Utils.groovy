package com.tambapps.groovy.groovybe.util

class Utils {

  static boolean debug = false

  static final File CURRENT_DIRECTORY = new File("." + File.separator)
  static final File HOME_DIRECTORY = new File(System.getProperty("user.home"))

  static String nameWithExtension(File file, String extension) {
    return nameWithExtension(file.getName(), extension)
  }

  static String nameWithExtension(String fileName, String extension) {
    return fileName.substring(0, fileName.indexOf('.')) + extension
  }

  static List<String> getEnumPossibleValues(Class aClass) {
    return Arrays.asList(aClass.enumConstants).collect {it.toString().toLowerCase().replaceAll("_", "-") }
  }
}
