package org.apache.commons.io.input;

import java.io.InputStream;

public class MarkShieldInputStream extends ProxyInputStream {
   public MarkShieldInputStream(InputStream var1) {
      super(var1);
   }

   public void mark(int var1) {
   }

   public boolean markSupported() {
      return false;
   }

   public void reset() {
      throw UnsupportedOperationExceptions.reset();
   }
}
