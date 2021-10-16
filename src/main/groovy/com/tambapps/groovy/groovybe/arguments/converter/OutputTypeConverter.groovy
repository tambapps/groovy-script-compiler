package com.tambapps.groovy.groovybe.arguments.converter

import com.tambapps.groovy.groovybe.arguments.GroovySubProject
import com.tambapps.groovy.groovybe.arguments.OutputType

class OutputTypeConverter extends EnumConverter<GroovySubProject> {

  OutputTypeConverter() {
    super(OutputType)
  }

}
