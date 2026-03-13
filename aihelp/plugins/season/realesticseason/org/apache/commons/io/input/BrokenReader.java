package org.apache.commons.io.input;

import java.io.IOException;
import java.io.Reader;

public class BrokenReader extends Reader {
   private final IOException exception;

   public BrokenReader(IOException var1) {
      this.exception = var1;
   }

   public BrokenReader() {
      this(new IOException("Broken reader"));
   }

   public int read(char[] var1, int var2, int var3) {
      throw this.exception;
   }

   public long skip(long var1) {
      throw this.exception;
   }

   public boolean ready() {
      throw this.exception;
   }

   public void mark(int var1) {
      throw this.exception;
   }

   public synchronized void reset() {
      throw this.exception;
   }

   public void close() {
      throw this.exception;
   }
}
