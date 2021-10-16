package com.tambapps.groovy.groovybe

import com.tambapps.groovy.groovybe.arguments.Arguments
import com.tambapps.groovy.groovybe.arguments.OutputType
import com.tambapps.groovy.groovybe.io.SourceDependencyGrabber
import com.tambapps.groovy.groovybe.io.GroovyCompiler
import com.tambapps.groovy.groovybe.io.GroovyDepsFetcher
import com.tambapps.groovy.groovybe.io.process.Jpackage
import com.tambapps.groovy.groovybe.io.process.NativeImage
import com.tambapps.groovy.groovybe.io.stream.JarMergingOutputStream
import com.tambapps.groovy.groovybe.util.Utils
import com.tambapps.maven.dependency.resolver.data.Artifact

import java.nio.file.Path

Arguments arguments = Arguments.parseArgs(args)
if (!arguments) {
  return
}
Utils.debug = arguments.debug

File tempDir = File.createTempDir('groovybe')

try {
  File jarWithDependencies
  String className = arguments.mainClass ?: Utils.nameWithExtension(arguments.inputFile.name, '')
  if (arguments.inputFile.name.endsWith('.jar')) {
    jarWithDependencies = arguments.inputFile
  } else {
    GroovyDepsFetcher groovyDepsFetcher = new GroovyDepsFetcher()
    SourceDependencyGrabber sourceDependencyGrabber = new SourceDependencyGrabber()

    // extract @Grab artifacts if any
    File transformedScriptFile = new File(tempDir, arguments.inputFile.name)
    transformedScriptFile.text = sourceDependencyGrabber.transform(arguments.inputFile.readLines())

    if (Utils.debug && !sourceDependencyGrabber.grabbedArtifacts.isEmpty()) {
      debugPrintln("found @Grab artifacts" +
          sourceDependencyGrabber.grabbedArtifacts.collect(Artifact.&toArtifactString))
      debugPrintln("ignored @Grab annotation(s), the artifact dependencies will be included in the jar")
    }

    // Fetch dependencies. They will constitute the classpath used for compilation
    debugPrintln('retrieving dependencies')
    List<File> fetchedDependencyJars = groovyDepsFetcher.fetch(arguments.version, arguments.subProjects,
        sourceDependencyGrabber.grabbedArtifacts)
    List<File> dependencyJars = fetchedDependencyJars + arguments.additionalJars

    // compile class
    debugPrintln('compiling script (Groovy compiler ' + GroovySystem.getVersion() + ')')
    GroovyCompiler compiler = new GroovyCompiler(tempDir, dependencyJars, arguments.outputType == OutputType.NATIVE_BINARY)
    File classFile = compiler.compile(transformedScriptFile)

    // compile executable jar
    debugPrintln('generating JAR')
    jarWithDependencies = new File(tempDir, "${className}-exec.jar")

    try (JarMergingOutputStream os = new JarMergingOutputStream(new FileOutputStream(jarWithDependencies), className)) {
      os.writeClass(classFile)
      for (dependencyJar in dependencyJars) {
        os.writeJar(dependencyJar)
      }
      os.flush()
    }
  }

  File outputFile
  // now export to provided format
  switch (arguments.outputType) {
    case OutputType.JAR:
      outputFile = new File(arguments.outputDir, jarWithDependencies.name)
      jarWithDependencies.renameTo(outputFile)
      break
    case OutputType.APPIMAGE:
      debugPrintln('running jpackage')
      Jpackage jpackage = arguments.jpackageFile != null ? new Jpackage(arguments.jpackageFile)
          : Jpackage.newInstance()
      outputFile = jpackage.run(tempDir, jarWithDependencies, className, arguments.outputDir)
      break
    case OutputType.NATIVE_BINARY:
      debugPrintln('running native-image')
      NativeImage nativeImage = new NativeImage(arguments.nativeImageFile)
      outputFile = nativeImage.run(jarWithDependencies, className, tempDir, arguments.outputDir)
      break
    default:
      throw new UnsupportedOperationException("Output type ${arguments.outputType} is not supported")
  }

  Path normalizedPath = outputFile.toPath().toAbsolutePath().normalize()
  if (outputFile.directory) {
    println "Files were generated in $normalizedPath"
  } else {
    println "$normalizedPath was generated"
  }
} catch (IOException e) {
  println e.message
} finally {
  // cleaning
  tempDir.deleteDir()
}

void debugPrintln(Object value) {
  if (Utils.debug) {
    println(value)
  }
}