package com.tambapps.groovy.groovybe.io

import com.tambapps.maven.dependency.resolver.data.Artifact
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

class SourceDependencyGrabberTest {

  private SourceDependencyGrabber grabber

  @BeforeEach
  void init() {
    grabber = new SourceDependencyGrabber()
  }

  @Test
  void grabNothing() {
    assertEquals('println "Hello World"', grabber.transform('println "Hello World"'))
    assertEquals([], grabber.grabbedArtifacts)
  }

  @Test
  void grabDependency() {
    assertEquals('println "Hello World"', grabber.transform(
        [
            "@Grab('org.springframework:spring-orm:3.2.5.RELEASE')",
            'println "Hello World"'
        ]))
    assertEquals([new Artifact('org.springframework', 'spring-orm', '3.2.5.RELEASE')],
        grabber.grabbedArtifacts)
  }
}
