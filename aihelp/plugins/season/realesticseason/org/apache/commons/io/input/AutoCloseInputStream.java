package org.apache.commons.io.input;

import java.io.InputStream;

public class AutoCloseInputStream extends ProxyInputStream {
   public AutoCloseInputStream(InputStream var1) {
      super(var1);
   }

   public void close() {
      this.in.close();
      this.in = ClosedInputStream.CLOSED_INPUT_STREAM;
   }

   protected void afterRead(int var1) {
      if (var1 == -1) {
         this.close();
      }

   }

   protected void finalize() {
      this.close();
      super.finalize();
   }
}
