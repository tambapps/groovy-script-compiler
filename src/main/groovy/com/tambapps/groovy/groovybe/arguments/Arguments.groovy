package com.tambapps.groovy.groovybe.arguments

import picocli.CommandLine

class Arguments {
  @CommandLine.Option(names = ['-o', '--output'], description = 'Output')
  OutputType outputType = OutputType.APP_IMAGE

  @CommandLine.Parameters(paramLabel = "SCRIPTFILE", description = 'The script file to compile')
  File scriptFile

  @CommandLine.Option(names = ["-h", "--help"], usageHelp = true, description = "display usage")
  boolean help = false

  static Arguments parseArgs(String[] args) {
    Arguments arguments = new Arguments()
    def commandLine = new CommandLine(arguments)
    commandLine.parseArgs(args).errors()
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
