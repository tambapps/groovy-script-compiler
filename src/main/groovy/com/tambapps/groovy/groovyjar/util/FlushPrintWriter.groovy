package com.tambapps.groovy.groovyjar.util

class FlushPrintWriter extends PrintWriter {

  FlushPrintWriter(OutputStream out) {
    super(out, true)
  }

  @Override
  PrintWriter append(CharSequence csq) {
    super.append(csq)
    flush()
    return this
  }
}
