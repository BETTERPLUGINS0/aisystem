package advancedplugins.pm2.cv.models.api.utils;

import advancedplugins.pm2.cv.models.api.utils.data.ResourceLocation;
import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import com.google.common.io.ByteStreams;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Base64;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class FileUtils {
   public static final String SLASH = FileSystems.getDefault().getSeparator();

   public static File copyResource(JavaPlugin var0, File var1, String var2) {
      if (!var1.exists()) {
         try {
            FileOutputStream var3 = new FileOutputStream(var1);
            InputStream var4 = var0.getResource(var2);
            if (var4 != null) {
               ByteStreams.copy(var4, var3);
            }

            var3.close();
         } catch (IOException var5) {
            var5.printStackTrace();
         }
      }

      return var1;
   }

   public static File copyResource(JavaPlugin var0, File var1, String var2, String var3) {
      return copyResource(var0, createFile(var1, var3), var2 + "/" + var3);
   }

   public static String createPath(String... var0) {
      if (var0.length == 0) {
         return "";
      } else {
         StringBuilder var1 = new StringBuilder(var0[0]);

         for(int var2 = 1; var2 < var0.length; ++var2) {
            var1.append(SLASH).append(var0[var2]);
         }

         return var1.toString();
      }
   }

   public static File createDirectory(File var0, String... var1) {
      String var2 = createPath(var1);
      File var3 = new File(var0, var2);
      if (!var3.exists() && !var3.mkdirs()) {
         LogUtil.log("Failed to create directory: " + var2);
      }

      return var3;
   }

   public static File createFile(File var0, String... var1) {
      String var2 = createPath(var1);
      File var3 = new File(var0, var2);
      if (!var3.getParentFile().exists() && !var3.getParentFile().mkdirs()) {
         LogUtil.log("Failed to create file: " + var2);
      }

      return var3;
   }

   public static File createFileOrEmpty(File var0, String... var1) {
      String var2 = createPath(var1);
      File var3 = new File(var0, var2);

      try {
         if (!var3.getParentFile().exists() && !var3.getParentFile().mkdirs() || !var3.exists() && !var3.createNewFile()) {
            LogUtil.log("Failed to create file: " + var2);
         }
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      return var3;
   }

   public static File createFile(File var0, String var1, ResourceLocation var2, String var3) {
      String var10000 = var0.getPath();
      String var4 = var10000 + SLASH + var2.getNamespace() + SLASH + var1;
      String var5 = var2.getPath();
      File var6 = new File(var4, var5 + "." + var3);
      if (!var6.getParentFile().exists() && !var6.getParentFile().mkdirs() && !var6.getParentFile().exists()) {
         LogUtil.log("Failed to create file: " + var6.getPath());
      }

      return var6;
   }

   public static void recreateFile(File var0) {
      try {
         if (!var0.getParentFile().exists() && !var0.getParentFile().mkdirs() || !var0.exists() && !var0.createNewFile()) {
            LogUtil.log("Failed to create file: " + var0.getName());
         }
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public static BufferedImage toImage(String var0) {
      String[] var1 = var0.split(",");
      return var1.length > 1 ? rawToImage(var1[1]) : rawToImage(var0);
   }

   public static BufferedImage rawToImage(String var0) {
      try {
         byte[] var1 = Base64.getDecoder().decode(var0);
         return ImageIO.read(new ByteArrayInputStream(var1));
      } catch (IOException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static String removeExtension(String var0) {
      int var1 = var0.lastIndexOf(".");
      return var1 == -1 ? var0 : var0.substring(0, var1);
   }

   public static boolean isExtension(String var0, String var1) {
      int var2 = var0.lastIndexOf(".");
      return var2 != -1 && var0.substring(var2 + 1).equalsIgnoreCase(var1);
   }

   public static void zipFile(File var0, String var1, ZipOutputStream var2) {
      if (!var0.isHidden()) {
         int var3;
         if (var0.isDirectory()) {
            if (var1.endsWith("/")) {
               var2.putNextEntry(new ZipEntry(var1));
            } else {
               var2.putNextEntry(new ZipEntry(var1 + "/"));
            }

            var2.closeEntry();
            File[] var4 = var0.listFiles();
            if (var4 != null) {
               for(var3 = 0; var3 < var4.length; ++var3) {
                  File var5 = var4[var3];
                  zipFile(var5, var1 + "/" + var5.getName(), var2);
               }
            }
         } else {
            FileInputStream var7 = new FileInputStream(var0);
            ZipEntry var8 = new ZipEntry(var1);
            var2.putNextEntry(var8);
            byte[] var6 = new byte[1024];

            while((var3 = var7.read(var6)) >= 0) {
               var2.write(var6, 0, var3);
            }

            var7.close();
         }
      }

   }

   public static void cleanDirectory(Path var0) {
      if (var0 != null && Files.exists(var0, new LinkOption[0])) {
         if (!Files.isDirectory(var0, new LinkOption[0])) {
            throw new IllegalArgumentException("Not a directory: " + String.valueOf(var0));
         } else {
            DirectoryStream var1 = Files.newDirectoryStream(var0);

            try {
               Iterator var2 = var1.iterator();

               while(var2.hasNext()) {
                  Path var3 = (Path)var2.next();
                  deleteRecursively(var3);
               }
            } catch (Throwable var5) {
               if (var1 != null) {
                  try {
                     var1.close();
                  } catch (Throwable var4) {
                     var5.addSuppressed(var4);
                  }
               }

               throw var5;
            }

            if (var1 != null) {
               var1.close();
            }

         }
      } else {
         throw new IllegalArgumentException("Directory does not exist: " + String.valueOf(var0));
      }
   }

   private static void deleteRecursively(Path var0) {
      if (Files.isDirectory(var0, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
         Files.walkFileTree(var0, new SimpleFileVisitor<Path>() {
            @NotNull
            public FileVisitResult visitFile(@NotNull Path var1, @NotNull BasicFileAttributes var2) {
               Files.delete(var1);
               return FileVisitResult.CONTINUE;
            }

            @NotNull
            public FileVisitResult postVisitDirectory(@NotNull Path var1, IOException var2) {
               Files.delete(var1);
               return FileVisitResult.CONTINUE;
            }
         });
      } else {
         Files.delete(var0);
      }

   }
}
