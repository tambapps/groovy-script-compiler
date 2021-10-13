package com.tambapps.groovy.groovybe.arguments

enum GroovySubProjects {
  ALL('groovy-all'), JSON('groovy-json'), XML('groovy-xml');

  final String artifactId

  GroovySubProjects(String artifactId) {
    this.artifactId = artifactId
  }
}
