package org.apache.commons.io;

import java.nio.ByteOrder;

public final class ByteOrderParser {
   private ByteOrderParser() {
   }

   public static ByteOrder parseByteOrder(String var0) {
      if (ByteOrder.BIG_ENDIAN.toString().equals(var0)) {
         return ByteOrder.BIG_ENDIAN;
      } else if (ByteOrder.LITTLE_ENDIAN.toString().equals(var0)) {
         return ByteOrder.LITTLE_ENDIAN;
      } else {
         throw new IllegalArgumentException("Unsupported byte order setting: " + var0 + ", expected one of " + ByteOrder.LITTLE_ENDIAN + ", " + ByteOrder.BIG_ENDIAN);
      }
   }
}
