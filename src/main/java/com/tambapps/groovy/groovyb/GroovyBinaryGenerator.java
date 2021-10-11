package com.tambapps.groovy.groovyb;

import java.io.File;
import java.io.IOException;

public class GroovyBinaryGenerator {

  public static void main(String[] args) throws IOException {
    GroovyCompiler compiler = new GroovyCompiler();

    File classFile = compiler.compile(new File(args[0])).get(0);
  }

}
