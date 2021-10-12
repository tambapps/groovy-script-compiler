package com.tambapps.groovy.groovybe

import com.tambapps.maven.dependency.resolver.DependencyResolver
import com.tambapps.maven.dependency.resolver.data.DependencyResolvingResult
import com.tambapps.maven.dependency.resolver.data.PomArtifact
import com.tambapps.maven.dependency.resolver.repository.RemoteMavenRepository
import com.tambapps.maven.dependency.resolver.repository.RemoteSavingMavenRepository
import com.tambapps.maven.dependency.resolver.version.FirstVersionFoundConflictResolver

class GroovyDepsFetcher {

  private static final File USER_HOME = new File(System.getProperty("user.home"))
  private final RemoteSavingMavenRepository repository =
      new RemoteSavingMavenRepository(new File(USER_HOME, ".m2"),
          [new RemoteMavenRepository()])
  private final DependencyResolver resolver = new DependencyResolver(repository)

  // for now it only support groovy 3.X
  // TODO make it handle different versions
  List<File> fetch() {
    DependencyResolvingResult result = resolver.resolve('org.codehaus.groovy', 'groovy', '3.0.9')
    return result.getArtifacts(new FirstVersionFoundConflictResolver()).collect(repository.&getJarFile)
  }
}
