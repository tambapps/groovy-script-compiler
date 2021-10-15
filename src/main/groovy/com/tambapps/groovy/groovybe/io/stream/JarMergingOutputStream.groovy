package com.tambapps.groovy.groovybe.io.stream

import com.tambapps.groovy.groovybe.util.IOUtils
import groovy.transform.CompileStatic

import java.util.jar.Attributes
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.jar.Manifest
import java.util.zip.ZipEntry

@CompileStatic
class JarMergingOutputStream extends JarOutputStream {
  private List<String> writtenEntries = []

  JarMergingOutputStream(OutputStream out, String className) throws IOException {
    super(out, generateManifest(className))
    writtenEntries.add(JarFile.MANIFEST_NAME)
  }

  void writeClass(File scriptClassFile) throws IOException {
    JarEntry entry = new JarEntry(scriptClassFile.getName())
    entry.setTime(scriptClassFile.lastModified())

    putNextEntry(entry)
    try (InputStream inputStream = new FileInputStream(scriptClassFile)) {
      IOUtils.copyStream(inputStream, this)
    }
    closeEntry()
  }

  void writeJar(File mainJar) throws IOException {
    JarFile jarFile = new JarFile(mainJar)
    for (JarEntry entry : jarFile.entries()) {
      if (writtenEntries.contains(entry.name)) {
        continue
      }
      writtenEntries.add(entry.name)
      putNextEntry(entry)
      try (InputStream is = jarFile.getInputStream(entry)) {
        IOUtils.copyStream(is, this)
      }
      closeEntry()
    }
  }

  @Override
  void putNextEntry(ZipEntry ze) throws IOException {
    super.putNextEntry(ze)
  }

  private static Manifest generateManifest(String className) {
    Manifest manifest = new Manifest()
    Attributes mainAttributes = manifest.getMainAttributes()
    mainAttributes[Attributes.Name.MANIFEST_VERSION] = "1.0"
    mainAttributes[Attributes.Name.MAIN_CLASS] = className
    mainAttributes[new Attributes.Name("Created-By")] = "Tambapps"
    return manifest
  }
}
