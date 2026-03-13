package org.apache.commons.io.output;

import java.io.IOException;
import java.io.Writer;

public class ClosedWriter extends Writer {
   public static final ClosedWriter CLOSED_WRITER = new ClosedWriter();

   public void write(char[] var1, int var2, int var3) {
      throw new IOException("write(" + new String(var1) + ", " + var2 + ", " + var3 + ") failed: stream is closed");
   }

   public void flush() {
      throw new IOException("flush() failed: stream is closed");
   }

   public void close() {
   }
}
