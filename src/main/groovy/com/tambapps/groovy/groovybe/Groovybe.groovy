package com.tambapps.groovy.groovybe

import com.tambapps.groovy.groovybe.arguments.Arguments
import com.tambapps.groovy.groovybe.arguments.OutputType
import com.tambapps.groovy.groovybe.io.GroovyCompiler
import com.tambapps.groovy.groovybe.io.GroovyDepsFetcher
import com.tambapps.groovy.groovybe.io.Jpackage
import com.tambapps.groovy.groovybe.io.stream.JarMergingOutputStream
import com.tambapps.groovy.groovybe.io.stream.ScriptJarOutputStream
import com.tambapps.groovy.groovybe.util.Utils

Arguments arguments = Arguments.parseArgs(args)
if (!arguments) {
  return
}

File tempDir = File.createTempDir('groovybe')


try {
  GroovyCompiler compiler = new GroovyCompiler(tempDir)
  GroovyDepsFetcher groovyDepsFetcher = new GroovyDepsFetcher()
  File classFile = compiler.compile(arguments.scriptFile)[0]
  String className = Utils.nameWithExtension(classFile, '')
  File jarFile = new File(tempDir, "${className}.jar")
  try (ScriptJarOutputStream os = new ScriptJarOutputStream(jarFile, classFile)) {
    os.write()
  }

  List<File> groovyJars = groovyDepsFetcher.fetch()

  File jarWithDependencies = new File(tempDir, "${className}-with-dependencies.jar")
  try (JarMergingOutputStream os = new JarMergingOutputStream(new FileOutputStream(jarWithDependencies))) {
    os.writeJar(jarFile)
    for (groovyJar in groovyJars) {
      os.writeJar(groovyJar)
    }
    os.flush()
  }
  switch (arguments.outputType) {
    case OutputType.JAR:
      jarWithDependencies.renameTo(new File(Utils.CURRENT_DIRECTORY, jarWithDependencies.name))
      break
    case OutputType.APPIMAGE:
      Jpackage jpackage = Jpackage.newInstance()
      // dir containing all files that will be packaged (should be just the fat jar)
      File jpackageInputDir = new File(tempDir, "jpackage_input")
      jpackageInputDir.mkdir()
      jarWithDependencies.renameTo(new File(jpackageInputDir, jarWithDependencies.name))
      jpackage.run(jpackageInputDir, jarWithDependencies, className)
      break
  }
} finally {
  // cleaning
  tempDir.deleteDir()
}
