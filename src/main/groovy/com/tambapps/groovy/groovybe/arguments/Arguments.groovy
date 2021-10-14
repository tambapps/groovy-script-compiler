package com.tambapps.groovy.groovybe.arguments

import com.tambapps.groovy.groovybe.util.Utils
import picocli.CommandLine

class Arguments {

  @CommandLine.Parameters(paramLabel = "SCRIPTFILE", description = 'The script file to compile', arity = '1')
  File scriptFile

  @CommandLine.Option(names = ['-t', '--type'], description = 'The type of output to create')
  OutputType outputType = OutputType.JAR

  @CommandLine.Option(names = ['-v', '--version'], description = 'Groovy version to use')
  String version = '3.0.9'

  @CommandLine.Option(names = ['-j', '--jpackage-path'], description = "Path of jpackage. Only useful for 'appimage' type")
  File jpackageFile = null

  @CommandLine.Option(names = ['-o', '--output-dir'], description = "Folder in which to put output file")
  File outputDir = Utils.CURRENT_DIRECTORY

  @CommandLine.Option(names = ['-s', '--groovy-subprojects'], description = 'Comma-separated list of Groovy subprojects to include in the jar', split = ',')
  List<GroovySubProjects> subProjects = []

  @CommandLine.Option(names = ['-a', '--additional-jars'], description = 'Comma-separated list of Additional jars. E.g. if your script use a non Groovy library, it would be the jar of the library', split = ',')
  List<File> additionalJars = []

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
    if (arguments.jpackageFile != null && !arguments.jpackageFile.exists()) {
      println("${arguments.jpackageFile} doesn't exists")
      return null
    }
    return arguments
  }
}
