package org.apache.commons.io.input;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import org.apache.commons.io.IOUtils;

public abstract class ProxyReader extends FilterReader {
   public ProxyReader(Reader var1) {
      super(var1);
   }

   public int read() {
      try {
         this.beforeRead(1);
         int var1 = this.in.read();
         this.afterRead(var1 != -1 ? 1 : -1);
         return var1;
      } catch (IOException var2) {
         this.handleIOException(var2);
         return -1;
      }
   }

   public int read(char[] var1) {
      try {
         this.beforeRead(IOUtils.length(var1));
         int var2 = this.in.read(var1);
         this.afterRead(var2);
         return var2;
      } catch (IOException var3) {
         this.handleIOException(var3);
         return -1;
      }
   }

   public int read(char[] var1, int var2, int var3) {
      try {
         this.beforeRead(var3);
         int var4 = this.in.read(var1, var2, var3);
         this.afterRead(var4);
         return var4;
      } catch (IOException var5) {
         this.handleIOException(var5);
         return -1;
      }
   }

   public int read(CharBuffer var1) {
      try {
         this.beforeRead(IOUtils.length((CharSequence)var1));
         int var2 = this.in.read(var1);
         this.afterRead(var2);
         return var2;
      } catch (IOException var3) {
         this.handleIOException(var3);
         return -1;
      }
   }

   public long skip(long var1) {
      try {
         return this.in.skip(var1);
      } catch (IOException var4) {
         this.handleIOException(var4);
         return 0L;
      }
   }

   public boolean ready() {
      try {
         return this.in.ready();
      } catch (IOException var2) {
         this.handleIOException(var2);
         return false;
      }
   }

   public void close() {
      try {
         this.in.close();
      } catch (IOException var2) {
         this.handleIOException(var2);
      }

   }

   public synchronized void mark(int var1) {
      try {
         this.in.mark(var1);
      } catch (IOException var3) {
         this.handleIOException(var3);
      }

   }

   public synchronized void reset() {
      try {
         this.in.reset();
      } catch (IOException var2) {
         this.handleIOException(var2);
      }

   }

   public boolean markSupported() {
      return this.in.markSupported();
   }

   protected void beforeRead(int var1) {
   }

   protected void afterRead(int var1) {
   }

   protected void handleIOException(IOException var1) {
      throw var1;
   }
}
