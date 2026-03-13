package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class BrokenInputStream extends InputStream {
   private final IOException exception;

   public BrokenInputStream(IOException var1) {
      this.exception = var1;
   }

   public BrokenInputStream() {
      this(new IOException("Broken input stream"));
   }

   public int read() {
      throw this.exception;
   }

   public int available() {
      throw this.exception;
   }

   public long skip(long var1) {
      throw this.exception;
   }

   public synchronized void reset() {
      throw this.exception;
   }

   public void close() {
      throw this.exception;
   }
}
