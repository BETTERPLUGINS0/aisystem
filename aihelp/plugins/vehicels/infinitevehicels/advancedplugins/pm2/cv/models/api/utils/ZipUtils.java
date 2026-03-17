package advancedplugins.pm2.cv.models.api.utils;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {
   public static void unzip(File var0, File var1) {
      byte[] var2 = new byte[1024];
      ZipInputStream var3 = new ZipInputStream(new FileInputStream(var0));

      for(ZipEntry var4 = var3.getNextEntry(); var4 != null; var4 = var3.getNextEntry()) {
         File var5 = newFile(var1, var4);
         if (var4.isDirectory()) {
            if (!var5.isDirectory() && !var5.mkdirs()) {
               throw new IOException("Failed to create directory " + String.valueOf(var5));
            }
         } else {
            File var6 = var5.getParentFile();
            if (!var6.isDirectory() && !var6.mkdirs()) {
               throw new IOException("Failed to create directory " + String.valueOf(var6));
            }

            FileOutputStream var7 = new FileOutputStream(var5);

            int var8;
            while((var8 = var3.read(var2)) > 0) {
               var7.write(var2, 0, var8);
            }

            var7.close();
         }
      }

      var3.closeEntry();
      var3.close();
   }

   private static File newFile(File var0, ZipEntry var1) {
      File var2 = new File(var0, var1.getName());
      String var3 = var0.getCanonicalPath();
      String var4 = var2.getCanonicalPath();
      if (!var4.startsWith(var3 + File.separator)) {
         throw new IOException("Entry is outside of the target dir: " + var1.getName());
      } else {
         ModelAPI.PLUGIN.getLogger().info("--> " + var1.getName());
         return var2;
      }
   }
}
