package com.tambapps.groovy.groovybe.util

import groovy.transform.CompileStatic
import org.jetbrains.annotations.NotNull

@CompileStatic
class FlushPrintWriter extends PrintWriter {

  FlushPrintWriter(@NotNull OutputStream out) {
    super(out, true)
  }

  @Override
  PrintWriter append(CharSequence csq) {
    super.append(csq)
    flush()
    return this
  }
}
