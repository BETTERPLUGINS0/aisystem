package org.apache.commons.io.input;

public class InfiniteCircularInputStream extends CircularInputStream {
   public InfiniteCircularInputStream(byte[] var1) {
      super(var1, -1L);
   }
}
