package com.tambapps.groovy.groovyb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class ScriptJarOutputStream extends JarOutputStream {

  private static Manifest generateManifest(File scriptClassFile) {
    Manifest manifest = new Manifest();
    Attributes mainAttributes = manifest.getMainAttributes();
    mainAttributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
    mainAttributes.put(Attributes.Name.MAIN_CLASS, scriptClassFile.getName().substring(0, scriptClassFile.getName().indexOf('.')));
    mainAttributes.put(new Attributes.Name("Created-By"), "Tambapps");
    mainAttributes.put(new Attributes.Name("Build-Jdk"), System.getProperty("java.version"));
    return manifest;
  }

  private final File scriptClassFile;
  public ScriptJarOutputStream(File outputFile, File scriptClassFile) throws IOException {
    super(new FileOutputStream(outputFile), ScriptJarOutputStream.generateManifest(scriptClassFile));
    this.scriptClassFile = scriptClassFile;
  }

  public void write() throws IOException {
    JarEntry entry = new JarEntry(scriptClassFile.getName());
    entry.setTime(scriptClassFile.lastModified());
    putNextEntry(entry);
    flush();
  }
}
