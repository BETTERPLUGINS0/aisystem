package org.apache.commons.io.output;

import java.io.OutputStream;

public class CloseShieldOutputStream extends ProxyOutputStream {
   public static CloseShieldOutputStream wrap(OutputStream var0) {
      return new CloseShieldOutputStream(var0);
   }

   /** @deprecated */
   @Deprecated
   public CloseShieldOutputStream(OutputStream var1) {
      super(var1);
   }

   public void close() {
      this.out = ClosedOutputStream.CLOSED_OUTPUT_STREAM;
   }
}
