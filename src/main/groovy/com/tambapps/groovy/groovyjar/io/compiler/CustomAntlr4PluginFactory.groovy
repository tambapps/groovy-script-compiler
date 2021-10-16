package com.tambapps.groovy.groovyjar.io.compiler

import com.tambapps.groovy.groovybe.util.ReflectionUtils;
import org.apache.groovy.parser.antlr4.Antlr4ParserPlugin
import org.apache.groovy.parser.antlr4.Antlr4PluginFactory
import org.apache.groovy.parser.antlr4.AstBuilder
import org.codehaus.groovy.ast.ModuleNode
import org.codehaus.groovy.control.ParserPlugin
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.syntax.Reduction

/**
 * Config class stuff to make native-image binaries work. Whole explanation at CustomModuleNode
 */
class CustomAntlr4PluginFactory extends Antlr4PluginFactory {

  @Override
  ParserPlugin createParserPlugin() {
    return new CustomAntlr4ParserPlugin()
  }

  private static class CustomAntlr4ParserPlugin extends Antlr4ParserPlugin {
    @Override
    ModuleNode buildAST(final SourceUnit sourceUnit, final ClassLoader classLoader, final Reduction cst) {
      CustomAstBuilder builder = new CustomAstBuilder(sourceUnit,
          sourceUnit.getConfiguration().isGroovydocEnabled(),
          sourceUnit.getConfiguration().isRuntimeGroovydocEnabled()
      )
      return builder.buildAST()
    }
  }
  private static class CustomAstBuilder extends AstBuilder {
    CustomAstBuilder(SourceUnit sourceUnit, boolean groovydocEnabled, boolean runtimeGroovydocEnabled) {
      super(sourceUnit, groovydocEnabled, runtimeGroovydocEnabled)
      ReflectionUtils.setPrivateFieldValue(getClass().superclass, this, 'moduleNode', new CustomModuleNode(sourceUnit))
    }
  }
}
