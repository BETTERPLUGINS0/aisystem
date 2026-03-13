package com.dfsek.terra.config.fileloaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FolderLoader extends LoaderImpl {
   private static final Logger logger = LoggerFactory.getLogger(FolderLoader.class);
   private final Path path;

   public FolderLoader(Path path) {
      this.path = path;
   }

   public InputStream get(String singleFile) throws IOException {
      return new FileInputStream(new File(this.path.toFile(), singleFile));
   }

   protected void load(String directory, String extension) {
      File newPath = new File(this.path.toFile(), directory);
      newPath.mkdirs();

      try {
         Stream paths = Files.walk(newPath.toPath());

         try {
            paths.filter((x$0) -> {
               return Files.isRegularFile(x$0, new LinkOption[0]);
            }).filter((file) -> {
               return file.toString().toLowerCase().endsWith(extension);
            }).forEach((file) -> {
               try {
                  String rel = newPath.toPath().relativize(file).toString();
                  this.streams.put(rel, new FileInputStream(file.toFile()));
               } catch (FileNotFoundException var4) {
                  logger.error("Could not find file to load", var4);
               }

            });
         } catch (Throwable var8) {
            if (paths != null) {
               try {
                  paths.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         if (paths != null) {
            paths.close();
         }
      } catch (IOException var9) {
         logger.error("Error while loading files", var9);
      }

   }
}
