package com.tambapps.groovy.groovybe.util

import groovy.transform.CompileStatic

@CompileStatic
class IOUtils {

  static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
    byte[] buf = new byte[8192]
    int count
    while(-1 != (count = inputStream.read(buf))) {
      outputStream.write(buf, 0, count)
    }
  }
}
