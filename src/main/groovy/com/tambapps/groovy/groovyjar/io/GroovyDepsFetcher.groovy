package com.tambapps.groovy.groovyjar.io

import com.tambapps.groovy.groovyjar.arguments.GroovySubProject
import com.tambapps.maven.dependency.resolver.DependencyResolver
import com.tambapps.maven.dependency.resolver.data.Artifact
import com.tambapps.maven.dependency.resolver.repository.RemoteSavingMavenRepository
import com.tambapps.maven.dependency.resolver.version.FirstVersionFoundConflictResolver

class GroovyDepsFetcher {

  // dependencies will be saved in local Maven repository
  private final RemoteSavingMavenRepository repository = new RemoteSavingMavenRepository()
  private final DependencyResolver resolver = new DependencyResolver(repository)

  List<File> fetch(String groovyVersion, List<GroovySubProject> submodules, List<Artifact> grabbedArtifacts) {
    String groovyGroupId = getGroovyGroupId(groovyVersion)
    if (submodules.contains(GroovySubProject.ALL)) {
      resolver.resolve(groovyGroupId, GroovySubProject.ALL.artifactId, groovyVersion)
    } else {
      resolver.resolve(groovyGroupId, 'groovy', groovyVersion)
      for (submodule in submodules) {
        resolver.resolve(groovyGroupId, submodule.artifactId, groovyVersion)
      }
    }
    for (artifact in grabbedArtifacts) {
      resolver.resolve(artifact)
    }
    return resolver.results
        .getArtifacts(new FirstVersionFoundConflictResolver())
        .collect(repository.&getJarFile)
  }

  private static String getGroovyGroupId(String version) {
    return version[0].isNumber() && version[0].toInteger() >= 4 ? 'org.apache.groovy' : 'org.codehaus.groovy'
  }
}
