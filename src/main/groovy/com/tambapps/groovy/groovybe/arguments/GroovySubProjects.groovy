package com.tambapps.groovy.groovybe.arguments

enum GroovySubProjects {
  ALL('groovy-all'), JSON('groovy-xml'), XML('groovy-json');

  final String artifactId

  GroovySubProjects(String artifactId) {
    this.artifactId = artifactId
  }
}
