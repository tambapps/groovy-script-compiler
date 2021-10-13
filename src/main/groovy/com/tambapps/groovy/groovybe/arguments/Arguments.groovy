package com.tambapps.groovy.groovybe.arguments

import picocli.CommandLine

class Arguments {

  @CommandLine.Parameters(paramLabel = "SCRIPTFILE", description = 'The script file to compile')
  File scriptFile

  @CommandLine.Option(names = ['-t', '--type'], description = 'The type of output to create')
  OutputType outputType = OutputType.JAR

  @CommandLine.Option(names = ['-v', '--version'], description = 'Groovy version to use')
  String version = '3.0.9'

  @CommandLine.Option(names = ['-s', '--groovy-subprojects'], description = 'Groovy submodules to include in the jar', split = ',')
  List<GroovySubProjects> subProjects = []

  @CommandLine.Option(names = ["-h", "--help"], usageHelp = true, description = "display usage")
  boolean help = false

  static Arguments parseArgs(String[] args) {
    Arguments arguments = new Arguments()
    def commandLine = new CommandLine(arguments)
    commandLine.setCaseInsensitiveEnumValuesAllowed(true)
    try {
      commandLine.parseArgs(args)
    } catch (CommandLine.PicocliException e) {
      println(e.message)
      return null
    }
    if (arguments.help) {
      commandLine.usage(new PrintWriter(System.out))
      return null
    }
    if (!arguments.scriptFile) {
      return null
    }
    return arguments
  }
}
