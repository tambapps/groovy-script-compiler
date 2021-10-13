package com.tambapps.groovy.groovybe.io

import com.tambapps.groovy.groovybe.arguments.GroovySubmodule
import com.tambapps.maven.dependency.resolver.DependencyResolver
import com.tambapps.maven.dependency.resolver.data.DependencyResolvingResult
import com.tambapps.maven.dependency.resolver.repository.RemoteSavingMavenRepository
import com.tambapps.maven.dependency.resolver.version.FirstVersionFoundConflictResolver

class GroovyDepsFetcher {

  private final RemoteSavingMavenRepository repository = new RemoteSavingMavenRepository()
  private final DependencyResolver resolver = new DependencyResolver(repository)

  // for now it only support groovy 3.X
  // TODO make it handle different versions
  List<File> fetch(List<GroovySubmodule> submodules) {
    // TODO handle groovy submodules
    DependencyResolvingResult result = resolver.resolve('org.codehaus.groovy', 'groovy', '3.0.9')
    return result.getArtifacts(new FirstVersionFoundConflictResolver()).collect(repository.&getJarFile)
  }
}
