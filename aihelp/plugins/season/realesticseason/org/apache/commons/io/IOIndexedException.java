package org.apache.commons.io;

import java.io.IOException;

public class IOIndexedException extends IOException {
   private static final long serialVersionUID = 1L;
   private final int index;

   public IOIndexedException(int var1, Throwable var2) {
      super(toMessage(var1, var2), var2);
      this.index = var1;
   }

   protected static String toMessage(int var0, Throwable var1) {
      String var2 = "Null";
      String var3 = var1 == null ? "Null" : var1.getClass().getSimpleName();
      String var4 = var1 == null ? "Null" : var1.getMessage();
      return String.format("%s #%,d: %s", var3, var0, var4);
   }

   public int getIndex() {
      return this.index;
   }
}
