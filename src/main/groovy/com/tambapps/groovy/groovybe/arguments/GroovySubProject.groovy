package com.tambapps.groovy.groovybe.arguments

enum GroovySubProject {
  ALL('groovy-all'),
  ANT('groovy-ant'),
  ASTBUILDER('groovy-astbuilder'),
  CLI_COMMONS('groovy-cli-commons'),
  CLI_PICOCLI('groovy-cli-picocli'),
  CONSOLE('groovy-console'),
  CONTRACTS('groovy-contracts'),
  DATETIME('groovy-datetime'),
  DATEUTIL('groovy-dateutil'),
  DOCGENERATOR('groovy-docgenerator'),
  GINQ('groovy-ginq'),
  GROOVYDOC('groovy-groovydoc'),
  GROOVYSH('groovy-groovysh'),
  JMX('groovy-jmx'),
  JSON('groovy-json'),
  JSR223('groovy-jsr223'),
  MACRO_LIBRARY('groovy-macro-library'),
  MACRO('groovy-macro'),
  NIO('groovy-nio'),
  SERVLET('groovy-servlet'),
  SQL('groovy-sql'),
  SWING('groovy-swing'),
  TEMPLATES('groovy-templates'),
  TEST_JUNIT5('groovy-test-junit5'),
  TEST('groovy-test'),
  TESTING('groovy-testing'),
  TOML('groovy-toml'),
  TYPECHECKERS('groovy-typecheckers'),
  XML('groovy-xml'),
  YAML('groovy-yaml');

  final String artifactId

  GroovySubProject(String artifactId) {
    this.artifactId = artifactId
  }
}
