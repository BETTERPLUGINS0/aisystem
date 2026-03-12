package fr.xephi.authme.util;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class FileUtils {
   private static final DateTimeFormatter CURRENT_DATE_STRING_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
   private static ConsoleLogger logger = ConsoleLoggerFactory.get(FileUtils.class);

   private FileUtils() {
   }

   public static boolean copyFileFromResource(File destinationFile, String resourcePath) {
      if (destinationFile.exists()) {
         return true;
      } else if (!createDirectory(destinationFile.getParentFile())) {
         logger.warning("Cannot create parent directories for '" + destinationFile + "'");
         return false;
      } else {
         try {
            InputStream is = getResourceFromJar(resourcePath);

            boolean var3;
            label65: {
               try {
                  if (is != null) {
                     Files.copy(is, destinationFile.toPath(), new CopyOption[0]);
                     var3 = true;
                     break label65;
                  }

                  logger.warning(String.format("Cannot copy resource '%s' to file '%s': cannot load resource", resourcePath, destinationFile.getPath()));
               } catch (Throwable var6) {
                  if (is != null) {
                     try {
                        is.close();
                     } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                     }
                  }

                  throw var6;
               }

               if (is != null) {
                  is.close();
               }

               return false;
            }

            if (is != null) {
               is.close();
            }

            return var3;
         } catch (IOException var7) {
            logger.logException(String.format("Cannot copy resource '%s' to file '%s':", resourcePath, destinationFile.getPath()), var7);
            return false;
         }
      }
   }

   public static boolean createDirectory(File dir) {
      if (!dir.exists() && !dir.mkdirs()) {
         logger.warning("Could not create directory '" + dir + "'");
         return false;
      } else {
         return dir.isDirectory();
      }
   }

   public static InputStream getResourceFromJar(String path) {
      String normalizedPath = path.replace("\\", "/");
      return AuthMe.class.getClassLoader().getResourceAsStream(normalizedPath);
   }

   public static void purgeDirectory(File directory) {
      if (directory.isDirectory()) {
         File[] files = directory.listFiles();
         if (files != null) {
            File[] var2 = files;
            int var3 = files.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               File target = var2[var4];
               if (target.isDirectory()) {
                  purgeDirectory(target);
               }

               delete(target);
            }

         }
      }
   }

   public static void delete(File file) {
      if (file != null) {
         boolean result = file.delete();
         if (!result) {
            logger.warning("Could not delete file '" + file + "'");
         }
      }

   }

   public static void create(File file) {
      try {
         boolean result = file.createNewFile();
         if (!result) {
            throw new IllegalStateException("Could not create file '" + file + "'");
         }
      } catch (IOException var2) {
         throw new IllegalStateException("Error while creating file '" + file + "'", var2);
      }
   }

   public static String makePath(String... elements) {
      return String.join(File.separator, elements);
   }

   public static String createCurrentTimeString() {
      return LocalDateTime.now().format(CURRENT_DATE_STRING_FORMATTER);
   }

   public static String createBackupFilePath(File file) {
      String filename = "backup_" + fr.xephi.authme.libs.com.google.common.io.Files.getNameWithoutExtension(file.getName()) + "_" + createCurrentTimeString() + "." + fr.xephi.authme.libs.com.google.common.io.Files.getFileExtension(file.getName());
      return makePath(file.getParent(), filename);
   }
}
