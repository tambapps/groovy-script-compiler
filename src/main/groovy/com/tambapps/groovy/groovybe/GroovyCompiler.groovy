package com.tambapps.groovy.groovybe

import com.tambapps.groovy.groovybe.util.Utils
import groovy.transform.CompileStatic
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.tools.FileSystemCompiler

@CompileStatic
class GroovyCompiler {

  // this is the class used by groovyc
  private final FileSystemCompiler compiler

  GroovyCompiler() throws IOException {
    FileSystemCompiler.CompilationOptions options = new FileSystemCompiler.CompilationOptions()
    CompilerConfiguration configuration = options.toCompilerConfiguration()
    this.compiler = new FileSystemCompiler(configuration, null)
  }

  List<File> compile(File... files) throws IOException {
    try {
      compiler.compile(files)
      return files.collect {
        new File(Utils.CURRENT_DIRECTORY, Utils.nameWithExtension(it, ".class"))
      }
    } catch (Exception e) {
      throw new IOException(e)
    }
  }
}
