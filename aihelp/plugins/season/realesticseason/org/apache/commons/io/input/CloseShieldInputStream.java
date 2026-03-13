package org.apache.commons.io.input;

import java.io.InputStream;

public class CloseShieldInputStream extends ProxyInputStream {
   public static CloseShieldInputStream wrap(InputStream var0) {
      return new CloseShieldInputStream(var0);
   }

   /** @deprecated */
   @Deprecated
   public CloseShieldInputStream(InputStream var1) {
      super(var1);
   }

   public void close() {
      this.in = ClosedInputStream.CLOSED_INPUT_STREAM;
   }
}
