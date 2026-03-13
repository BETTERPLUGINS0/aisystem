package com.volmit.iris.util.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.volmit.iris.Iris;
import com.volmit.iris.util.dom4j.Document;
import com.volmit.iris.util.dom4j.DocumentHelper;
import com.volmit.iris.util.dom4j.io.OutputFormat;
import com.volmit.iris.util.dom4j.io.SAXReader;
import com.volmit.iris.util.dom4j.io.XMLWriter;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.scheduling.J;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.function.IOFunction;

public class IO {
   public static final char DIR_SEPARATOR_UNIX = '/';
   public static final char DIR_SEPARATOR_WINDOWS = '\\';
   public static final char DIR_SEPARATOR;
   public static final String LINE_SEPARATOR_UNIX = "\n";
   public static final String LINE_SEPARATOR_WINDOWS = "\r\n";
   public static final String LINE_SEPARATOR;
   private static final int DEFAULT_BUFFER_SIZE = 4096;
   private static final char[] hexArray;

   public static String decompress(String gz) {
      ByteArrayInputStream var1 = new ByteArrayInputStream(Base64.getUrlDecoder().decode(var0));
      GZIPInputStream var2 = new GZIPInputStream(var1);
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();
      fullTransfer(var2, var3, 256);
      var2.close();
      return var3.toString();
   }

   public static byte[] sdecompress(String compressed) {
      ByteArrayInputStream var1 = new ByteArrayInputStream(Base64.getUrlDecoder().decode(var0));
      GZIPInputStream var2 = new GZIPInputStream(var1);
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();
      fullTransfer(var2, var3, 256);
      var2.close();
      return var3.toByteArray();
   }

   public static String encode(byte[] data) {
      return Base64.getUrlEncoder().encodeToString(var0);
   }

   public static byte[] decode(String u) {
      return Base64.getUrlDecoder().decode(var0);
   }

   public static String hash(String b) {
      try {
         MessageDigest var1 = MessageDigest.getInstance("SHA-256");
         return bytesToHex(var1.digest(var0.getBytes(StandardCharsets.UTF_8)));
      } catch (NoSuchAlgorithmException var2) {
         Iris.reportError(var2);
         var2.printStackTrace();
         return "¯\\_(ツ)_/¯";
      }
   }

   public static long hashRecursive(File... bases) {
      LinkedList var1 = new LinkedList();
      HashSet var2 = new HashSet();
      Arrays.parallelSort(var0, Comparator.comparing(File::getName));
      var1.addAll(Arrays.asList(var0));

      try {
         CRC32 var3 = new CRC32();

         while(!var1.isEmpty()) {
            File var4 = (File)var1.removeFirst();
            if (var2.add(var4)) {
               if (var4.isDirectory()) {
                  File[] var5 = var4.listFiles();
                  if (var5 != null) {
                     Arrays.parallelSort(var5, Comparator.comparing(File::getName));
                     var1.addAll(Arrays.asList(var5));
                  }
               } else {
                  try {
                     CheckedInputStream var12 = new CheckedInputStream(readDeterministic(var4), var3);

                     try {
                        fullTransfer(var12, new VoidOutputStream(), 8192);
                     } catch (Throwable var9) {
                        try {
                           var12.close();
                        } catch (Throwable var8) {
                           var9.addSuppressed(var8);
                        }

                        throw var9;
                     }

                     var12.close();
                  } catch (IOException var10) {
                     Iris.reportError(var10);
                     var10.printStackTrace();
                  }
               }
            }
         }

         return var3.getValue();
      } catch (Throwable var11) {
         Iris.reportError(var11);
         var11.printStackTrace();
         return 0L;
      }
   }

   public static InputStream readDeterministic(File file) {
      if (!var0.getName().endsWith(".json")) {
         return new FileInputStream(var0);
      } else {
         JsonElement var1;
         try {
            FileReader var2 = new FileReader(var0);

            try {
               var1 = JsonParser.parseReader(var2);
            } catch (Throwable var10) {
               try {
                  var2.close();
               } catch (Throwable var9) {
                  var10.addSuppressed(var9);
               }

               throw var10;
            }

            var2.close();
         } catch (Throwable var11) {
            throw new IOException("Failed to read json file " + String.valueOf(var0), var11);
         }

         LinkedList var12 = new LinkedList();
         var12.add(var1);

         while(!var12.isEmpty()) {
            JsonElement var3 = (JsonElement)var12.pop();
            Object var4 = List.of();
            if (var3 instanceof JsonObject) {
               JsonObject var5 = (JsonObject)var3;
               Map var7 = var5.asMap();
               TreeMap var8 = new TreeMap(var7);
               var7.clear();
               var7.putAll(var8);
               var4 = var8.values();
            } else if (var3 instanceof JsonArray) {
               JsonArray var6 = (JsonArray)var3;
               var4 = var6.asList();
            }

            Stream var10000 = ((Collection)var4).stream().filter((var0x) -> {
               return var0x.isJsonObject() || var0x.isJsonArray();
            });
            Objects.requireNonNull(var12);
            var10000.forEach(var12::add);
         }

         return toInputStream(var1.toString());
      }
   }

   public static String hash(File b) {
      try {
         MessageDigest var1 = MessageDigest.getInstance("SHA-256");
         DigestInputStream var2 = new DigestInputStream(readDeterministic(var0), var1);
         fullTransfer(var2, new VoidOutputStream(), 8192);
         var2.close();
         return bytesToHex(var2.getMessageDigest().digest());
      } catch (Throwable var3) {
         Iris.reportError(var3);
         var3.printStackTrace();
         return "¯\\_(ツ)_/¯";
      }
   }

   public static String bytesToHex(byte[] bytes) {
      char[] var1 = new char[var0.length * 2];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         int var3 = var0[var2] & 255;
         var1[var2 * 2] = hexArray[var3 >>> 4];
         var1[var2 * 2 + 1] = hexArray[var3 & 15];
      }

      return (new String(var1)).toUpperCase();
   }

   public static String print(byte[] bytes) {
      String var10000 = Form.memSize((long)var0.length, 2);
      return var10000 + "[" + bytesToHex(var0) + "]";
   }

   public static String longsToHex(long[] bytes) {
      byte[] var1 = new byte[var0.length * 8];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2 * 8] = (byte)((int)(var0[var2] >>> 56));
         var1[var2 * 8 + 1] = (byte)((int)(var0[var2] >>> 48));
         var1[var2 * 8 + 2] = (byte)((int)(var0[var2] >>> 40));
         var1[var2 * 8 + 3] = (byte)((int)(var0[var2] >>> 32));
         var1[var2 * 8 + 4] = (byte)((int)(var0[var2] >>> 24));
         var1[var2 * 8 + 5] = (byte)((int)(var0[var2] >>> 16));
         var1[var2 * 8 + 6] = (byte)((int)(var0[var2] >>> 8));
         var1[var2 * 8 + 7] = (byte)((int)(var0[var2] >>> 0));
      }

      return bytesToHex(var1);
   }

   public static int transfer(InputStream in, OutputStream out, byte[] buffer) {
      int var3 = var0.read(var2);
      if (var3 != -1) {
         var1.write(var2, 0, var3);
      }

      return var3;
   }

   public static long transfer(InputStream in, OutputStream out, int targetBuffer, long totalSize) {
      long var5 = var3;
      long var7 = 0L;
      byte[] var9 = new byte[var2];
      boolean var10 = false;

      int var11;
      while((var11 = var0.read(var9, 0, (int)(var5 < (long)var2 ? var5 : (long)var2))) != -1) {
         var5 -= (long)var11;
         var1.write(var9, 0, var11);
         var7 += (long)var11;
         if (var5 <= 0L) {
            break;
         }
      }

      return var7;
   }

   public static long fillTransfer(InputStream in, OutputStream out) {
      return fullTransfer(var0, var1, 8192);
   }

   public static void deleteUp(File f) {
      if (var0.exists()) {
         var0.delete();
         if (var0.getParentFile().list().length == 0) {
            deleteUp(var0.getParentFile());
         }
      }

   }

   public static long fullTransfer(InputStream in, OutputStream out, int bufferSize) {
      long var3 = 0L;
      byte[] var5 = new byte[var2];

      int var7;
      for(boolean var6 = false; (var7 = var0.read(var5)) != -1; var3 += (long)var7) {
         var1.write(var5, 0, var7);
      }

      return var3;
   }

   public static void delete(File f) {
      if (var0 != null && var0.exists()) {
         if (var0.isDirectory()) {
            File[] var1 = var0.listFiles();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               File var4 = var1[var3];
               delete(var4);
            }
         }

         var0.delete();
      }
   }

   public static long size(File file) {
      long var1 = 0L;
      if (var0.exists()) {
         if (var0.isDirectory()) {
            File[] var3 = var0.listFiles();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               File var6 = var3[var5];
               var1 += size(var6);
            }
         } else {
            var1 += var0.length();
         }
      }

      return var1;
   }

   public static long count(File file) {
      long var1 = 0L;
      if (var0.exists()) {
         if (var0.isDirectory()) {
            File[] var3 = var0.listFiles();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               File var6 = var3[var5];
               var1 += count(var6);
            }
         } else {
            ++var1;
         }
      }

      return var1;
   }

   public static long transfer(InputStream in, OutputStream out, byte[] buf, int totalSize) {
      long var4 = (long)var3;
      long var6 = 0L;
      boolean var8 = false;

      int var9;
      while((var9 = var0.read(var2, 0, (int)(var4 < (long)var2.length ? var4 : (long)var2.length))) != -1) {
         var4 -= (long)var9;
         var1.write(var2, 0, var9);
         var6 += (long)var9;
         if (var4 <= 0L) {
            break;
         }
      }

      return var6;
   }

   public static void readEntry(File zipfile, String entryname, Consumer<InputStream> v) {
      ZipFile var3 = new ZipFile(var0);
      Throwable var4 = null;

      try {
         Enumeration var5 = var3.entries();

         while(var5.hasMoreElements()) {
            ZipEntry var6 = (ZipEntry)var5.nextElement();
            if (var1.equals(var6.getName())) {
               InputStream var7 = var3.getInputStream(var6);
               var2.accept(var7);
            }
         }
      } catch (Exception var11) {
         Iris.reportError(var11);
         var4 = var11.getCause();
      } finally {
         var3.close();
      }

      if (var4 != null) {
         throw new IOException("Failed to read zip entry, however it has been closed safely.", var4);
      }
   }

   public static void writeAll(File f, Object c) {
      try {
         var0.getParentFile().mkdirs();
      } catch (Throwable var3) {
      }

      PrintWriter var2 = new PrintWriter(new FileWriter(var0));
      var2.println(var1.toString());
      var2.close();
   }

   public static void writeAll(File f, OutputStream c) {
      var0.getParentFile().mkdirs();
      FileInputStream var2 = new FileInputStream(var0);
      fullTransfer(var2, var1, 8192);
      var2.close();
   }

   public static String readAll(File f) {
      FileReader var1;
      try {
         var1 = new FileReader(var0);
      } catch (IOException var5) {
         Iris.reportError(var5);
         throw var5;
      }

      BufferedReader var2 = new BufferedReader(var1);
      StringBuilder var3 = new StringBuilder();
      String var4 = "";

      while((var4 = var2.readLine()) != null) {
         var3.append(var4).append("\n");
      }

      var2.close();
      return var3.toString();
   }

   public static String readAll(InputStream in) {
      BufferedReader var1 = new BufferedReader(new InputStreamReader(var0));
      StringBuilder var2 = new StringBuilder();
      String var3 = "";

      while((var3 = var1.readLine()) != null) {
         var2.append(var3).append("\n");
      }

      var1.close();
      return var2.toString();
   }

   public static void touch(File file) {
      if (!var0.exists()) {
         FileOutputStream var1 = new FileOutputStream(var0);
         var1.close();
      }

      var0.setLastModified(System.currentTimeMillis());
   }

   public static void copyFile(File srcFile, File destFile) {
      copyFile(var0, var1, true);
   }

   public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) {
      if (var0 == null) {
         throw new NullPointerException("Source must not be null");
      } else if (var1 == null) {
         throw new NullPointerException("Destination must not be null");
      } else if (!var0.exists()) {
         throw new FileNotFoundException("Source '" + String.valueOf(var0) + "' does not exist");
      } else if (var0.isDirectory()) {
         throw new IOException("Source '" + String.valueOf(var0) + "' exists but is a directory");
      } else if (var0.getCanonicalPath().equals(var1.getCanonicalPath())) {
         String var10002 = String.valueOf(var0);
         throw new IOException("Source '" + var10002 + "' and destination '" + String.valueOf(var1) + "' are the same");
      } else if (var1.getParentFile() != null && !var1.getParentFile().exists() && !var1.getParentFile().mkdirs()) {
         throw new IOException("Destination '" + String.valueOf(var1) + "' directory cannot be created");
      } else if (var1.exists() && !var1.canWrite()) {
         throw new IOException("Destination '" + String.valueOf(var1) + "' exists but is read-only");
      } else {
         doCopyFile(var0, var1, var2);
      }
   }

   private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) {
      if (var1.exists() && var1.isDirectory()) {
         throw new IOException("Destination '" + String.valueOf(var1) + "' exists but is a directory");
      } else {
         FileInputStream var3 = new FileInputStream(var0);

         try {
            FileOutputStream var4 = new FileOutputStream(var1);

            try {
               copy((InputStream)var3, (OutputStream)var4);
            } finally {
               var4.close();
            }
         } finally {
            var3.close();
         }

         if (var0.length() != var1.length()) {
            String var10002 = String.valueOf(var0);
            throw new IOException("Failed to copy full contents from '" + var10002 + "' to '" + String.valueOf(var1) + "'");
         } else {
            if (var2) {
               var1.setLastModified(var0.lastModified());
            }

         }
      }
   }

   public static void copyDirectory(Path source, Path target) {
      Files.walk(var0).forEach((var2) -> {
         Path var3 = var1.resolve(var0.relativize(var2));

         try {
            if (Files.isDirectory(var2, new LinkOption[0])) {
               if (!Files.exists(var3, new LinkOption[0])) {
                  Files.createDirectories(var3);
               }
            } else {
               Files.copy(var2, var3, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
            }
         } catch (IOException var5) {
            Iris.error("Failed to copy " + String.valueOf(var3));
            var5.printStackTrace();
         }

      });
   }

   public static void closeQuietly(Reader input) {
      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (IOException var2) {
         Iris.reportError(var2);
      }

   }

   public static void closeQuietly(Writer output) {
      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (IOException var2) {
         Iris.reportError(var2);
      }

   }

   public static void closeQuietly(InputStream input) {
      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (IOException var2) {
         Iris.reportError(var2);
      }

   }

   public static void closeQuietly(OutputStream output) {
      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (IOException var2) {
         Iris.reportError(var2);
      }

   }

   public static byte[] toByteArray(InputStream input) {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      copy((InputStream)var0, (OutputStream)var1);
      return var1.toByteArray();
   }

   public static byte[] toByteArray(Reader input) {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      copy((Reader)var0, (OutputStream)var1);
      return var1.toByteArray();
   }

   public static byte[] toByteArray(Reader input, String encoding) {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      copy((Reader)var0, (OutputStream)var2, var1);
      return var2.toByteArray();
   }

   /** @deprecated */
   @Deprecated
   public static byte[] toByteArray(String input) {
      return var0.getBytes();
   }

   public static char[] toCharArray(InputStream is) {
      CharArrayWriter var1 = new CharArrayWriter();
      copy((InputStream)var0, (Writer)var1);
      return var1.toCharArray();
   }

   public static char[] toCharArray(InputStream is, String encoding) {
      CharArrayWriter var2 = new CharArrayWriter();
      copy((InputStream)var0, (Writer)var2, var1);
      return var2.toCharArray();
   }

   public static char[] toCharArray(Reader input) {
      CharArrayWriter var1 = new CharArrayWriter();
      copy((Reader)var0, (Writer)var1);
      return var1.toCharArray();
   }

   public static String toString(InputStream input) {
      StringWriter var1 = new StringWriter();
      copy((InputStream)var0, (Writer)var1);
      return var1.toString();
   }

   public static String toString(InputStream input, String encoding) {
      StringWriter var2 = new StringWriter();
      copy((InputStream)var0, (Writer)var2, var1);
      return var2.toString();
   }

   public static String toString(Reader input) {
      StringWriter var1 = new StringWriter();
      copy((Reader)var0, (Writer)var1);
      return var1.toString();
   }

   /** @deprecated */
   @Deprecated
   public static String toString(byte[] input) {
      return new String(var0);
   }

   /** @deprecated */
   @Deprecated
   public static String toString(byte[] input, String encoding) {
      return var1 == null ? new String(var0) : new String(var0, var1);
   }

   public static List<String> readLines(InputStream input) {
      InputStreamReader var1 = new InputStreamReader(var0);
      return readLines((Reader)var1);
   }

   public static List<String> readLines(InputStream input, String encoding) {
      if (var1 == null) {
         return readLines(var0);
      } else {
         InputStreamReader var2 = new InputStreamReader(var0, var1);
         return readLines((Reader)var2);
      }
   }

   public static List<String> readLines(Reader input) {
      BufferedReader var1 = new BufferedReader(var0);
      ArrayList var2 = new ArrayList();

      for(String var3 = var1.readLine(); var3 != null; var3 = var1.readLine()) {
         var2.add(var3);
      }

      return var2;
   }

   public static InputStream toInputStream(String input) {
      byte[] var1 = var0.getBytes();
      return new ByteArrayInputStream(var1);
   }

   public static InputStream toInputStream(String input, String encoding) {
      byte[] var2 = var1 != null ? var0.getBytes(var1) : var0.getBytes();
      return new ByteArrayInputStream(var2);
   }

   public static void write(byte[] data, OutputStream output) {
      if (var0 != null) {
         var1.write(var0);
      }

   }

   public static void write(byte[] data, Writer output) {
      if (var0 != null) {
         var1.write(new String(var0));
      }

   }

   public static void write(byte[] data, Writer output, String encoding) {
      if (var0 != null) {
         if (var2 == null) {
            write(var0, var1);
         } else {
            var1.write(new String(var0, var2));
         }
      }

   }

   public static void write(char[] data, Writer output) {
      if (var0 != null) {
         var1.write(var0);
      }

   }

   public static void write(char[] data, OutputStream output) {
      if (var0 != null) {
         var1.write((new String(var0)).getBytes());
      }

   }

   public static void write(char[] data, OutputStream output, String encoding) {
      if (var0 != null) {
         if (var2 == null) {
            write(var0, var1);
         } else {
            var1.write((new String(var0)).getBytes(var2));
         }
      }

   }

   public static void write(String data, Writer output) {
      if (var0 != null) {
         var1.write(var0);
      }

   }

   public static void write(String data, OutputStream output) {
      if (var0 != null) {
         var1.write(var0.getBytes());
      }

   }

   public static void write(String data, OutputStream output, String encoding) {
      if (var0 != null) {
         if (var2 == null) {
            write(var0, var1);
         } else {
            var1.write(var0.getBytes(var2));
         }
      }

   }

   public static void write(StringBuffer data, Writer output) {
      if (var0 != null) {
         var1.write(var0.toString());
      }

   }

   public static void write(StringBuffer data, OutputStream output) {
      if (var0 != null) {
         var1.write(var0.toString().getBytes());
      }

   }

   public static void write(StringBuffer data, OutputStream output, String encoding) {
      if (var0 != null) {
         if (var2 == null) {
            write(var0, var1);
         } else {
            var1.write(var0.toString().getBytes(var2));
         }
      }

   }

   public static void writeLines(Collection<String> lines, String lineEnding, OutputStream output) {
      if (var0 != null) {
         if (var1 == null) {
            var1 = LINE_SEPARATOR;
         }

         for(Iterator var3 = var0.iterator(); var3.hasNext(); var2.write(var1.getBytes())) {
            Object var4 = var3.next();
            if (var4 != null) {
               var2.write(var4.toString().getBytes());
            }
         }

      }
   }

   public static void writeLines(Collection<String> lines, String lineEnding, OutputStream output, String encoding) {
      if (var3 == null) {
         writeLines(var0, var1, var2);
      } else {
         if (var0 == null) {
            return;
         }

         if (var1 == null) {
            var1 = LINE_SEPARATOR;
         }

         for(Iterator var4 = var0.iterator(); var4.hasNext(); var2.write(var1.getBytes(var3))) {
            Object var5 = var4.next();
            if (var5 != null) {
               var2.write(var5.toString().getBytes(var3));
            }
         }
      }

   }

   public static void writeLines(Collection<String> lines, String lineEnding, Writer writer) {
      if (var0 != null) {
         if (var1 == null) {
            var1 = LINE_SEPARATOR;
         }

         for(Iterator var3 = var0.iterator(); var3.hasNext(); var2.write(var1)) {
            Object var4 = var3.next();
            if (var4 != null) {
               var2.write(var4.toString());
            }
         }

      }
   }

   public static int copy(InputStream input, OutputStream output) {
      long var2 = copyLarge(var0, var1);
      return var2 > 2147483647L ? -1 : (int)var2;
   }

   public static long copyLarge(InputStream input, OutputStream output) {
      byte[] var2 = new byte[4096];
      long var3 = 0L;

      int var6;
      for(boolean var5 = false; -1 != (var6 = var0.read(var2)); var3 += (long)var6) {
         var1.write(var2, 0, var6);
      }

      return var3;
   }

   public static void copy(InputStream input, Writer output) {
      InputStreamReader var2 = new InputStreamReader(var0);
      copy((Reader)var2, (Writer)var1);
   }

   public static void copy(InputStream input, Writer output, String encoding) {
      if (var2 == null) {
         copy(var0, var1);
      } else {
         InputStreamReader var3 = new InputStreamReader(var0, var2);
         copy((Reader)var3, (Writer)var1);
      }

   }

   public static int copy(Reader input, Writer output) {
      long var2 = copyLarge(var0, var1);
      return var2 > 2147483647L ? -1 : (int)var2;
   }

   public static long copyLarge(Reader input, Writer output) {
      char[] var2 = new char[4096];
      long var3 = 0L;

      int var6;
      for(boolean var5 = false; -1 != (var6 = var0.read(var2)); var3 += (long)var6) {
         var1.write(var2, 0, var6);
      }

      return var3;
   }

   public static void copy(Reader input, OutputStream output) {
      OutputStreamWriter var2 = new OutputStreamWriter(var1);
      copy((Reader)var0, (Writer)var2);
      var2.flush();
   }

   public static void copy(Reader input, OutputStream output, String encoding) {
      if (var2 == null) {
         copy(var0, var1);
      } else {
         OutputStreamWriter var3 = new OutputStreamWriter(var1, var2);
         copy((Reader)var0, (Writer)var3);
         var3.flush();
      }

   }

   public static boolean contentEquals(InputStream input1, InputStream input2) {
      if (!(var0 instanceof BufferedInputStream)) {
         var0 = new BufferedInputStream((InputStream)var0);
      }

      if (!(var1 instanceof BufferedInputStream)) {
         var1 = new BufferedInputStream((InputStream)var1);
      }

      int var3;
      for(int var2 = ((InputStream)var0).read(); -1 != var2; var2 = ((InputStream)var0).read()) {
         var3 = ((InputStream)var1).read();
         if (var2 != var3) {
            return false;
         }
      }

      var3 = ((InputStream)var1).read();
      return var3 == -1;
   }

   public static boolean contentEquals(Reader input1, Reader input2) {
      if (!(var0 instanceof BufferedReader)) {
         var0 = new BufferedReader((Reader)var0);
      }

      if (!(var1 instanceof BufferedReader)) {
         var1 = new BufferedReader((Reader)var1);
      }

      int var3;
      for(int var2 = ((Reader)var0).read(); -1 != var2; var2 = ((Reader)var0).read()) {
         var3 = ((Reader)var1).read();
         if (var2 != var3) {
            return false;
         }
      }

      var3 = ((Reader)var1).read();
      return var3 == -1;
   }

   public static void write(File file, Document doc) {
      try {
         var0.getParentFile().mkdirs();
         FileWriter var2 = new FileWriter(var0);

         try {
            (new XMLWriter(var2, OutputFormat.createPrettyPrint())).write(var1);
         } catch (Throwable var6) {
            try {
               var2.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }

            throw var6;
         }

         var2.close();
      } catch (Throwable var7) {
         throw var7;
      }
   }

   public static Document read(File file) {
      try {
         if (var0.exists()) {
            return (new SAXReader()).read(var0);
         } else {
            Document var1 = DocumentHelper.createDocument();
            var1.addElement("project").addAttribute("version", "4");
            return var1;
         }
      } catch (Throwable var2) {
         throw var2;
      }
   }

   public static <T extends Closeable> void write(File file, IOFunction<FileOutputStream, T> builder, IOConsumer<T> action) {
      File var3 = new File(var0.getParentFile(), ".tmp");
      var3.mkdirs();
      var3.deleteOnExit();
      File var4 = File.createTempFile("iris", ".bin", var3);

      try {
         FileChannel var5 = FileChannel.open(var0.toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.SYNC);

         try {
            lock(var5);
            Closeable var6 = (Closeable)var1.apply(new FileOutputStream(var4));

            try {
               var2.accept(var6);
            } catch (Throwable var17) {
               if (var6 != null) {
                  try {
                     var6.close();
                  } catch (Throwable var16) {
                     var17.addSuppressed(var16);
                  }
               }

               throw var17;
            }

            if (var6 != null) {
               var6.close();
            }

            Files.copy(var4.toPath(), Channels.newOutputStream(var5));
            var5.truncate(var4.length());
         } catch (Throwable var18) {
            if (var5 != null) {
               try {
                  var5.close();
               } catch (Throwable var15) {
                  var18.addSuppressed(var15);
               }
            }

            throw var18;
         }

         if (var5 != null) {
            var5.close();
         }
      } finally {
         var4.delete();
      }

   }

   public static FileLock lock(FileChannel channel) {
      while(true) {
         try {
            return var0.lock();
         } catch (OverlappingFileLockException var2) {
            J.sleep(1L);
         }
      }
   }

   static {
      DIR_SEPARATOR = File.separatorChar;
      hexArray = "0123456789ABCDEF".toCharArray();
      StringWriter var0 = new StringWriter(4);
      PrintWriter var1 = new PrintWriter(var0);
      var1.println();
      LINE_SEPARATOR = var0.toString();
   }
}
