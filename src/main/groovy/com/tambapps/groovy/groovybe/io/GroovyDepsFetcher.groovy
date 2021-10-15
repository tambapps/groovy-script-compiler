package com.tambapps.groovy.groovybe.io

import com.tambapps.groovy.groovybe.arguments.GroovySubProjects
import com.tambapps.maven.dependency.resolver.DependencyResolver
import com.tambapps.maven.dependency.resolver.data.Artifact
import com.tambapps.maven.dependency.resolver.repository.RemoteSavingMavenRepository
import com.tambapps.maven.dependency.resolver.version.FirstVersionFoundConflictResolver


class GroovyDepsFetcher {

  // TODO handle case for people that doesn't have Maven. Be careful, we still need to save the files
  private final RemoteSavingMavenRepository repository = new RemoteSavingMavenRepository()
  private final DependencyResolver resolver = new DependencyResolver(repository)

  // for now it only support groovy 3.X
  // TODO for groovy 4.X groupId has changed. handle that
  List<File> fetch(String groovyVersion, List<GroovySubProjects> submodules, List<Artifact> grabbedArtifacts) {
    if (submodules.contains(GroovySubProjects.ALL)) {
      resolver.resolve('org.codehaus.groovy', GroovySubProjects.ALL.artifactId, groovyVersion)
    } else {
      resolver.resolve('org.codehaus.groovy', 'groovy', groovyVersion)
      for (submodule in submodules) {
        resolver.resolve('org.codehaus.groovy', submodule.artifactId, groovyVersion)
      }
    }
    for (artifact in grabbedArtifacts) {
      resolver.resolve(artifact)
    }
    return resolver.results
        .getArtifacts(new FirstVersionFoundConflictResolver())
        .collect(repository.&getJarFile)
  }
}
