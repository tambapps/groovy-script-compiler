package com.tambapps.groovy.groovybe.io

import com.tambapps.groovy.groovybe.util.Utils
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.tools.FileSystemCompiler

class GroovyCompiler {

  // this is the class used by groovyc
  private final FileSystemCompiler compiler
  private final File targetDir

  GroovyCompiler(File targetDir, List<File> deps) throws IOException {
    this.targetDir = targetDir
    CompilerConfiguration configuration = new FileSystemCompiler.CompilationOptions().tap {
      // private properties, but groovy magic do the extra-work for me
      it.targetDir = targetDir
      printStack = true
      classpath = deps.collect { it.absolutePath }.join(File.pathSeparator)
    }.toCompilerConfiguration()
    this.compiler = new FileSystemCompiler(configuration, null)
  }

  File compile(File scriptFile) throws IOException {
    try {
      compiler.compile(scriptFile)
      return new File(targetDir, Utils.nameWithExtension(scriptFile, ".class"))
    } catch (Exception e) {
      throw new IOException(e)
    }
  }
}
