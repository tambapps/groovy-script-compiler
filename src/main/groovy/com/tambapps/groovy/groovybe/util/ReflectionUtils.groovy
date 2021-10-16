package com.tambapps.groovy.groovybe.util

import groovy.transform.CompileStatic

import java.lang.reflect.Field

@CompileStatic
final class ReflectionUtils {

  static void setPrivateFieldValue(Class clazz, Object object, String fieldName, Object value) {
    Field field = clazz.getDeclaredField(fieldName)
    field.setAccessible(true)
    field.set(object, value)
  }

}
