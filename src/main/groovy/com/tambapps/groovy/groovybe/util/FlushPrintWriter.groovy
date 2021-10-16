package com.tambapps.groovy.groovybe.util

import org.jetbrains.annotations.NotNull

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
