package org.apache.commons.io.input;

import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;

public class TeeReader extends ProxyReader {
   private final Writer branch;
   private final boolean closeBranch;

   public TeeReader(Reader var1, Writer var2) {
      this(var1, var2, false);
   }

   public TeeReader(Reader var1, Writer var2, boolean var3) {
      super(var1);
      this.branch = var2;
      this.closeBranch = var3;
   }

   public void close() {
      try {
         super.close();
      } finally {
         if (this.closeBranch) {
            this.branch.close();
         }

      }

   }

   public int read() {
      int var1 = super.read();
      if (var1 != -1) {
         this.branch.write(var1);
      }

      return var1;
   }

   public int read(char[] var1) {
      int var2 = super.read(var1);
      if (var2 != -1) {
         this.branch.write(var1, 0, var2);
      }

      return var2;
   }

   public int read(char[] var1, int var2, int var3) {
      int var4 = super.read(var1, var2, var3);
      if (var4 != -1) {
         this.branch.write(var1, var2, var4);
      }

      return var4;
   }

   public int read(CharBuffer var1) {
      int var2 = var1.position();
      int var3 = super.read(var1);
      if (var3 != -1) {
         int var4 = var1.position();
         int var5 = var1.limit();

         try {
            var1.position(var2).limit(var4);
            this.branch.append(var1);
         } finally {
            var1.position(var4).limit(var5);
         }
      }

      return var3;
   }
}
