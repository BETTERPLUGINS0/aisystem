package org.apache.commons.io.output;

import java.io.Writer;

public class CloseShieldWriter extends ProxyWriter {
   public static CloseShieldWriter wrap(Writer var0) {
      return new CloseShieldWriter(var0);
   }

   /** @deprecated */
   @Deprecated
   public CloseShieldWriter(Writer var1) {
      super(var1);
   }

   public void close() {
      this.out = ClosedWriter.CLOSED_WRITER;
   }
}
