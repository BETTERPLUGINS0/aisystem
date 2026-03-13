package org.apache.commons.io.input;

import java.io.FilterReader;
import java.io.Reader;
import java.util.function.IntPredicate;

public abstract class AbstractCharacterFilterReader extends FilterReader {
   protected static final IntPredicate SKIP_NONE = (var0) -> {
      return false;
   };
   private final IntPredicate skip;

   protected AbstractCharacterFilterReader(Reader var1) {
      this(var1, SKIP_NONE);
   }

   protected AbstractCharacterFilterReader(Reader var1, IntPredicate var2) {
      super(var1);
      this.skip = var2 == null ? SKIP_NONE : var2;
   }

   protected boolean filter(int var1) {
      return this.skip.test(var1);
   }

   public int read() {
      int var1;
      do {
         var1 = this.in.read();
      } while(var1 != -1 && this.filter(var1));

      return var1;
   }

   public int read(char[] var1, int var2, int var3) {
      int var4 = super.read(var1, var2, var3);
      if (var4 == -1) {
         return -1;
      } else {
         int var5 = var2 - 1;

         for(int var6 = var2; var6 < var2 + var4; ++var6) {
            if (!this.filter(var1[var6])) {
               ++var5;
               if (var5 < var6) {
                  var1[var5] = var1[var6];
               }
            }
         }

         return var5 - var2 + 1;
      }
   }
}
