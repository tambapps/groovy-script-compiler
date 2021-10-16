package com.tambapps.groovy.groovybe.util

class IOUtils {

  static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
    byte[] buf = new byte[8192]
    int count
    while(-1 != (count = inputStream.read(buf))) {
      outputStream.write(buf, 0, count)
    }
  }

  static void runProcess(List<String> command, String errorMessage) {
    Process process = command.join(' ').execute()
    process.consumeProcessOutput(new FlushPrintWriter(System.out), new FlushPrintWriter(System.err))
    int outputCode = process.waitFor()
    if (outputCode != 0) {
      throw new IOException(errorMessage)
    }
  }
}
