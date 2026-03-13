package com.nisovin.shopkeepers.util.java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class FileUtils {
   public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-DD_HH-mm-ss");

   public static void checkIsFileWritable(Path file) throws IOException {
      if (!Files.isWritable(file)) {
         throw new IOException("Missing write permission for file " + String.valueOf(file));
      }
   }

   public static void checkIsDirectoryWritable(Path directory) throws IOException {
      if (!Files.isWritable(directory)) {
         throw new IOException("Missing write permission for directory " + String.valueOf(directory));
      } else {
         try {
            if (!Files.isExecutable(directory)) {
               throw new IOException("Missing execute (i.e. access) permission for directory " + String.valueOf(directory));
            }
         } catch (SecurityException var2) {
         }

      }
   }

   public static void fsync(Path path) throws IOException {
      boolean isDirectory = Files.isDirectory(path, new LinkOption[0]);
      StandardOpenOption fileAccess = isDirectory ? StandardOpenOption.READ : StandardOpenOption.WRITE;

      try {
         FileChannel file = FileChannel.open(path, fileAccess);

         try {
            file.force(true);
         } catch (Throwable var7) {
            if (file != null) {
               try {
                  file.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (file != null) {
            file.close();
         }

      } catch (IOException var8) {
         if (!isDirectory) {
            throw new IOException("Could not fsync file '" + String.valueOf(path) + "': " + ThrowableUtils.getDescription(var8), var8);
         }
      }
   }

   public static void fsyncParentDirectory(Path path) throws IOException {
      Validate.notNull(path, (String)"path is null");
      Path parent = path.getParent();
      if (parent != null) {
         fsync(parent);
      }

   }

   public static void createDirectories(Path directory) throws IOException {
      try {
         Files.createDirectories(directory);
      } catch (IOException var2) {
         throw new IOException("Could not create directory '" + String.valueOf(directory) + "': " + ThrowableUtils.getDescription(var2), var2);
      }
   }

   public static void createParentDirectories(Path path) throws IOException {
      Path parent = path.getParent();
      if (parent != null) {
         createDirectories(parent);
      }

   }

   public static void delete(Path path) throws IOException {
      try {
         Files.delete(path);
      } catch (IOException var2) {
         throw new IOException("Could not delete file '" + String.valueOf(path) + "': " + ThrowableUtils.getDescription(var2), var2);
      }
   }

   public static boolean deleteIfExists(Path path) throws IOException {
      try {
         return Files.deleteIfExists(path);
      } catch (IOException var2) {
         throw new IOException("Could not delete file '" + String.valueOf(path) + "': " + ThrowableUtils.getDescription(var2), var2);
      }
   }

   public static void moveFile(Path source, Path target, Logger logger) throws IOException {
      Validate.notNull(source, (String)"source is null");
      Validate.notNull(target, (String)"target is null");
      Validate.notNull(logger, (String)"logger is null");
      createParentDirectories(target);

      try {
         try {
            Files.move(source, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
         } catch (AtomicMoveNotSupportedException var6) {
            logger.warning(() -> {
               String var10000 = String.valueOf(source);
               return "Could not atomically move file '" + var10000 + "' to '" + String.valueOf(target) + "' (" + ThrowableUtils.getDescription(var6) + ")! Attempting non-atomic move.";
            });
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
         }
      } catch (IOException var7) {
         if (!source.toFile().renameTo(target.toFile())) {
            logger.warning(() -> {
               String var10000 = String.valueOf(source);
               return "Could not move file '" + var10000 + "' to '" + String.valueOf(target) + "' (" + ThrowableUtils.getDescription(var7) + ")! Attempting copy and delete.";
            });

            try {
               copy(source, target, StandardCopyOption.REPLACE_EXISTING);
               delete(source);
            } catch (IOException var5) {
               String var10002 = String.valueOf(source);
               throw new IOException("Could not copy-and-delete file '" + var10002 + "' to '" + String.valueOf(target) + "': " + ThrowableUtils.getDescription(var5), var5);
            }
         }
      }

   }

   public static void copy(Path source, Path target, CopyOption... copyOptions) throws IOException {
      Validate.notNull(source, (String)"source is null");
      Validate.notNull(target, (String)"target is null");
      createParentDirectories(target);
      Files.copy(source, target, copyOptions);
      fsync(target);
      fsyncParentDirectory(target);
   }

   public static Writer newUnbufferedWriter(Path path, Charset cs, OpenOption... options) throws IOException {
      CharsetEncoder encoder = cs.newEncoder();
      Writer writer = new OutputStreamWriter(Files.newOutputStream(path, options), encoder);
      return writer;
   }

   public static String read(Reader reader) throws IOException {
      Validate.notNull(reader, (String)"reader is null");
      BufferedReader bufferedReader;
      if (reader instanceof BufferedReader) {
         bufferedReader = (BufferedReader)reader;
      } else {
         bufferedReader = new BufferedReader(reader);
      }

      StringBuilder data = new StringBuilder();

      String var4;
      try {
         String line;
         while((line = bufferedReader.readLine()) != null) {
            data.append(line).append('\n');
         }

         var4 = data.toString();
      } finally {
         bufferedReader.close();
      }

      return var4;
   }

   public static Path getTempSibling(Path path) {
      Path fileName = path.getFileName();
      if (fileName == null) {
         throw new IllegalArgumentException("Path is empty!");
      } else {
         return path.resolveSibling(String.valueOf(fileName) + ".tmp");
      }
   }

   public static Path relativize(@Nullable Path basePath, Path path) {
      return basePath != null && path.startsWith(basePath) ? basePath.relativize(path) : path;
   }

   public static void writeSafely(Path path, String content, Charset charset, Logger logger, @Nullable Path basePath) throws IOException {
      Path tempPath = getTempSibling(path);

      assert tempPath != null;

      handleExistingTempFile(path, logger, basePath);
      createParentDirectories(tempPath);
      Path tempDirectory = tempPath.getParent();
      if (tempDirectory != null) {
         checkIsDirectoryWritable(tempDirectory);
      }

      Path directory = path.getParent();
      if (directory != null && !directory.equals(tempDirectory)) {
         checkIsDirectoryWritable(directory);
      }

      try {
         BufferedWriter writer = Files.newBufferedWriter(tempPath, charset);

         try {
            writer.write(content);
         } catch (Throwable var12) {
            if (writer != null) {
               try {
                  writer.close();
               } catch (Throwable var11) {
                  var12.addSuppressed(var11);
               }
            }

            throw var12;
         }

         if (writer != null) {
            writer.close();
         }
      } catch (IOException var13) {
         throw new IOException("Could not write temporary file (" + String.valueOf(relativize(basePath, tempPath)) + "): " + ThrowableUtils.getDescription(var13), var13);
      }

      fsync(tempPath);
      fsyncParentDirectory(tempPath);
      deleteIfExists(path);
      createParentDirectories(path);
      moveFile(tempPath, path, logger);
      fsyncParentDirectory(path);
   }

   private static void handleExistingTempFile(Path path, Logger logger, @Nullable Path basePath) throws IOException {
      Path tempPath = getTempSibling(path);
      if (Files.exists(tempPath, new LinkOption[0])) {
         checkIsFileWritable(tempPath);
         Path tempDirectory = tempPath.getParent();
         if (tempDirectory != null) {
            checkIsDirectoryWritable(tempDirectory);
         }

         Path directory = path.getParent();
         if (directory != null && !directory.equals(tempDirectory)) {
            checkIsDirectoryWritable(directory);
         }

         String var10001;
         if (!Files.exists(path, new LinkOption[0])) {
            var10001 = String.valueOf(relativize(basePath, tempPath));
            logger.warning("Found an existing temporary file (" + var10001 + "), but no file at the destination (" + String.valueOf(relativize(basePath, path)) + ")! This might indicate an issue during a previous write attempt! We rename the temporary file and then continue the writing!");
            moveFile(tempPath, path, logger);
         } else {
            var10001 = String.valueOf(relativize(basePath, tempPath));
            logger.warning("Found an existing temporary file (" + var10001 + "), but also a file at the destination (" + String.valueOf(relativize(basePath, path)) + ")! This might indicate an issue during a previous write attempt! We delete the temporary file and then continue the writing!");
            delete(tempPath);
         }

      }
   }

   private FileUtils() {
   }
}
