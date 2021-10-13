package com.tambapps.groovy.groovybe.exception

class JpackageNotFoundException extends RuntimeException {

  JpackageNotFoundException() {
    super("Jpackage could not found. You will have provide it manually")
  }
}
