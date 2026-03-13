package org.apache.commons.io.input;

import java.io.Reader;

public class ClosedReader extends Reader {
   public static final ClosedReader CLOSED_READER = new ClosedReader();

   public int read(char[] var1, int var2, int var3) {
      return -1;
   }

   public void close() {
   }
}
