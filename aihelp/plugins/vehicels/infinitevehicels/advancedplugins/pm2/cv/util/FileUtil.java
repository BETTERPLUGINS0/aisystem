package advancedplugins.pm2.cv.util;

import java.io.File;

public final class FileUtil {
   public static String getFileNameWithoutExtension(String name) {
      int var1 = var0.lastIndexOf(46);
      return var1 != -1 ? var0.substring(0, var1) : var0;
   }

   public static String getFileNameWithoutExtension(File file) {
      return getFileNameWithoutExtension(var0.getName());
   }

   public static void deleteDirectory(File directory) {
      if (var0.isDirectory()) {
         File[] var1 = var0.listFiles();
         if (var1 != null) {
            File[] var2 = var1;
            int var3 = var1.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               File var5 = var2[var4];
               deleteDirectory(var5);
            }
         }

         var0.delete();
      }

   }
}
