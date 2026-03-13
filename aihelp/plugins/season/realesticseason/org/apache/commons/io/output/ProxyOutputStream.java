package org.apache.commons.io.output;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

public class ProxyOutputStream extends FilterOutputStream {
   public ProxyOutputStream(OutputStream var1) {
      super(var1);
   }

   public void write(int var1) {
      try {
         this.beforeWrite(1);
         this.out.write(var1);
         this.afterWrite(1);
      } catch (IOException var3) {
         this.handleIOException(var3);
      }

   }

   public void write(byte[] var1) {
      try {
         int var2 = IOUtils.length(var1);
         this.beforeWrite(var2);
         this.out.write(var1);
         this.afterWrite(var2);
      } catch (IOException var3) {
         this.handleIOException(var3);
      }

   }

   public void write(byte[] var1, int var2, int var3) {
      try {
         this.beforeWrite(var3);
         this.out.write(var1, var2, var3);
         this.afterWrite(var3);
      } catch (IOException var5) {
         this.handleIOException(var5);
      }

   }

   public void flush() {
      try {
         this.out.flush();
      } catch (IOException var2) {
         this.handleIOException(var2);
      }

   }

   public void close() {
      IOUtils.close(this.out, this::handleIOException);
   }

   protected void beforeWrite(int var1) {
   }

   protected void afterWrite(int var1) {
   }

   protected void handleIOException(IOException var1) {
      throw var1;
   }
}
