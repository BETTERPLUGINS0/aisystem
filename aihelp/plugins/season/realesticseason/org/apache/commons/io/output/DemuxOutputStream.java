package org.apache.commons.io.output;

import java.io.Closeable;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

public class DemuxOutputStream extends OutputStream {
   private final InheritableThreadLocal<OutputStream> outputStreamThreadLocal = new InheritableThreadLocal();

   public OutputStream bindStream(OutputStream var1) {
      OutputStream var2 = (OutputStream)this.outputStreamThreadLocal.get();
      this.outputStreamThreadLocal.set(var1);
      return var2;
   }

   public void close() {
      IOUtils.close((Closeable)this.outputStreamThreadLocal.get());
   }

   public void flush() {
      OutputStream var1 = (OutputStream)this.outputStreamThreadLocal.get();
      if (null != var1) {
         var1.flush();
      }

   }

   public void write(int var1) {
      OutputStream var2 = (OutputStream)this.outputStreamThreadLocal.get();
      if (null != var2) {
         var2.write(var1);
      }

   }
}
