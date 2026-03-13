package org.apache.commons.io.file;

import org.apache.commons.io.IOUtils;

public enum StandardDeleteOption implements DeleteOption {
   OVERRIDE_READ_ONLY;

   public static boolean overrideReadOnly(DeleteOption[] var0) {
      if (IOUtils.length((Object[])var0) == 0) {
         return false;
      } else {
         DeleteOption[] var1 = var0;
         int var2 = var0.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            DeleteOption var4 = var1[var3];
            if (var4 == OVERRIDE_READ_ONLY) {
               return true;
            }
         }

         return false;
      }
   }

   // $FF: synthetic method
   private static StandardDeleteOption[] $values() {
      return new StandardDeleteOption[]{OVERRIDE_READ_ONLY};
   }
}
