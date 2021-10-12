package com.tambapps.groovy.groovybe.io

import com.tambapps.groovy.groovybe.util.IOUtils
import groovy.transform.CompileStatic

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

@CompileStatic
class JarMergingOutputStream extends JarOutputStream {
  private List<String> writtenEntries = []

  JarMergingOutputStream(OutputStream out) throws IOException {
    super(out)
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
}
