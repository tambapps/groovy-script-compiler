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
// dir containing all files that will be packaged (should be just the fat jar)
File jpackageInputDir = new File(tempDir, "jpackage_input")
jpackageInputDir.mkdir()

GroovyCompiler compiler = new GroovyCompiler(tempDir)
GroovyDepsFetcher groovyDepsFetcher = new GroovyDepsFetcher()
Jpackage jpackage = Jpackage.newInstance()

try {
  File classFile = compiler.compile(arguments.scriptFile)[0]
  String className = Utils.nameWithExtension(classFile, '')
  File jarFile = new File(tempDir, "${className}.jar")
  try (ScriptJarOutputStream os = new ScriptJarOutputStream(jarFile, classFile)) {
    os.write()
  }

  List<File> groovyJars = groovyDepsFetcher.fetch()

  File jarWithDependencies = new File(jpackageInputDir, "${className}-with-dependencies.jar")
  try (JarMergingOutputStream os = new JarMergingOutputStream(new FileOutputStream(jarWithDependencies))) {
    os.writeJar(jarFile)
    for (groovyJar in groovyJars) {
      os.writeJar(groovyJar)
    }
    os.flush()
  }
  switch (arguments.outputType) {
    case OutputType.APP_IMAGE:
      jpackage.run(jpackageInputDir, jarWithDependencies, className)
      break
  }
} finally {
  // cleaning
  tempDir.deleteDir()
}
