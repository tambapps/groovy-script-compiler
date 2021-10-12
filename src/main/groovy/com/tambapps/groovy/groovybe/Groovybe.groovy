package com.tambapps.groovy.groovybe

import com.tambapps.groovy.groovybe.io.JarMergingOutputStream
import com.tambapps.groovy.groovybe.io.ScriptJarOutputStream
import com.tambapps.groovy.groovybe.util.Utils

GroovyCompiler compiler = new GroovyCompiler()
GroovyDepsFetcher groovyDepsFetcher = new GroovyDepsFetcher()

File classFile = compiler.compile(new File(args[0])).get(0)
File jarFile = new File(classFile.parent, Utils.nameWithExtension(classFile, ".jar"))
try (ScriptJarOutputStream os = new ScriptJarOutputStream(jarFile, classFile)) {
  os.write()
}

List<File> groovyJars = groovyDepsFetcher.fetch()

File jarWithDependencies = new File(Utils.CURRENT_DIRECTORY, Utils.nameWithExtension(classFile, "-with-dependencies.jar"))
try (JarMergingOutputStream os = new JarMergingOutputStream(new FileOutputStream(jarWithDependencies))) {
  os.writeMainJar(jarFile)
  for (groovyJar in groovyJars) {
    os.writeJar(groovyJar)
  }
  os.flush()
}
// TODO call jpackage