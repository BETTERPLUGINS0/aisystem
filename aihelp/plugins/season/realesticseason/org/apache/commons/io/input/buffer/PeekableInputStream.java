package org.apache.commons.io.input.buffer;

import java.io.InputStream;
import java.util.Objects;

public class PeekableInputStream extends CircularBufferInputStream {
   public PeekableInputStream(InputStream var1, int var2) {
      super(var1, var2);
   }

   public PeekableInputStream(InputStream var1) {
      super(var1);
   }

   public boolean peek(byte[] var1) {
      Objects.requireNonNull(var1, "sourceBuffer");
      return this.peek(var1, 0, var1.length);
   }

   public boolean peek(byte[] var1, int var2, int var3) {
      Objects.requireNonNull(var1, "sourceBuffer");
      if (var1.length > this.bufferSize) {
         throw new IllegalArgumentException("Peek request size of " + var1.length + " bytes exceeds buffer size of " + this.bufferSize + " bytes");
      } else {
         if (this.buffer.getCurrentNumberOfBytes() < var1.length) {
            this.fillBuffer();
         }

         return this.buffer.peek(var1, var2, var3);
      }
   }
}
