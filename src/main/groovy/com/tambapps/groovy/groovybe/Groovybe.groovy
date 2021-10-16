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
setProperty('DEBUG', arguments.debug)

File tempDir = File.createTempDir('groovybe')

try {
  GroovyDepsFetcher groovyDepsFetcher = new GroovyDepsFetcher()
  SourceDependencyGrabber sourceDependencyGrabber = new SourceDependencyGrabber()

  // extract @Grab artifacts if any
  File transformedScriptFile = new File(tempDir, arguments.scriptFile.name)
  String className = Utils.nameWithExtension(arguments.scriptFile.name, '')

  String transformedText = sourceDependencyGrabber.transform(arguments.scriptFile.readLines())
  if (arguments.outputType == OutputType.NATIVE_BINARY) {
    // hack for native-image. long story explained in Utils
    // TODO it wouldn't work if script contains imports
    transformedText = Utils.applyScriptTemplate(className, transformedText)
  }
  transformedScriptFile.text = transformedText

  if (getProperty('DEBUG') && !sourceDependencyGrabber.grabbedArtifacts.isEmpty()) {
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
  debugPrintln('compiling script')
  GroovyCompiler compiler = new GroovyCompiler(tempDir, dependencyJars)
  File classFile = compiler.compile(transformedScriptFile)

  // compile executable jar
  debugPrintln('generating JAR')
  File jarWithDependencies = new File(tempDir, "${className}-exec.jar")
  try (JarMergingOutputStream os = new JarMergingOutputStream(new FileOutputStream(jarWithDependencies), className)) {
    os.writeClass(classFile)
    for (dependencyJar in dependencyJars) {
      os.writeJar(dependencyJar)
    }
    os.flush()
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
      outputFile = nativeImage.run(jarWithDependencies, className, arguments.outputDir)
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
} finally {
  // cleaning
  tempDir.deleteDir()
}

void debugPrintln(Object value) {
  if (getProperty('DEBUG')) {
    println(value)
  }
}