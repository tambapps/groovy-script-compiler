package com.tambapps.groovy.groovybe

import groovy.transform.CompileStatic

@CompileStatic
class Groovybe {

  static void main(String[] args) throws IOException {
    GroovyCompiler compiler = new GroovyCompiler()

    File classFile = compiler.compile(new File(args[0])).get(0)
    File jarFile = new File(classFile.parent, Utils.nameWithExtension(classFile, ".jar"))
    try (ScriptJarOutputStream os = new ScriptJarOutputStream(jarFile, classFile)) {
      os.write()
    }
    // TODO
  }

}
