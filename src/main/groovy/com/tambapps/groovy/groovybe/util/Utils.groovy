package com.tambapps.groovy.groovybe.util

import groovy.text.SimpleTemplateEngine
import groovy.transform.CompileStatic

@CompileStatic
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

  /**
   * Applies a Script template to the script text and return the resolved template. This method
   * is useful when generating NATIVE_BINARY output.
   * By default groovyc generate a groovy.lang.Script with two constructors, and native-image don't
   * know which to call since no constructor is specified explicitely when instantiating the Script.
   * To avoid this problem, I transform a groovy script into a Script class (and a psvm function) myself,
   * and call explicitly a constructor. That is why this function is useful
   *
   * @param scriptText
   * @param className the name of the class
   * @param scriptText the script content
   * @return the applied template
   */
  static String applyScriptTemplate(String className, String scriptText) {
    def engine = new SimpleTemplateEngine()
    return engine.createTemplate(Utils.class.getResource('/Script.groovytemplate'))
        .make([ className: className, scriptText: scriptText ])
        .toString()
  }
}
