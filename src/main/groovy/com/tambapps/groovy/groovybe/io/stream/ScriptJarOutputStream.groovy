package com.tambapps.groovy.groovybe.io.stream

import com.tambapps.groovy.groovybe.util.IOUtils
import groovy.transform.CompileStatic

import java.util.jar.Attributes
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream
import java.util.jar.Manifest

@CompileStatic
class ScriptJarOutputStream extends JarOutputStream {

  private static Manifest generateManifest(String className) {
    Manifest manifest = new Manifest()
    Attributes mainAttributes = manifest.getMainAttributes()
    mainAttributes[Attributes.Name.MANIFEST_VERSION] = "1.0"
    mainAttributes[Attributes.Name.MAIN_CLASS] = className
    mainAttributes[new Attributes.Name("Created-By")] = "Tambapps"
    return manifest
  }

  ScriptJarOutputStream(File outputFile, String className) throws IOException {
    super(new FileOutputStream(outputFile), generateManifest(className))
  }

  void writeClass(File scriptClassFile) throws IOException {
    JarEntry entry = new JarEntry(scriptClassFile.getName())
    entry.setTime(scriptClassFile.lastModified())

    putNextEntry(entry)
    try (InputStream inputStream = new FileInputStream(scriptClassFile)) {
      IOUtils.copyStream(inputStream, this)
    }
    closeEntry()
    flush()
  }

}
