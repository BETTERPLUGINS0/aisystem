package fr.xephi.authme.libs.ch.jalu.configme.utils;

import fr.xephi.authme.libs.ch.jalu.configme.exception.ConfigMeException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public final class Utils {
   private Utils() {
   }

   public static void createFileIfNotExists(Path file) {
      if (Files.exists(file, new LinkOption[0])) {
         if (!Files.isRegularFile(file, new LinkOption[0])) {
            throw new ConfigMeException("Expected file but '" + file + "' is not a file");
         }
      } else {
         Path parent = file.getParent();
         if (!Files.exists(parent, new LinkOption[0]) || !Files.isDirectory(parent, new LinkOption[0])) {
            try {
               Files.createDirectories(parent);
            } catch (IOException var4) {
               throw new ConfigMeException("Failed to create parent folders for '" + file + "'", var4);
            }
         }

         try {
            Files.createFile(file);
         } catch (IOException var3) {
            throw new ConfigMeException("Failed to create file '" + file + "'", var3);
         }
      }

   }
}
