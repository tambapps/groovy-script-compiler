package com.tambapps.groovy.groovyjar.io

import com.tambapps.groovy.groovyjar.io.compiler.CustomAntlr4PluginFactory

import com.tambapps.groovy.groovyjar.util.Utils
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.tools.FileSystemCompiler

class GroovyCompiler {

  // this is the class used by groovyc
  private final FileSystemCompiler compiler
  private final File targetDir

  GroovyCompiler(File targetDir, List<File> deps) throws IOException {
    this(targetDir, deps, false)
  }
  GroovyCompiler(File targetDir, List<File> deps, boolean compileStatic) throws IOException {
    this.targetDir = targetDir
    CompilerConfiguration configuration = new FileSystemCompiler.CompilationOptions().tap {
      // private properties, but groovy magic do the extra-work for me
      it.targetDir = targetDir
      printStack = true
      classpath = deps.collect { it.absolutePath }.join(File.pathSeparator)
      it.compileStatic = compileStatic
    }.toCompilerConfiguration()
    // hack for groovy native-image. Whole explanation at CustomModuleNode
    configuration.pluginFactory = new CustomAntlr4PluginFactory()
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
