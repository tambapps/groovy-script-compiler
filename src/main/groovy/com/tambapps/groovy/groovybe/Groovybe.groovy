package com.tambapps.groovy.groovybe

import groovy.transform.CompileStatic

@CompileStatic
class Groovybe {

  static final File CURRENT_DIRECTORY = new File("." + File.separator)

  static void main(String[] args) throws IOException {
    GroovyCompiler compiler = new GroovyCompiler()
    GroovyDepsFetcher groovyDepsFetcher = new GroovyDepsFetcher()
    JarMerger jarMerger = new JarMerger()

    File classFile = compiler.compile(new File(args[0])).get(0)
    File jarFile = new File(classFile.parent, Utils.nameWithExtension(classFile, ".jar"))
    try (ScriptJarOutputStream os = new ScriptJarOutputStream(jarFile, classFile)) {
      os.write()
    }

    List<File> groovyJars = groovyDepsFetcher.fetch()

    File jarWithDependencies = new File(CURRENT_DIRECTORY, "TODO_name.jar")
    jarMerger.merge(jarFile, groovyJars, jarWithDependencies)
    // TODO call jpackage
  }

}
