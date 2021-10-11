package com.tambapps.groovy.groovyb

import groovy.transform.CompileStatic

@CompileStatic
class GroovyBinaryGenerator {

  static void main(String[] args) throws IOException {
    GroovyCompiler compiler = new GroovyCompiler()

    File classFile = compiler.compile(new File(args[0])).get(0)
    // TODO
  }

}
