package com.tambapps.groovy.groovyb;

import lombok.AllArgsConstructor;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.tools.FileSystemCompiler;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GroovyCompiler {

  // this is the class used by groovyc
  private final FileSystemCompiler compiler;

  public GroovyCompiler() throws IOException {
    FileSystemCompiler.CompilationOptions options = new FileSystemCompiler.CompilationOptions();
    CompilerConfiguration configuration = options.toCompilerConfiguration();
    this.compiler = new FileSystemCompiler(configuration, null);
  }

  public List<File> compile(File... files) throws IOException {
    try {
      compiler.compile(files);
      return Arrays.stream(files).map(
          f -> new File(new File("." + File.separator), Utils.nameWithExtension(f, ".class")))
          .collect(Collectors.toList());
    } catch (Exception e) {
      throw new IOException(e);
    }
  }
}
