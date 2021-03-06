package com.tambapps.groovy.groovyjar.arguments

import com.tambapps.groovy.groovyjar.arguments.converter.GroovySubProjectConverter
import com.tambapps.groovy.groovyjar.arguments.converter.OutputTypeConverter
import com.tambapps.groovy.groovyjar.util.Utils
import picocli.CommandLine

class Arguments {

  @CommandLine.Parameters(paramLabel = "inputFile", description = 'The script file to compile (can be an executable jar instead for appimage and native-binary)', arity = '1')
  File inputFile

  @CommandLine.Option(names = ['-t', '--type'], description = 'The type of output to create. Possible values: [jar, appimage, native-binary]', converter = OutputTypeConverter, showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
  OutputType outputType = OutputType.JAR

  @CommandLine.Option(names = ['-v', '--version'], description = 'Groovy version to use', showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
  String version = '3.0.9'

  @CommandLine.Option(names = ['-m', '--main-class'], description = "Main class. Default to the input file's name")
  String mainClass

  @CommandLine.Option(names = ['-j', '--jpackage-path'], description = "Path of jpackage. Only useful for 'appimage' type")
  File jpackageFile = null

  @CommandLine.Option(names = ['-n', '--native-image-path'], description = "Path of native-image (graalvm). Only useful for 'native-binary' type")
  File nativeImageFile = null

  @CommandLine.Option(names = ['-o', '--output-dir'], description = "Folder in which to put output file. Defaults to current directory")
  File outputDir = Utils.CURRENT_DIRECTORY

  @CommandLine.Option(names = ['-s', '--groovy-subprojects'], description = 'Comma-separated list of Groovy subprojects to include in the jar. Possible values: [all, ant, astbuilder, cli-commons, cli-picocli, console, contracts, datetime, dateutil, docgenerator, ginq, groovydoc, groovysh, jmx, json, jsr223, macro-library, macro, nio, servlet, sql, swing, templates, test-junit5, test, testing, toml, typecheckers, xml, yaml]', split = ',', converter = GroovySubProjectConverter)
  List<GroovySubProject> subProjects = []

  @CommandLine.Option(names = ['-a', '--additional-jars'], description = 'Comma-separated list of Additional jars. E.g. if your script use a non Groovy library, it would be the jar of the library', split = ',')
  List<File> additionalJars = []

  @CommandLine.Option(names = ["-d", "--debug"], description = "Display more logs")
  boolean debug = false

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
      def writer = new PrintWriter(System.out)
      commandLine.usage(writer)
      writer.flush()
      return null
    }
    if (arguments.jpackageFile != null && !arguments.jpackageFile.exists()) {
      println("${arguments.jpackageFile} doesn't exists")
      return null
    }
    return arguments
  }
}
