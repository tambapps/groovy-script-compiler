package com.tambapps.groovy.groovybe

import com.tambapps.groovy.groovybe.arguments.Arguments
import com.tambapps.groovy.groovybe.arguments.OutputType
import com.tambapps.groovy.groovybe.io.SourceDependencyGrabber
import com.tambapps.groovy.groovybe.io.GroovyCompiler
import com.tambapps.groovy.groovybe.io.GroovyDepsFetcher
import com.tambapps.groovy.groovybe.io.Jpackage
import com.tambapps.groovy.groovybe.io.stream.JarMergingOutputStream
import com.tambapps.groovy.groovybe.util.Utils

Arguments arguments = Arguments.parseArgs(args)
if (!arguments) {
  return
}

File tempDir = File.createTempDir('groovybe')

try {
  GroovyDepsFetcher groovyDepsFetcher = new GroovyDepsFetcher()
  SourceDependencyGrabber sourceDependencyGrabber = new SourceDependencyGrabber()

  // extract @Grab artifacts if any
  File transformedScriptFile = new File(tempDir, arguments.scriptFile.name)
  transformedScriptFile.text = sourceDependencyGrabber.transform(arguments.scriptFile.readLines())

  // Fetch dependencies. They will constitute the classpath used for compilation
  List<File> dependencyJars =
      groovyDepsFetcher.fetch(arguments.version, arguments.subProjects,
          sourceDependencyGrabber.grabbedArtifacts) + arguments.additionalJars

  // compile class
  GroovyCompiler compiler = new GroovyCompiler(tempDir, dependencyJars)
  File classFile = compiler.compile(transformedScriptFile)
  String className = Utils.nameWithExtension(classFile, '')

  // compile executable jar
  File jarWithDependencies = new File(tempDir, "${className}-exec.jar")
  try (JarMergingOutputStream os = new JarMergingOutputStream(new FileOutputStream(jarWithDependencies), className)) {
    os.writeClass(classFile)
    for (dependencyJar in dependencyJars) {
      os.writeJar(dependencyJar)
    }
    os.flush()
  }

  // now export to provided format
  switch (arguments.outputType) {
    case OutputType.JAR:
      jarWithDependencies.renameTo(new File(arguments.outputDir, jarWithDependencies.name))
      break
    case OutputType.APPIMAGE:
      Jpackage jpackage = arguments.jpackageFile != null ? new Jpackage(arguments.jpackageFile)
          : Jpackage.newInstance()
      // dir containing all files that will be packaged (should be just the fat jar)
      File jpackageInputDir = new File(tempDir, "jpackage_input")
      jpackageInputDir.mkdir()
      jarWithDependencies.renameTo(new File(jpackageInputDir, jarWithDependencies.name))
      jpackage.run(jpackageInputDir, jarWithDependencies, className, arguments.outputDir)
      break
  }
} finally {
  // cleaning
  tempDir.deleteDir()
}
