package com.tambapps.groovy.groovybe

import groovy.transform.CompileStatic
import lombok.AllArgsConstructor
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.tools.FileSystemCompiler

@CompileStatic
@AllArgsConstructor
class GroovyCompiler {

  private static final File CURRENT_DIRECTORY = new File("." + File.separator)
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
        new File(CURRENT_DIRECTORY, Utils.nameWithExtension(it, ".class"))
      }
    } catch (Exception e) {
      throw new IOException(e)
    }
  }
}
