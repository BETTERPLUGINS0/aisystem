package org.apache.commons.io.output;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import org.apache.commons.io.IOUtils;

public class ProxyCollectionWriter extends FilterCollectionWriter {
   public ProxyCollectionWriter(Collection<Writer> var1) {
      super(var1);
   }

   public ProxyCollectionWriter(Writer... var1) {
      super(var1);
   }

   protected void afterWrite(int var1) {
   }

   public Writer append(char var1) {
      try {
         this.beforeWrite(1);
         super.append(var1);
         this.afterWrite(1);
      } catch (IOException var3) {
         this.handleIOException(var3);
      }

      return this;
   }

   public Writer append(CharSequence var1) {
      try {
         int var2 = IOUtils.length(var1);
         this.beforeWrite(var2);
         super.append(var1);
         this.afterWrite(var2);
      } catch (IOException var3) {
         this.handleIOException(var3);
      }

      return this;
   }

   public Writer append(CharSequence var1, int var2, int var3) {
      try {
         this.beforeWrite(var3 - var2);
         super.append(var1, var2, var3);
         this.afterWrite(var3 - var2);
      } catch (IOException var5) {
         this.handleIOException(var5);
      }

      return this;
   }

   protected void beforeWrite(int var1) {
   }

   public void close() {
      try {
         super.close();
      } catch (IOException var2) {
         this.handleIOException(var2);
      }

   }

   public void flush() {
      try {
         super.flush();
      } catch (IOException var2) {
         this.handleIOException(var2);
      }

   }

   protected void handleIOException(IOException var1) {
      throw var1;
   }

   public void write(char[] var1) {
      try {
         int var2 = IOUtils.length(var1);
         this.beforeWrite(var2);
         super.write(var1);
         this.afterWrite(var2);
      } catch (IOException var3) {
         this.handleIOException(var3);
      }

   }

   public void write(char[] var1, int var2, int var3) {
      try {
         this.beforeWrite(var3);
         super.write(var1, var2, var3);
         this.afterWrite(var3);
      } catch (IOException var5) {
         this.handleIOException(var5);
      }

   }

   public void write(int var1) {
      try {
         this.beforeWrite(1);
         super.write(var1);
         this.afterWrite(1);
      } catch (IOException var3) {
         this.handleIOException(var3);
      }

   }

   public void write(String var1) {
      try {
         int var2 = IOUtils.length((CharSequence)var1);
         this.beforeWrite(var2);
         super.write(var1);
         this.afterWrite(var2);
      } catch (IOException var3) {
         this.handleIOException(var3);
      }

   }

   public void write(String var1, int var2, int var3) {
      try {
         this.beforeWrite(var3);
         super.write(var1, var2, var3);
         this.afterWrite(var3);
      } catch (IOException var5) {
         this.handleIOException(var5);
      }

   }
}
