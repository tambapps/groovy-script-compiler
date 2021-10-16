package com.tambapps.groovy.groovybe.util

import com.tambapps.groovy.groovybe.io.compiler.CustomModuleNode

import java.lang.reflect.Field

final class ReflectionUtils {

  static void setPrivateFieldValue(Class clazz, Object object, String fieldName, Object value) {
    Field field = clazz.getDeclaredField(fieldName)
    field.setAccessible(true)
    field.set(object, value)
  }

  static void setPrivateMethodAccessible(Class clazz, String methodName, Class... parameterTypes) {
    clazz.getDeclaredMethod(methodName, parameterTypes).setAccessible(true)
  }
}
