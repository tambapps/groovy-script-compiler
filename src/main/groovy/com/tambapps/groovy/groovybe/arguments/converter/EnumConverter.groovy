package com.tambapps.groovy.groovybe.arguments.converter

import com.tambapps.groovy.groovybe.util.Utils
import picocli.CommandLine

class EnumConverter<T extends Enum> implements CommandLine.ITypeConverter<T> {

  private final Class<T> clazz

  EnumConverter(Class<T> clazz) {
    this.clazz = clazz
  }

  @Override
  T convert(String value) throws Exception {
    def constants = Arrays.asList(clazz.enumConstants)
    for (constant in constants) {
      if (constant.name().equalsIgnoreCase(value.replaceAll("-", "_"))) {
        return constant
      }
    }
    def possibleValues = Utils.getEnumPossibleValues(clazz)
    throw new CommandLine.TypeConversionException("Unkown value '$value' (Possible values are $possibleValues)")
  }
}
