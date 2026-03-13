package org.apache.commons.io.output;

import java.io.IOException;
import java.io.Writer;

public class BrokenWriter extends Writer {
   private final IOException exception;

   public BrokenWriter(IOException var1) {
      this.exception = var1;
   }

   public BrokenWriter() {
      this(new IOException("Broken writer"));
   }

   public void write(char[] var1, int var2, int var3) {
      throw this.exception;
   }

   public void flush() {
      throw this.exception;
   }

   public void close() {
      throw this.exception;
   }
}
