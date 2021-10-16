package com.tambapps.groovy.groovyjar.util

import java.lang.reflect.Field

final class ReflectionUtils {

  static void setPrivateFieldValue(Class clazz, Object object, String fieldName, Object value) {
    Field field = clazz.getDeclaredField(fieldName)
    field.setAccessible(true)
    field.set(object, value)
  }

}
