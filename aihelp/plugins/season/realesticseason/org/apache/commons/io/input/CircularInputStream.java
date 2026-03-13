package org.apache.commons.io.input;

import java.io.InputStream;
import java.util.Objects;

public class CircularInputStream extends InputStream {
   private long byteCount;
   private int position = -1;
   private final byte[] repeatedContent;
   private final long targetByteCount;

   private static byte[] validate(byte[] var0) {
      Objects.requireNonNull(var0, "repeatContent");
      byte[] var1 = var0;
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         byte var4 = var1[var3];
         if (var4 == -1) {
            throw new IllegalArgumentException("repeatContent contains the end-of-stream marker -1");
         }
      }

      return var0;
   }

   public CircularInputStream(byte[] var1, long var2) {
      this.repeatedContent = validate(var1);
      if (var1.length == 0) {
         throw new IllegalArgumentException("repeatContent is empty.");
      } else {
         this.targetByteCount = var2;
      }
   }

   public int read() {
      if (this.targetByteCount >= 0L) {
         if (this.byteCount == this.targetByteCount) {
            return -1;
         }

         ++this.byteCount;
      }

      this.position = (this.position + 1) % this.repeatedContent.length;
      return this.repeatedContent[this.position] & 255;
   }
}
