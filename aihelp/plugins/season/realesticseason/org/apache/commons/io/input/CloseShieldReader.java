package org.apache.commons.io.input;

import java.io.Reader;

public class CloseShieldReader extends ProxyReader {
   public static CloseShieldReader wrap(Reader var0) {
      return new CloseShieldReader(var0);
   }

   /** @deprecated */
   @Deprecated
   public CloseShieldReader(Reader var1) {
      super(var1);
   }

   public void close() {
      this.in = ClosedReader.CLOSED_READER;
   }
}
