package com.tambapps.groovy.groovybe.io

import com.tambapps.groovy.groovybe.util.Utils
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.tools.FileSystemCompiler

class GroovyCompiler {

  // this is the class used by groovyc
  private final FileSystemCompiler compiler
  private final File targetDir

  GroovyCompiler(File targetDir) throws IOException {
    this.targetDir = targetDir
    FileSystemCompiler.CompilationOptions options = new FileSystemCompiler.CompilationOptions()
    // private property, but groovy magic do the extra-work for me
    options.targetDir = targetDir
    options.printStack = true
    CompilerConfiguration configuration = options.toCompilerConfiguration()
    this.compiler = new FileSystemCompiler(configuration, null)
  }

  List<File> compile(File... files) throws IOException {
    try {
      compiler.compile(files)
      return files.collect {
        new File(targetDir, Utils.nameWithExtension(it, ".class"))
      }
    } catch (Exception e) {
      throw new IOException(e)
    }
  }
}
