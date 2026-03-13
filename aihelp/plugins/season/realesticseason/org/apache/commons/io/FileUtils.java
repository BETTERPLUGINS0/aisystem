package org.apache.commons.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;
import org.apache.commons.io.file.AccumulatorPathVisitor;
import org.apache.commons.io.file.Counters;
import org.apache.commons.io.file.PathFilter;
import org.apache.commons.io.file.PathUtils;
import org.apache.commons.io.file.StandardDeleteOption;
import org.apache.commons.io.filefilter.FileEqualsFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

public class FileUtils {
   public static final long ONE_KB = 1024L;
   public static final BigInteger ONE_KB_BI = BigInteger.valueOf(1024L);
   public static final long ONE_MB = 1048576L;
   public static final BigInteger ONE_MB_BI;
   public static final long ONE_GB = 1073741824L;
   public static final BigInteger ONE_GB_BI;
   public static final long ONE_TB = 1099511627776L;
   public static final BigInteger ONE_TB_BI;
   public static final long ONE_PB = 1125899906842624L;
   public static final BigInteger ONE_PB_BI;
   public static final long ONE_EB = 1152921504606846976L;
   public static final BigInteger ONE_EB_BI;
   public static final BigInteger ONE_ZB;
   public static final BigInteger ONE_YB;
   public static final File[] EMPTY_FILE_ARRAY;

   private static CopyOption[] addCopyAttributes(CopyOption... var0) {
      CopyOption[] var1 = (CopyOption[])Arrays.copyOf(var0, var0.length + 1);
      Arrays.sort(var1, 0, var0.length);
      if (Arrays.binarySearch(var0, 0, var0.length, StandardCopyOption.COPY_ATTRIBUTES) >= 0) {
         return var0;
      } else {
         var1[var1.length - 1] = StandardCopyOption.COPY_ATTRIBUTES;
         return var1;
      }
   }

   public static String byteCountToDisplaySize(BigInteger var0) {
      Objects.requireNonNull(var0, "size");
      String var1;
      if (var0.divide(ONE_EB_BI).compareTo(BigInteger.ZERO) > 0) {
         var1 = var0.divide(ONE_EB_BI) + " EB";
      } else if (var0.divide(ONE_PB_BI).compareTo(BigInteger.ZERO) > 0) {
         var1 = var0.divide(ONE_PB_BI) + " PB";
      } else if (var0.divide(ONE_TB_BI).compareTo(BigInteger.ZERO) > 0) {
         var1 = var0.divide(ONE_TB_BI) + " TB";
      } else if (var0.divide(ONE_GB_BI).compareTo(BigInteger.ZERO) > 0) {
         var1 = var0.divide(ONE_GB_BI) + " GB";
      } else if (var0.divide(ONE_MB_BI).compareTo(BigInteger.ZERO) > 0) {
         var1 = var0.divide(ONE_MB_BI) + " MB";
      } else if (var0.divide(ONE_KB_BI).compareTo(BigInteger.ZERO) > 0) {
         var1 = var0.divide(ONE_KB_BI) + " KB";
      } else {
         var1 = var0 + " bytes";
      }

      return var1;
   }

   public static String byteCountToDisplaySize(long var0) {
      return byteCountToDisplaySize(BigInteger.valueOf(var0));
   }

   public static Checksum checksum(File var0, Checksum var1) {
      requireExistsChecked(var0, "file");
      requireFile(var0, "file");
      Objects.requireNonNull(var1, "checksum");
      CheckedInputStream var2 = new CheckedInputStream(Files.newInputStream(var0.toPath()), var1);

      try {
         IOUtils.consume(var2);
      } catch (Throwable var6) {
         try {
            var2.close();
         } catch (Throwable var5) {
            var6.addSuppressed(var5);
         }

         throw var6;
      }

      var2.close();
      return var1;
   }

   public static long checksumCRC32(File var0) {
      return checksum(var0, new CRC32()).getValue();
   }

   public static void cleanDirectory(File var0) {
      File[] var1 = listFiles(var0, (FileFilter)null);
      ArrayList var2 = new ArrayList();
      File[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         File var6 = var3[var5];

         try {
            forceDelete(var6);
         } catch (IOException var8) {
            var2.add(var8);
         }
      }

      if (!var2.isEmpty()) {
         throw new IOExceptionList(var0.toString(), var2);
      }
   }

   private static void cleanDirectoryOnExit(File var0) {
      File[] var1 = listFiles(var0, (FileFilter)null);
      ArrayList var2 = new ArrayList();
      File[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         File var6 = var3[var5];

         try {
            forceDeleteOnExit(var6);
         } catch (IOException var8) {
            var2.add(var8);
         }
      }

      if (!var2.isEmpty()) {
         throw new IOExceptionList(var2);
      }
   }

   public static boolean contentEquals(File var0, File var1) {
      if (var0 == null && var1 == null) {
         return true;
      } else if (var0 != null && var1 != null) {
         boolean var2 = var0.exists();
         if (var2 != var1.exists()) {
            return false;
         } else if (!var2) {
            return true;
         } else {
            requireFile(var0, "file1");
            requireFile(var1, "file2");
            if (var0.length() != var1.length()) {
               return false;
            } else if (var0.getCanonicalFile().equals(var1.getCanonicalFile())) {
               return true;
            } else {
               InputStream var3 = Files.newInputStream(var0.toPath());

               boolean var5;
               try {
                  InputStream var4 = Files.newInputStream(var1.toPath());

                  try {
                     var5 = IOUtils.contentEquals(var3, var4);
                  } catch (Throwable var9) {
                     if (var4 != null) {
                        try {
                           var4.close();
                        } catch (Throwable var8) {
                           var9.addSuppressed(var8);
                        }
                     }

                     throw var9;
                  }

                  if (var4 != null) {
                     var4.close();
                  }
               } catch (Throwable var10) {
                  if (var3 != null) {
                     try {
                        var3.close();
                     } catch (Throwable var7) {
                        var10.addSuppressed(var7);
                     }
                  }

                  throw var10;
               }

               if (var3 != null) {
                  var3.close();
               }

               return var5;
            }
         }
      } else {
         return false;
      }
   }

   public static boolean contentEqualsIgnoreEOL(File var0, File var1, String var2) {
      if (var0 == null && var1 == null) {
         return true;
      } else if (var0 != null && var1 != null) {
         boolean var3 = var0.exists();
         if (var3 != var1.exists()) {
            return false;
         } else if (!var3) {
            return true;
         } else {
            requireFile(var0, "file1");
            requireFile(var1, "file2");
            if (var0.getCanonicalFile().equals(var1.getCanonicalFile())) {
               return true;
            } else {
               Charset var4 = Charsets.toCharset(var2);
               InputStreamReader var5 = new InputStreamReader(Files.newInputStream(var0.toPath()), var4);

               boolean var7;
               try {
                  InputStreamReader var6 = new InputStreamReader(Files.newInputStream(var1.toPath()), var4);

                  try {
                     var7 = IOUtils.contentEqualsIgnoreEOL(var5, var6);
                  } catch (Throwable var11) {
                     try {
                        var6.close();
                     } catch (Throwable var10) {
                        var11.addSuppressed(var10);
                     }

                     throw var11;
                  }

                  var6.close();
               } catch (Throwable var12) {
                  try {
                     var5.close();
                  } catch (Throwable var9) {
                     var12.addSuppressed(var9);
                  }

                  throw var12;
               }

               var5.close();
               return var7;
            }
         }
      } else {
         return false;
      }
   }

   public static File[] convertFileCollectionToFileArray(Collection<File> var0) {
      return (File[])var0.toArray(EMPTY_FILE_ARRAY);
   }

   public static void copyDirectory(File var0, File var1) {
      copyDirectory(var0, var1, true);
   }

   public static void copyDirectory(File var0, File var1, boolean var2) {
      copyDirectory(var0, var1, (FileFilter)null, var2);
   }

   public static void copyDirectory(File var0, File var1, FileFilter var2) {
      copyDirectory(var0, var1, var2, true);
   }

   public static void copyDirectory(File var0, File var1, FileFilter var2, boolean var3) {
      copyDirectory(var0, var1, var2, var3, StandardCopyOption.REPLACE_EXISTING);
   }

   public static void copyDirectory(File var0, File var1, FileFilter var2, boolean var3, CopyOption... var4) {
      requireFileCopy(var0, var1);
      requireDirectory(var0, "srcDir");
      requireCanonicalPathsNotEquals(var0, var1);
      ArrayList var5 = null;
      String var6 = var0.getCanonicalPath();
      String var7 = var1.getCanonicalPath();
      if (var7.startsWith(var6)) {
         File[] var8 = listFiles(var0, var2);
         if (var8.length > 0) {
            var5 = new ArrayList(var8.length);
            File[] var9 = var8;
            int var10 = var8.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               File var12 = var9[var11];
               File var13 = new File(var1, var12.getName());
               var5.add(var13.getCanonicalPath());
            }
         }
      }

      doCopyDirectory(var0, var1, var2, var5, var3, var3 ? addCopyAttributes(var4) : var4);
   }

   public static void copyDirectoryToDirectory(File var0, File var1) {
      requireDirectoryIfExists(var0, "sourceDir");
      requireDirectoryIfExists(var1, "destinationDir");
      copyDirectory(var0, new File(var1, var0.getName()), true);
   }

   public static void copyFile(File var0, File var1) {
      copyFile(var0, var1, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
   }

   public static void copyFile(File var0, File var1, boolean var2) {
      copyFile(var0, var1, var2 ? new CopyOption[]{StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING} : new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
   }

   public static void copyFile(File var0, File var1, boolean var2, CopyOption... var3) {
      copyFile(var0, var1, var2 ? addCopyAttributes(var3) : var3);
   }

   public static void copyFile(File var0, File var1, CopyOption... var2) {
      requireFileCopy(var0, var1);
      requireFile(var0, "srcFile");
      requireCanonicalPathsNotEquals(var0, var1);
      createParentDirectories(var1);
      requireFileIfExists(var1, "destFile");
      if (var1.exists()) {
         requireCanWrite(var1, "destFile");
      }

      Files.copy(var0.toPath(), var1.toPath(), var2);
      requireEqualSizes(var0, var1, var0.length(), var1.length());
   }

   public static long copyFile(File var0, OutputStream var1) {
      InputStream var2 = Files.newInputStream(var0.toPath());

      long var3;
      try {
         var3 = IOUtils.copyLarge(var2, var1);
      } catch (Throwable var6) {
         if (var2 != null) {
            try {
               var2.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (var2 != null) {
         var2.close();
      }

      return var3;
   }

   public static void copyFileToDirectory(File var0, File var1) {
      copyFileToDirectory(var0, var1, true);
   }

   public static void copyFileToDirectory(File var0, File var1, boolean var2) {
      Objects.requireNonNull(var0, "sourceFile");
      requireDirectoryIfExists(var1, "destinationDir");
      copyFile(var0, new File(var1, var0.getName()), var2);
   }

   public static void copyInputStreamToFile(InputStream var0, File var1) {
      InputStream var2 = var0;

      try {
         copyToFile(var2, var1);
      } catch (Throwable var6) {
         if (var0 != null) {
            try {
               var2.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (var0 != null) {
         var0.close();
      }

   }

   public static void copyToDirectory(File var0, File var1) {
      Objects.requireNonNull(var0, "sourceFile");
      if (var0.isFile()) {
         copyFileToDirectory(var0, var1);
      } else {
         if (!var0.isDirectory()) {
            throw new FileNotFoundException("The source " + var0 + " does not exist");
         }

         copyDirectoryToDirectory(var0, var1);
      }

   }

   public static void copyToDirectory(Iterable<File> var0, File var1) {
      Objects.requireNonNull(var0, "sourceIterable");
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         File var3 = (File)var2.next();
         copyFileToDirectory(var3, var1);
      }

   }

   public static void copyToFile(InputStream var0, File var1) {
      FileOutputStream var2 = openOutputStream(var1);

      try {
         IOUtils.copy((InputStream)var0, (OutputStream)var2);
      } catch (Throwable var6) {
         if (var2 != null) {
            try {
               var2.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (var2 != null) {
         var2.close();
      }

   }

   public static void copyURLToFile(URL var0, File var1) {
      InputStream var2 = var0.openStream();

      try {
         copyInputStreamToFile(var2, var1);
      } catch (Throwable var6) {
         if (var2 != null) {
            try {
               var2.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (var2 != null) {
         var2.close();
      }

   }

   public static void copyURLToFile(URL var0, File var1, int var2, int var3) {
      URLConnection var4 = var0.openConnection();
      var4.setConnectTimeout(var2);
      var4.setReadTimeout(var3);
      InputStream var5 = var4.getInputStream();

      try {
         copyInputStreamToFile(var5, var1);
      } catch (Throwable var9) {
         if (var5 != null) {
            try {
               var5.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }
         }

         throw var9;
      }

      if (var5 != null) {
         var5.close();
      }

   }

   public static File createParentDirectories(File var0) {
      return mkdirs(getParentFile(var0));
   }

   static String decodeUrl(String var0) {
      String var1 = var0;
      if (var0 != null && var0.indexOf(37) >= 0) {
         int var2 = var0.length();
         StringBuilder var3 = new StringBuilder();
         ByteBuffer var4 = ByteBuffer.allocate(var2);
         int var5 = 0;

         while(true) {
            while(true) {
               if (var5 >= var2) {
                  var1 = var3.toString();
                  return var1;
               }

               if (var0.charAt(var5) != '%') {
                  break;
               }

               try {
                  while(true) {
                     byte var6 = (byte)Integer.parseInt(var0.substring(var5 + 1, var5 + 3), 16);
                     var4.put(var6);
                     var5 += 3;
                     if (var5 >= var2 || var0.charAt(var5) != '%') {
                        break;
                     }
                  }
               } catch (RuntimeException var10) {
                  break;
               } finally {
                  if (var4.position() > 0) {
                     var4.flip();
                     var3.append(StandardCharsets.UTF_8.decode(var4).toString());
                     var4.clear();
                  }

               }
            }

            var3.append(var0.charAt(var5++));
         }
      } else {
         return var1;
      }
   }

   public static File delete(File var0) {
      Objects.requireNonNull(var0, "file");
      Files.delete(var0.toPath());
      return var0;
   }

   public static void deleteDirectory(File var0) {
      Objects.requireNonNull(var0, "directory");
      if (var0.exists()) {
         if (!isSymlink(var0)) {
            cleanDirectory(var0);
         }

         delete(var0);
      }
   }

   private static void deleteDirectoryOnExit(File var0) {
      if (var0.exists()) {
         var0.deleteOnExit();
         if (!isSymlink(var0)) {
            cleanDirectoryOnExit(var0);
         }

      }
   }

   public static boolean deleteQuietly(File var0) {
      if (var0 == null) {
         return false;
      } else {
         try {
            if (var0.isDirectory()) {
               cleanDirectory(var0);
            }
         } catch (Exception var3) {
         }

         try {
            return var0.delete();
         } catch (Exception var2) {
            return false;
         }
      }
   }

   public static boolean directoryContains(File var0, File var1) {
      requireDirectoryExists(var0, "directory");
      if (var1 == null) {
         return false;
      } else {
         return var0.exists() && var1.exists() ? FilenameUtils.directoryContains(var0.getCanonicalPath(), var1.getCanonicalPath()) : false;
      }
   }

   private static void doCopyDirectory(File var0, File var1, FileFilter var2, List<String> var3, boolean var4, CopyOption... var5) {
      File[] var6 = listFiles(var0, var2);
      requireDirectoryIfExists(var1, "destDir");
      mkdirs(var1);
      requireCanWrite(var1, "destDir");
      File[] var7 = var6;
      int var8 = var6.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         File var10 = var7[var9];
         File var11 = new File(var1, var10.getName());
         if (var3 == null || !var3.contains(var10.getCanonicalPath())) {
            if (var10.isDirectory()) {
               doCopyDirectory(var10, var11, var2, var3, var4, var5);
            } else {
               copyFile(var10, var11, var5);
            }
         }
      }

      if (var4) {
         setLastModified(var0, var1);
      }

   }

   public static void forceDelete(File var0) {
      Objects.requireNonNull(var0, "file");

      Counters.PathCounters var1;
      try {
         var1 = PathUtils.delete(var0.toPath(), PathUtils.EMPTY_LINK_OPTION_ARRAY, StandardDeleteOption.OVERRIDE_READ_ONLY);
      } catch (IOException var3) {
         throw new IOException("Cannot delete file: " + var0, var3);
      }

      if (var1.getFileCounter().get() < 1L && var1.getDirectoryCounter().get() < 1L) {
         throw new FileNotFoundException("File does not exist: " + var0);
      }
   }

   public static void forceDeleteOnExit(File var0) {
      Objects.requireNonNull(var0, "file");
      if (var0.isDirectory()) {
         deleteDirectoryOnExit(var0);
      } else {
         var0.deleteOnExit();
      }

   }

   public static void forceMkdir(File var0) {
      mkdirs(var0);
   }

   public static void forceMkdirParent(File var0) {
      Objects.requireNonNull(var0, "file");
      File var1 = getParentFile(var0);
      if (var1 != null) {
         forceMkdir(var1);
      }
   }

   public static File getFile(File var0, String... var1) {
      Objects.requireNonNull(var0, "directory");
      Objects.requireNonNull(var1, "names");
      File var2 = var0;
      String[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         var2 = new File(var2, var6);
      }

      return var2;
   }

   public static File getFile(String... var0) {
      Objects.requireNonNull(var0, "names");
      File var1 = null;
      String[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         if (var1 == null) {
            var1 = new File(var5);
         } else {
            var1 = new File(var1, var5);
         }
      }

      return var1;
   }

   private static File getParentFile(File var0) {
      return var0 == null ? null : var0.getParentFile();
   }

   public static File getTempDirectory() {
      return new File(getTempDirectoryPath());
   }

   public static String getTempDirectoryPath() {
      return System.getProperty("java.io.tmpdir");
   }

   public static File getUserDirectory() {
      return new File(getUserDirectoryPath());
   }

   public static String getUserDirectoryPath() {
      return System.getProperty("user.home");
   }

   public static boolean isDirectory(File var0, LinkOption... var1) {
      return var0 != null && Files.isDirectory(var0.toPath(), var1);
   }

   public static boolean isEmptyDirectory(File var0) {
      return PathUtils.isEmptyDirectory(var0.toPath());
   }

   public static boolean isFileNewer(File var0, ChronoLocalDate var1) {
      return isFileNewer(var0, var1, LocalTime.now());
   }

   public static boolean isFileNewer(File var0, ChronoLocalDate var1, LocalTime var2) {
      Objects.requireNonNull(var1, "chronoLocalDate");
      Objects.requireNonNull(var2, "localTime");
      return isFileNewer(var0, var1.atTime(var2));
   }

   public static boolean isFileNewer(File var0, ChronoLocalDateTime<?> var1) {
      return isFileNewer(var0, var1, ZoneId.systemDefault());
   }

   public static boolean isFileNewer(File var0, ChronoLocalDateTime<?> var1, ZoneId var2) {
      Objects.requireNonNull(var1, "chronoLocalDateTime");
      Objects.requireNonNull(var2, "zoneId");
      return isFileNewer(var0, var1.atZone(var2));
   }

   public static boolean isFileNewer(File var0, ChronoZonedDateTime<?> var1) {
      Objects.requireNonNull(var1, "chronoZonedDateTime");
      return isFileNewer(var0, var1.toInstant());
   }

   public static boolean isFileNewer(File var0, Date var1) {
      Objects.requireNonNull(var1, "date");
      return isFileNewer(var0, var1.getTime());
   }

   public static boolean isFileNewer(File var0, File var1) {
      requireExists(var1, "reference");
      return isFileNewer(var0, lastModifiedUnchecked(var1));
   }

   public static boolean isFileNewer(File var0, Instant var1) {
      Objects.requireNonNull(var1, "instant");
      return isFileNewer(var0, var1.toEpochMilli());
   }

   public static boolean isFileNewer(File var0, long var1) {
      Objects.requireNonNull(var0, "file");
      return var0.exists() && lastModifiedUnchecked(var0) > var1;
   }

   public static boolean isFileOlder(File var0, ChronoLocalDate var1) {
      return isFileOlder(var0, var1, LocalTime.now());
   }

   public static boolean isFileOlder(File var0, ChronoLocalDate var1, LocalTime var2) {
      Objects.requireNonNull(var1, "chronoLocalDate");
      Objects.requireNonNull(var2, "localTime");
      return isFileOlder(var0, var1.atTime(var2));
   }

   public static boolean isFileOlder(File var0, ChronoLocalDateTime<?> var1) {
      return isFileOlder(var0, var1, ZoneId.systemDefault());
   }

   public static boolean isFileOlder(File var0, ChronoLocalDateTime<?> var1, ZoneId var2) {
      Objects.requireNonNull(var1, "chronoLocalDateTime");
      Objects.requireNonNull(var2, "zoneId");
      return isFileOlder(var0, var1.atZone(var2));
   }

   public static boolean isFileOlder(File var0, ChronoZonedDateTime<?> var1) {
      Objects.requireNonNull(var1, "chronoZonedDateTime");
      return isFileOlder(var0, var1.toInstant());
   }

   public static boolean isFileOlder(File var0, Date var1) {
      Objects.requireNonNull(var1, "date");
      return isFileOlder(var0, var1.getTime());
   }

   public static boolean isFileOlder(File var0, File var1) {
      requireExists(var1, "reference");
      return isFileOlder(var0, lastModifiedUnchecked(var1));
   }

   public static boolean isFileOlder(File var0, Instant var1) {
      Objects.requireNonNull(var1, "instant");
      return isFileOlder(var0, var1.toEpochMilli());
   }

   public static boolean isFileOlder(File var0, long var1) {
      Objects.requireNonNull(var0, "file");
      return var0.exists() && lastModifiedUnchecked(var0) < var1;
   }

   public static boolean isRegularFile(File var0, LinkOption... var1) {
      return var0 != null && Files.isRegularFile(var0.toPath(), var1);
   }

   public static boolean isSymlink(File var0) {
      return var0 != null && Files.isSymbolicLink(var0.toPath());
   }

   public static Iterator<File> iterateFiles(File var0, IOFileFilter var1, IOFileFilter var2) {
      return listFiles(var0, var1, var2).iterator();
   }

   public static Iterator<File> iterateFiles(File var0, String[] var1, boolean var2) {
      try {
         return StreamIterator.iterator(streamFiles(var0, var2, var1));
      } catch (IOException var4) {
         throw new UncheckedIOException(var0.toString(), var4);
      }
   }

   public static Iterator<File> iterateFilesAndDirs(File var0, IOFileFilter var1, IOFileFilter var2) {
      return listFilesAndDirs(var0, var1, var2).iterator();
   }

   public static long lastModified(File var0) {
      return Files.getLastModifiedTime((Path)Objects.requireNonNull(var0.toPath(), "file")).toMillis();
   }

   public static long lastModifiedUnchecked(File var0) {
      try {
         return lastModified(var0);
      } catch (IOException var2) {
         throw new UncheckedIOException(var0.toString(), var2);
      }
   }

   public static LineIterator lineIterator(File var0) {
      return lineIterator(var0, (String)null);
   }

   public static LineIterator lineIterator(File var0, String var1) {
      FileInputStream var2 = null;

      try {
         var2 = openInputStream(var0);
         return IOUtils.lineIterator(var2, (String)var1);
      } catch (RuntimeException | IOException var4) {
         Objects.requireNonNull(var4);
         IOUtils.closeQuietly(var2, var4::addSuppressed);
         throw var4;
      }
   }

   private static AccumulatorPathVisitor listAccumulate(File var0, IOFileFilter var1, IOFileFilter var2) {
      boolean var3 = var2 != null;
      FileEqualsFileFilter var4 = new FileEqualsFileFilter(var0);
      Object var5 = var3 ? var4.or(var2) : var4;
      AccumulatorPathVisitor var6 = new AccumulatorPathVisitor(Counters.noopPathCounters(), var1, (PathFilter)var5);
      Files.walkFileTree(var0.toPath(), Collections.emptySet(), toMaxDepth(var3), var6);
      return var6;
   }

   private static File[] listFiles(File var0, FileFilter var1) {
      requireDirectoryExists(var0, "directory");
      File[] var2 = var1 == null ? var0.listFiles() : var0.listFiles(var1);
      if (var2 == null) {
         throw new IOException("Unknown I/O error listing contents of directory: " + var0);
      } else {
         return var2;
      }
   }

   public static Collection<File> listFiles(File var0, IOFileFilter var1, IOFileFilter var2) {
      try {
         AccumulatorPathVisitor var3 = listAccumulate(var0, var1, var2);
         return (Collection)var3.getFileList().stream().map(Path::toFile).collect(Collectors.toList());
      } catch (IOException var4) {
         throw new UncheckedIOException(var0.toString(), var4);
      }
   }

   public static Collection<File> listFiles(File var0, String[] var1, boolean var2) {
      try {
         return toList(streamFiles(var0, var2, var1));
      } catch (IOException var4) {
         throw new UncheckedIOException(var0.toString(), var4);
      }
   }

   public static Collection<File> listFilesAndDirs(File var0, IOFileFilter var1, IOFileFilter var2) {
      try {
         AccumulatorPathVisitor var3 = listAccumulate(var0, var1, var2);
         List var4 = var3.getFileList();
         var4.addAll(var3.getDirList());
         return (Collection)var4.stream().map(Path::toFile).collect(Collectors.toList());
      } catch (IOException var5) {
         throw new UncheckedIOException(var0.toString(), var5);
      }
   }

   private static File mkdirs(File var0) {
      if (var0 != null && !var0.mkdirs() && !var0.isDirectory()) {
         throw new IOException("Cannot create directory '" + var0 + "'.");
      } else {
         return var0;
      }
   }

   public static void moveDirectory(File var0, File var1) {
      validateMoveParameters(var0, var1);
      requireDirectory(var0, "srcDir");
      requireAbsent(var1, "destDir");
      if (!var0.renameTo(var1)) {
         if (var1.getCanonicalPath().startsWith(var0.getCanonicalPath() + File.separator)) {
            throw new IOException("Cannot move directory: " + var0 + " to a subdirectory of itself: " + var1);
         }

         copyDirectory(var0, var1);
         deleteDirectory(var0);
         if (var0.exists()) {
            throw new IOException("Failed to delete original directory '" + var0 + "' after copy to '" + var1 + "'");
         }
      }

   }

   public static void moveDirectoryToDirectory(File var0, File var1, boolean var2) {
      validateMoveParameters(var0, var1);
      if (!var1.isDirectory()) {
         if (var1.exists()) {
            throw new IOException("Destination '" + var1 + "' is not a directory");
         }

         if (!var2) {
            throw new FileNotFoundException("Destination directory '" + var1 + "' does not exist [createDestDir=" + false + "]");
         }

         mkdirs(var1);
      }

      moveDirectory(var0, new File(var1, var0.getName()));
   }

   public static void moveFile(File var0, File var1) {
      moveFile(var0, var1, StandardCopyOption.COPY_ATTRIBUTES);
   }

   public static void moveFile(File var0, File var1, CopyOption... var2) {
      validateMoveParameters(var0, var1);
      requireFile(var0, "srcFile");
      requireAbsent(var1, (String)null);
      boolean var3 = var0.renameTo(var1);
      if (!var3) {
         copyFile(var0, var1, var2);
         if (!var0.delete()) {
            deleteQuietly(var1);
            throw new IOException("Failed to delete original file '" + var0 + "' after copy to '" + var1 + "'");
         }
      }

   }

   public static void moveFileToDirectory(File var0, File var1, boolean var2) {
      validateMoveParameters(var0, var1);
      if (!var1.exists() && var2) {
         mkdirs(var1);
      }

      requireExistsChecked(var1, "destDir");
      requireDirectory(var1, "destDir");
      moveFile(var0, new File(var1, var0.getName()));
   }

   public static void moveToDirectory(File var0, File var1, boolean var2) {
      validateMoveParameters(var0, var1);
      if (var0.isDirectory()) {
         moveDirectoryToDirectory(var0, var1, var2);
      } else {
         moveFileToDirectory(var0, var1, var2);
      }

   }

   public static FileInputStream openInputStream(File var0) {
      Objects.requireNonNull(var0, "file");
      return new FileInputStream(var0);
   }

   public static FileOutputStream openOutputStream(File var0) {
      return openOutputStream(var0, false);
   }

   public static FileOutputStream openOutputStream(File var0, boolean var1) {
      Objects.requireNonNull(var0, "file");
      if (var0.exists()) {
         requireFile(var0, "file");
         requireCanWrite(var0, "file");
      } else {
         createParentDirectories(var0);
      }

      return new FileOutputStream(var0, var1);
   }

   public static byte[] readFileToByteArray(File var0) {
      FileInputStream var1 = openInputStream(var0);

      byte[] var4;
      try {
         long var2 = var0.length();
         var4 = var2 > 0L ? IOUtils.toByteArray(var1, var2) : IOUtils.toByteArray((InputStream)var1);
      } catch (Throwable var6) {
         if (var1 != null) {
            try {
               var1.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (var1 != null) {
         var1.close();
      }

      return var4;
   }

   /** @deprecated */
   @Deprecated
   public static String readFileToString(File var0) {
      return readFileToString(var0, Charset.defaultCharset());
   }

   public static String readFileToString(File var0, Charset var1) {
      FileInputStream var2 = openInputStream(var0);

      String var3;
      try {
         var3 = IOUtils.toString((InputStream)var2, (Charset)Charsets.toCharset(var1));
      } catch (Throwable var6) {
         if (var2 != null) {
            try {
               var2.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (var2 != null) {
         var2.close();
      }

      return var3;
   }

   public static String readFileToString(File var0, String var1) {
      return readFileToString(var0, Charsets.toCharset(var1));
   }

   /** @deprecated */
   @Deprecated
   public static List<String> readLines(File var0) {
      return readLines(var0, Charset.defaultCharset());
   }

   public static List<String> readLines(File var0, Charset var1) {
      FileInputStream var2 = openInputStream(var0);

      List var3;
      try {
         var3 = IOUtils.readLines(var2, (Charset)Charsets.toCharset(var1));
      } catch (Throwable var6) {
         if (var2 != null) {
            try {
               var2.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (var2 != null) {
         var2.close();
      }

      return var3;
   }

   public static List<String> readLines(File var0, String var1) {
      return readLines(var0, Charsets.toCharset(var1));
   }

   private static void requireAbsent(File var0, String var1) {
      if (var0.exists()) {
         throw new FileExistsException(String.format("File element in parameter '%s' already exists: '%s'", var1, var0));
      }
   }

   private static void requireCanonicalPathsNotEquals(File var0, File var1) {
      String var2 = var0.getCanonicalPath();
      if (var2.equals(var1.getCanonicalPath())) {
         throw new IllegalArgumentException(String.format("File canonical paths are equal: '%s' (file1='%s', file2='%s')", var2, var0, var1));
      }
   }

   private static void requireCanWrite(File var0, String var1) {
      Objects.requireNonNull(var0, "file");
      if (!var0.canWrite()) {
         throw new IllegalArgumentException("File parameter '" + var1 + " is not writable: '" + var0 + "'");
      }
   }

   private static File requireDirectory(File var0, String var1) {
      Objects.requireNonNull(var0, var1);
      if (!var0.isDirectory()) {
         throw new IllegalArgumentException("Parameter '" + var1 + "' is not a directory: '" + var0 + "'");
      } else {
         return var0;
      }
   }

   private static File requireDirectoryExists(File var0, String var1) {
      requireExists(var0, var1);
      requireDirectory(var0, var1);
      return var0;
   }

   private static File requireDirectoryIfExists(File var0, String var1) {
      Objects.requireNonNull(var0, var1);
      if (var0.exists()) {
         requireDirectory(var0, var1);
      }

      return var0;
   }

   private static void requireEqualSizes(File var0, File var1, long var2, long var4) {
      if (var2 != var4) {
         throw new IOException("Failed to copy full contents from '" + var0 + "' to '" + var1 + "' Expected length: " + var2 + " Actual: " + var4);
      }
   }

   private static File requireExists(File var0, String var1) {
      Objects.requireNonNull(var0, var1);
      if (!var0.exists()) {
         throw new IllegalArgumentException("File system element for parameter '" + var1 + "' does not exist: '" + var0 + "'");
      } else {
         return var0;
      }
   }

   private static File requireExistsChecked(File var0, String var1) {
      Objects.requireNonNull(var0, var1);
      if (!var0.exists()) {
         throw new FileNotFoundException("File system element for parameter '" + var1 + "' does not exist: '" + var0 + "'");
      } else {
         return var0;
      }
   }

   private static File requireFile(File var0, String var1) {
      Objects.requireNonNull(var0, var1);
      if (!var0.isFile()) {
         throw new IllegalArgumentException("Parameter '" + var1 + "' is not a file: " + var0);
      } else {
         return var0;
      }
   }

   private static void requireFileCopy(File var0, File var1) {
      requireExistsChecked(var0, "source");
      Objects.requireNonNull(var1, "destination");
   }

   private static File requireFileIfExists(File var0, String var1) {
      Objects.requireNonNull(var0, var1);
      return var0.exists() ? requireFile(var0, var1) : var0;
   }

   private static void setLastModified(File var0, File var1) {
      Objects.requireNonNull(var0, "sourceFile");
      setLastModified(var1, lastModified(var0));
   }

   private static void setLastModified(File var0, long var1) {
      Objects.requireNonNull(var0, "file");
      if (!var0.setLastModified(var1)) {
         throw new IOException(String.format("Failed setLastModified(%s) on '%s'", var1, var0));
      }
   }

   public static long sizeOf(File var0) {
      requireExists(var0, "file");
      return var0.isDirectory() ? sizeOfDirectory0(var0) : var0.length();
   }

   private static long sizeOf0(File var0) {
      Objects.requireNonNull(var0, "file");
      return var0.isDirectory() ? sizeOfDirectory0(var0) : var0.length();
   }

   public static BigInteger sizeOfAsBigInteger(File var0) {
      requireExists(var0, "file");
      return var0.isDirectory() ? sizeOfDirectoryBig0(var0) : BigInteger.valueOf(var0.length());
   }

   private static BigInteger sizeOfBig0(File var0) {
      Objects.requireNonNull(var0, "fileOrDir");
      return var0.isDirectory() ? sizeOfDirectoryBig0(var0) : BigInteger.valueOf(var0.length());
   }

   public static long sizeOfDirectory(File var0) {
      return sizeOfDirectory0(requireDirectoryExists(var0, "directory"));
   }

   private static long sizeOfDirectory0(File var0) {
      Objects.requireNonNull(var0, "directory");
      File[] var1 = var0.listFiles();
      if (var1 == null) {
         return 0L;
      } else {
         long var2 = 0L;
         File[] var4 = var1;
         int var5 = var1.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File var7 = var4[var6];
            if (!isSymlink(var7)) {
               var2 += sizeOf0(var7);
               if (var2 < 0L) {
                  break;
               }
            }
         }

         return var2;
      }
   }

   public static BigInteger sizeOfDirectoryAsBigInteger(File var0) {
      return sizeOfDirectoryBig0(requireDirectoryExists(var0, "directory"));
   }

   private static BigInteger sizeOfDirectoryBig0(File var0) {
      Objects.requireNonNull(var0, "directory");
      File[] var1 = var0.listFiles();
      if (var1 == null) {
         return BigInteger.ZERO;
      } else {
         BigInteger var2 = BigInteger.ZERO;
         File[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File var6 = var3[var5];
            if (!isSymlink(var6)) {
               var2 = var2.add(sizeOfBig0(var6));
            }
         }

         return var2;
      }
   }

   public static Stream<File> streamFiles(File var0, boolean var1, String... var2) {
      IOFileFilter var3 = var2 == null ? FileFileFilter.INSTANCE : FileFileFilter.INSTANCE.and(new SuffixFileFilter(toSuffixes(var2)));
      return PathUtils.walk(var0.toPath(), var3, toMaxDepth(var1), false, FileVisitOption.FOLLOW_LINKS).map(Path::toFile);
   }

   public static File toFile(URL var0) {
      if (var0 != null && "file".equalsIgnoreCase(var0.getProtocol())) {
         String var1 = var0.getFile().replace('/', File.separatorChar);
         return new File(decodeUrl(var1));
      } else {
         return null;
      }
   }

   public static File[] toFiles(URL... var0) {
      if (IOUtils.length((Object[])var0) == 0) {
         return EMPTY_FILE_ARRAY;
      } else {
         File[] var1 = new File[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            URL var3 = var0[var2];
            if (var3 != null) {
               if (!"file".equalsIgnoreCase(var3.getProtocol())) {
                  throw new IllegalArgumentException("Can only convert file URL to a File: " + var3);
               }

               var1[var2] = toFile(var3);
            }
         }

         return var1;
      }
   }

   private static List<File> toList(Stream<File> var0) {
      return (List)var0.collect(Collectors.toList());
   }

   private static int toMaxDepth(boolean var0) {
      return var0 ? Integer.MAX_VALUE : 1;
   }

   private static String[] toSuffixes(String... var0) {
      Objects.requireNonNull(var0, "extensions");
      String[] var1 = new String[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = "." + var0[var2];
      }

      return var1;
   }

   public static void touch(File var0) {
      Objects.requireNonNull(var0, "file");
      if (!var0.exists()) {
         openOutputStream(var0).close();
      }

      setLastModified(var0, System.currentTimeMillis());
   }

   public static URL[] toURLs(File... var0) {
      Objects.requireNonNull(var0, "files");
      URL[] var1 = new URL[var0.length];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2] = var0[var2].toURI().toURL();
      }

      return var1;
   }

   private static void validateMoveParameters(File var0, File var1) {
      Objects.requireNonNull(var0, "source");
      Objects.requireNonNull(var1, "destination");
      if (!var0.exists()) {
         throw new FileNotFoundException("Source '" + var0 + "' does not exist");
      }
   }

   public static boolean waitFor(File var0, int var1) {
      Objects.requireNonNull(var0, "file");
      long var2 = System.currentTimeMillis() + (long)var1 * 1000L;
      boolean var4 = false;

      try {
         while(!var0.exists()) {
            long var5 = var2 - System.currentTimeMillis();
            if (var5 < 0L) {
               boolean var7 = false;
               return var7;
            }

            try {
               Thread.sleep(Math.min(100L, var5));
            } catch (InterruptedException var12) {
               var4 = true;
            } catch (Exception var13) {
               break;
            }
         }
      } finally {
         if (var4) {
            Thread.currentThread().interrupt();
         }

      }

      return true;
   }

   /** @deprecated */
   @Deprecated
   public static void write(File var0, CharSequence var1) {
      write(var0, var1, Charset.defaultCharset(), false);
   }

   /** @deprecated */
   @Deprecated
   public static void write(File var0, CharSequence var1, boolean var2) {
      write(var0, var1, Charset.defaultCharset(), var2);
   }

   public static void write(File var0, CharSequence var1, Charset var2) {
      write(var0, var1, var2, false);
   }

   public static void write(File var0, CharSequence var1, Charset var2, boolean var3) {
      writeStringToFile(var0, Objects.toString(var1, (String)null), var2, var3);
   }

   public static void write(File var0, CharSequence var1, String var2) {
      write(var0, var1, var2, false);
   }

   public static void write(File var0, CharSequence var1, String var2, boolean var3) {
      write(var0, var1, Charsets.toCharset(var2), var3);
   }

   public static void writeByteArrayToFile(File var0, byte[] var1) {
      writeByteArrayToFile(var0, var1, false);
   }

   public static void writeByteArrayToFile(File var0, byte[] var1, boolean var2) {
      writeByteArrayToFile(var0, var1, 0, var1.length, var2);
   }

   public static void writeByteArrayToFile(File var0, byte[] var1, int var2, int var3) {
      writeByteArrayToFile(var0, var1, var2, var3, false);
   }

   public static void writeByteArrayToFile(File var0, byte[] var1, int var2, int var3, boolean var4) {
      FileOutputStream var5 = openOutputStream(var0, var4);

      try {
         var5.write(var1, var2, var3);
      } catch (Throwable var9) {
         if (var5 != null) {
            try {
               var5.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }
         }

         throw var9;
      }

      if (var5 != null) {
         var5.close();
      }

   }

   public static void writeLines(File var0, Collection<?> var1) {
      writeLines(var0, (String)null, var1, (String)null, false);
   }

   public static void writeLines(File var0, Collection<?> var1, boolean var2) {
      writeLines(var0, (String)null, var1, (String)null, var2);
   }

   public static void writeLines(File var0, Collection<?> var1, String var2) {
      writeLines(var0, (String)null, var1, var2, false);
   }

   public static void writeLines(File var0, Collection<?> var1, String var2, boolean var3) {
      writeLines(var0, (String)null, var1, var2, var3);
   }

   public static void writeLines(File var0, String var1, Collection<?> var2) {
      writeLines(var0, var1, var2, (String)null, false);
   }

   public static void writeLines(File var0, String var1, Collection<?> var2, boolean var3) {
      writeLines(var0, var1, var2, (String)null, var3);
   }

   public static void writeLines(File var0, String var1, Collection<?> var2, String var3) {
      writeLines(var0, var1, var2, var3, false);
   }

   public static void writeLines(File var0, String var1, Collection<?> var2, String var3, boolean var4) {
      BufferedOutputStream var5 = new BufferedOutputStream(openOutputStream(var0, var4));

      try {
         IOUtils.writeLines(var2, var3, var5, (String)var1);
      } catch (Throwable var9) {
         try {
            var5.close();
         } catch (Throwable var8) {
            var9.addSuppressed(var8);
         }

         throw var9;
      }

      var5.close();
   }

   /** @deprecated */
   @Deprecated
   public static void writeStringToFile(File var0, String var1) {
      writeStringToFile(var0, var1, Charset.defaultCharset(), false);
   }

   /** @deprecated */
   @Deprecated
   public static void writeStringToFile(File var0, String var1, boolean var2) {
      writeStringToFile(var0, var1, Charset.defaultCharset(), var2);
   }

   public static void writeStringToFile(File var0, String var1, Charset var2) {
      writeStringToFile(var0, var1, var2, false);
   }

   public static void writeStringToFile(File var0, String var1, Charset var2, boolean var3) {
      FileOutputStream var4 = openOutputStream(var0, var3);

      try {
         IOUtils.write((String)var1, (OutputStream)var4, (Charset)var2);
      } catch (Throwable var8) {
         if (var4 != null) {
            try {
               var4.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (var4 != null) {
         var4.close();
      }

   }

   public static void writeStringToFile(File var0, String var1, String var2) {
      writeStringToFile(var0, var1, var2, false);
   }

   public static void writeStringToFile(File var0, String var1, String var2, boolean var3) {
      writeStringToFile(var0, var1, Charsets.toCharset(var2), var3);
   }

   static {
      ONE_MB_BI = ONE_KB_BI.multiply(ONE_KB_BI);
      ONE_GB_BI = ONE_KB_BI.multiply(ONE_MB_BI);
      ONE_TB_BI = ONE_KB_BI.multiply(ONE_GB_BI);
      ONE_PB_BI = ONE_KB_BI.multiply(ONE_TB_BI);
      ONE_EB_BI = ONE_KB_BI.multiply(ONE_PB_BI);
      ONE_ZB = BigInteger.valueOf(1024L).multiply(BigInteger.valueOf(1152921504606846976L));
      ONE_YB = ONE_KB_BI.multiply(ONE_ZB);
      EMPTY_FILE_ARRAY = new File[0];
   }
}
