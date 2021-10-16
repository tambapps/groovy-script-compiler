package com.tambapps.groovy.groovybe.io.compiler

import com.tambapps.groovy.groovybe.util.ReflectionUtils
import groovyjarjarasm.asm.Opcodes
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.ModuleNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.runtime.InvokerHelper

import static org.codehaus.groovy.ast.ClassHelper.isObjectType
import static org.codehaus.groovy.ast.ClassHelper.isObjectType
import static org.codehaus.groovy.ast.ClassHelper.isPrimitiveVoid
import static org.codehaus.groovy.ast.tools.GeneralUtils.args
import static org.codehaus.groovy.ast.tools.GeneralUtils.args
import static org.codehaus.groovy.ast.tools.GeneralUtils.args
import static org.codehaus.groovy.ast.tools.GeneralUtils.callX
import static org.codehaus.groovy.ast.tools.GeneralUtils.callX
import static org.codehaus.groovy.ast.tools.GeneralUtils.classX
import static org.codehaus.groovy.ast.tools.GeneralUtils.ctorX
import static org.codehaus.groovy.ast.tools.GeneralUtils.param
import static org.codehaus.groovy.ast.tools.GeneralUtils.params
import static org.codehaus.groovy.ast.tools.GeneralUtils.stmt
import static org.codehaus.groovy.ast.tools.GeneralUtils.stmt
import static org.codehaus.groovy.ast.tools.GeneralUtils.stmt
import static org.codehaus.groovy.ast.tools.GeneralUtils.varX
import static org.codehaus.groovy.ast.tools.GeneralUtils.varX
import static org.codehaus.groovy.ast.tools.GeneralUtils.varX
import static org.codehaus.groovy.ast.tools.GeneralUtils.varX


/**
 * This class is useful to make native-image binaries work
 *
 * When Groovy compiles a script, it converts it into a class extending Script, and creates two
 * constructors for it. It also generates a psvm method and instantiate and run the Script by calling
 * InvokerHelper.runScript(clazz, args). The constructor call is ambiguous to native-image, it doesn't
 * know which one to use.
 *
 * So this class modify groovyc default behaviour when converting script, by creating instead a
 * Script with only one constructor and an explicit call to it in the psvm method
 *
 */
class CustomModuleNode extends ModuleNode {
  CustomModuleNode(SourceUnit context) {
    super(context)
  }

  @Override
  protected ClassNode createStatementsClass() {
    ClassNode classNode = getScriptClassDummy();
    if (classNode.getName().endsWith("package-info")) {
      return classNode;
    }

    MethodNode existingMain = handleMainMethodIfPresent(methods);

    ctorX(classNode, args(ctorX(ClassHelper.make(Binding.class), args("args"))))

    /*
      Now we create the psvm method with only one statement (assuming the class name is HelloWorld)
      new HelloWorld(new Binding(args)).run()
     */
    classNode.addMethod(
        new MethodNode(
            "main",
            Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC,
            ClassHelper.VOID_TYPE,
            finalParam(ClassHelper.STRING_TYPE.makeArray(), "args"),
            ClassNode.EMPTY_ARRAY,
            stmt(
                callX(
                    ctorX(classNode, args(
                        ctorX(
                            ClassHelper.make(Binding.class), args(varX("args"))))),
                    "run")
            )
        )
    );

    MethodNode methodNode = new MethodNode("run", Opcodes.ACC_PUBLIC, ClassHelper.OBJECT_TYPE, Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, statementBlock);
    methodNode.setIsScriptBody();
    if (existingMain != null) {
      methodNode.addAnnotations(existingMain.getAnnotations());
    }
    classNode.addMethod(methodNode);

    Statement stmt = stmt(ctorX(ClassNode.SUPER, args(varX("context"))))

    classNode.addConstructor(
        Opcodes.ACC_PUBLIC,
        finalParam(ClassHelper.make(Binding.class), "context"),
        ClassNode.EMPTY_ARRAY,
        stmt);

    for (MethodNode method : methods) {
      if (method.isAbstract()) {
        throw new RuntimeException("Cannot use abstract methods in a script" +
            ", they are only available inside classes. Method: " + method.getName());
      }
      classNode.addMethod(method);
    }
    return classNode;
  }

  // just copied ModuleNode.handleMainMethodIfPresent
  private MethodNode handleMainMethodIfPresent(final List<MethodNode> methods) {
    boolean found = false;
    MethodNode result = null;
    for (Iterator<MethodNode> iter = methods.iterator(); iter.hasNext(); ) {
      MethodNode node = iter.next();
      if (node.getName().equals("main")) {
        if (node.isStatic() && node.getParameters().length == 1) {
          boolean retTypeMatches, argTypeMatches;
          ClassNode argType = node.getParameters()[0].getType();
          ClassNode retType = node.getReturnType();

          argTypeMatches = (isObjectType(argType) || argType.getName().contains("String[]"));
          retTypeMatches = (isPrimitiveVoid(retType) || isObjectType(retType));
          if (retTypeMatches && argTypeMatches) {
            if (found) {
              throw new RuntimeException("Repetitive main method found.");
            } else {
              found = true;
              result = node;
            }
            // if script has both loose statements as well as main(), then main() is ignored
            if (statementBlock.isEmpty()) {
              addStatement(node.getCode());
            }
            iter.remove();
          }
        }
      }
    }
    return result;
  }

  // just copied ModuleNode.finalParam
  private static Parameter[] finalParam(final ClassNode type, final String name) {
    Parameter parameter = param(type, name);
    parameter.setModifiers(Opcodes.ACC_FINAL);
    return params(parameter);
  }
}
