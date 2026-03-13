package org.apache.commons.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.output.AppendableWriter;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.io.output.ThresholdingOutputStream;
import org.apache.commons.io.output.UnsynchronizedByteArrayOutputStream;

public class IOUtils {
   public static final int CR = 13;
   public static final int DEFAULT_BUFFER_SIZE = 8192;
   public static final char DIR_SEPARATOR;
   public static final char DIR_SEPARATOR_UNIX = '/';
   public static final char DIR_SEPARATOR_WINDOWS = '\\';
   public static final byte[] EMPTY_BYTE_ARRAY;
   public static final int EOF = -1;
   public static final int LF = 10;
   /** @deprecated */
   @Deprecated
   public static final String LINE_SEPARATOR;
   public static final String LINE_SEPARATOR_UNIX;
   public static final String LINE_SEPARATOR_WINDOWS;
   private static final ThreadLocal<byte[]> SKIP_BYTE_BUFFER;
   private static final ThreadLocal<char[]> SKIP_CHAR_BUFFER;

   public static BufferedInputStream buffer(InputStream var0) {
      Objects.requireNonNull(var0, "inputStream");
      return var0 instanceof BufferedInputStream ? (BufferedInputStream)var0 : new BufferedInputStream(var0);
   }

   public static BufferedInputStream buffer(InputStream var0, int var1) {
      Objects.requireNonNull(var0, "inputStream");
      return var0 instanceof BufferedInputStream ? (BufferedInputStream)var0 : new BufferedInputStream(var0, var1);
   }

   public static BufferedOutputStream buffer(OutputStream var0) {
      Objects.requireNonNull(var0, "outputStream");
      return var0 instanceof BufferedOutputStream ? (BufferedOutputStream)var0 : new BufferedOutputStream(var0);
   }

   public static BufferedOutputStream buffer(OutputStream var0, int var1) {
      Objects.requireNonNull(var0, "outputStream");
      return var0 instanceof BufferedOutputStream ? (BufferedOutputStream)var0 : new BufferedOutputStream(var0, var1);
   }

   public static BufferedReader buffer(Reader var0) {
      return var0 instanceof BufferedReader ? (BufferedReader)var0 : new BufferedReader(var0);
   }

   public static BufferedReader buffer(Reader var0, int var1) {
      return var0 instanceof BufferedReader ? (BufferedReader)var0 : new BufferedReader(var0, var1);
   }

   public static BufferedWriter buffer(Writer var0) {
      return var0 instanceof BufferedWriter ? (BufferedWriter)var0 : new BufferedWriter(var0);
   }

   public static BufferedWriter buffer(Writer var0, int var1) {
      return var0 instanceof BufferedWriter ? (BufferedWriter)var0 : new BufferedWriter(var0, var1);
   }

   public static byte[] byteArray() {
      return byteArray(8192);
   }

   public static byte[] byteArray(int var0) {
      return new byte[var0];
   }

   private static char[] charArray() {
      return charArray(8192);
   }

   private static char[] charArray(int var0) {
      return new char[var0];
   }

   public static void close(Closeable var0) {
      if (var0 != null) {
         var0.close();
      }

   }

   public static void close(Closeable... var0) {
      if (var0 != null) {
         Closeable[] var1 = var0;
         int var2 = var0.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Closeable var4 = var1[var3];
            close(var4);
         }
      }

   }

   public static void close(Closeable var0, IOConsumer<IOException> var1) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (IOException var3) {
            if (var1 != null) {
               var1.accept(var3);
            }
         }
      }

   }

   public static void close(URLConnection var0) {
      if (var0 instanceof HttpURLConnection) {
         ((HttpURLConnection)var0).disconnect();
      }

   }

   public static void closeQuietly(Closeable var0) {
      closeQuietly(var0, (Consumer)null);
   }

   public static void closeQuietly(Closeable... var0) {
      if (var0 != null) {
         Closeable[] var1 = var0;
         int var2 = var0.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Closeable var4 = var1[var3];
            closeQuietly(var4);
         }

      }
   }

   public static void closeQuietly(Closeable var0, Consumer<IOException> var1) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (IOException var3) {
            if (var1 != null) {
               var1.accept(var3);
            }
         }
      }

   }

   public static void closeQuietly(InputStream var0) {
      closeQuietly((Closeable)var0);
   }

   public static void closeQuietly(OutputStream var0) {
      closeQuietly((Closeable)var0);
   }

   public static void closeQuietly(Reader var0) {
      closeQuietly((Closeable)var0);
   }

   public static void closeQuietly(Selector var0) {
      closeQuietly((Closeable)var0);
   }

   public static void closeQuietly(ServerSocket var0) {
      closeQuietly((Closeable)var0);
   }

   public static void closeQuietly(Socket var0) {
      closeQuietly((Closeable)var0);
   }

   public static void closeQuietly(Writer var0) {
      closeQuietly((Closeable)var0);
   }

   public static long consume(InputStream var0) {
      return copyLarge((InputStream)var0, (OutputStream)NullOutputStream.NULL_OUTPUT_STREAM, (byte[])getByteArray());
   }

   public static boolean contentEquals(InputStream var0, InputStream var1) {
      if (var0 == var1) {
         return true;
      } else if (var0 != null && var1 != null) {
         byte[] var2 = getByteArray();
         byte[] var3 = byteArray();

         while(true) {
            int var4 = 0;
            int var5 = 0;

            for(int var8 = 0; var8 < 8192; ++var8) {
               if (var4 == var8) {
                  int var6;
                  do {
                     var6 = var0.read(var2, var4, 8192 - var4);
                  } while(var6 == 0);

                  if (var6 == -1) {
                     return var5 == var8 && var1.read() == -1;
                  }

                  var4 += var6;
               }

               if (var5 == var8) {
                  int var7;
                  do {
                     var7 = var1.read(var3, var5, 8192 - var5);
                  } while(var7 == 0);

                  if (var7 == -1) {
                     return var4 == var8 && var0.read() == -1;
                  }

                  var5 += var7;
               }

               if (var2[var8] != var3[var8]) {
                  return false;
               }
            }
         }
      } else {
         return false;
      }
   }

   public static boolean contentEquals(Reader var0, Reader var1) {
      if (var0 == var1) {
         return true;
      } else if (var0 != null && var1 != null) {
         char[] var2 = getCharArray();
         char[] var3 = charArray();

         while(true) {
            int var4 = 0;
            int var5 = 0;

            for(int var8 = 0; var8 < 8192; ++var8) {
               if (var4 == var8) {
                  int var6;
                  do {
                     var6 = var0.read(var2, var4, 8192 - var4);
                  } while(var6 == 0);

                  if (var6 == -1) {
                     return var5 == var8 && var1.read() == -1;
                  }

                  var4 += var6;
               }

               if (var5 == var8) {
                  int var7;
                  do {
                     var7 = var1.read(var3, var5, 8192 - var5);
                  } while(var7 == 0);

                  if (var7 == -1) {
                     return var4 == var8 && var0.read() == -1;
                  }

                  var5 += var7;
               }

               if (var2[var8] != var3[var8]) {
                  return false;
               }
            }
         }
      } else {
         return false;
      }
   }

   public static boolean contentEqualsIgnoreEOL(Reader var0, Reader var1) {
      if (var0 == var1) {
         return true;
      } else if (var0 == null ^ var1 == null) {
         return false;
      } else {
         BufferedReader var2 = toBufferedReader(var0);
         BufferedReader var3 = toBufferedReader(var1);
         String var4 = var2.readLine();

         String var5;
         for(var5 = var3.readLine(); var4 != null && var4.equals(var5); var5 = var3.readLine()) {
            var4 = var2.readLine();
         }

         return Objects.equals(var4, var5);
      }
   }

   public static int copy(InputStream var0, OutputStream var1) {
      long var2 = copyLarge(var0, var1);
      return var2 > 2147483647L ? -1 : (int)var2;
   }

   public static long copy(InputStream var0, OutputStream var1, int var2) {
      return copyLarge(var0, var1, byteArray(var2));
   }

   /** @deprecated */
   @Deprecated
   public static void copy(InputStream var0, Writer var1) {
      copy(var0, var1, Charset.defaultCharset());
   }

   public static void copy(InputStream var0, Writer var1, Charset var2) {
      InputStreamReader var3 = new InputStreamReader(var0, Charsets.toCharset(var2));
      copy((Reader)var3, (Writer)var1);
   }

   public static void copy(InputStream var0, Writer var1, String var2) {
      copy(var0, var1, Charsets.toCharset(var2));
   }

   public static long copy(Reader var0, Appendable var1) {
      return copy(var0, var1, CharBuffer.allocate(8192));
   }

   public static long copy(Reader var0, Appendable var1, CharBuffer var2) {
      long var3;
      int var5;
      for(var3 = 0L; -1 != (var5 = var0.read(var2)); var3 += (long)var5) {
         var2.flip();
         var1.append(var2, 0, var5);
      }

      return var3;
   }

   /** @deprecated */
   @Deprecated
   public static void copy(Reader var0, OutputStream var1) {
      copy(var0, var1, Charset.defaultCharset());
   }

   public static void copy(Reader var0, OutputStream var1, Charset var2) {
      OutputStreamWriter var3 = new OutputStreamWriter(var1, Charsets.toCharset(var2));
      copy((Reader)var0, (Writer)var3);
      var3.flush();
   }

   public static void copy(Reader var0, OutputStream var1, String var2) {
      copy(var0, var1, Charsets.toCharset(var2));
   }

   public static int copy(Reader var0, Writer var1) {
      long var2 = copyLarge(var0, var1);
      return var2 > 2147483647L ? -1 : (int)var2;
   }

   public static long copy(URL var0, File var1) {
      OutputStream var2 = Files.newOutputStream(((File)Objects.requireNonNull(var1, "file")).toPath());

      long var3;
      try {
         var3 = copy(var0, var2);
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

   public static long copy(URL var0, OutputStream var1) {
      InputStream var2 = ((URL)Objects.requireNonNull(var0, "url")).openStream();

      long var3;
      try {
         var3 = copyLarge(var2, var1);
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

   public static long copyLarge(InputStream var0, OutputStream var1) {
      return copy(var0, var1, 8192);
   }

   public static long copyLarge(InputStream var0, OutputStream var1, byte[] var2) {
      Objects.requireNonNull(var0, "inputStream");
      Objects.requireNonNull(var1, "outputStream");

      long var3;
      int var5;
      for(var3 = 0L; -1 != (var5 = var0.read(var2)); var3 += (long)var5) {
         var1.write(var2, 0, var5);
      }

      return var3;
   }

   public static long copyLarge(InputStream var0, OutputStream var1, long var2, long var4) {
      return copyLarge(var0, var1, var2, var4, getByteArray());
   }

   public static long copyLarge(InputStream var0, OutputStream var1, long var2, long var4, byte[] var6) {
      if (var2 > 0L) {
         skipFully(var0, var2);
      }

      if (var4 == 0L) {
         return 0L;
      } else {
         int var7 = var6.length;
         int var8 = var7;
         if (var4 > 0L && var4 < (long)var7) {
            var8 = (int)var4;
         }

         long var10 = 0L;

         int var9;
         while(var8 > 0 && -1 != (var9 = var0.read(var6, 0, var8))) {
            var1.write(var6, 0, var9);
            var10 += (long)var9;
            if (var4 > 0L) {
               var8 = (int)Math.min(var4 - var10, (long)var7);
            }
         }

         return var10;
      }
   }

   public static long copyLarge(Reader var0, Writer var1) {
      return copyLarge(var0, var1, getCharArray());
   }

   public static long copyLarge(Reader var0, Writer var1, char[] var2) {
      long var3;
      int var5;
      for(var3 = 0L; -1 != (var5 = var0.read(var2)); var3 += (long)var5) {
         var1.write(var2, 0, var5);
      }

      return var3;
   }

   public static long copyLarge(Reader var0, Writer var1, long var2, long var4) {
      return copyLarge(var0, var1, var2, var4, getCharArray());
   }

   public static long copyLarge(Reader var0, Writer var1, long var2, long var4, char[] var6) {
      if (var2 > 0L) {
         skipFully(var0, var2);
      }

      if (var4 == 0L) {
         return 0L;
      } else {
         int var7 = var6.length;
         if (var4 > 0L && var4 < (long)var6.length) {
            var7 = (int)var4;
         }

         long var9 = 0L;

         int var8;
         while(var7 > 0 && -1 != (var8 = var0.read(var6, 0, var7))) {
            var1.write(var6, 0, var8);
            var9 += (long)var8;
            if (var4 > 0L) {
               var7 = (int)Math.min(var4 - var9, (long)var6.length);
            }
         }

         return var9;
      }
   }

   static byte[] getByteArray() {
      return (byte[])SKIP_BYTE_BUFFER.get();
   }

   static char[] getCharArray() {
      return (char[])SKIP_CHAR_BUFFER.get();
   }

   public static int length(byte[] var0) {
      return var0 == null ? 0 : var0.length;
   }

   public static int length(char[] var0) {
      return var0 == null ? 0 : var0.length;
   }

   public static int length(CharSequence var0) {
      return var0 == null ? 0 : var0.length();
   }

   public static int length(Object[] var0) {
      return var0 == null ? 0 : var0.length;
   }

   public static LineIterator lineIterator(InputStream var0, Charset var1) {
      return new LineIterator(new InputStreamReader(var0, Charsets.toCharset(var1)));
   }

   public static LineIterator lineIterator(InputStream var0, String var1) {
      return lineIterator(var0, Charsets.toCharset(var1));
   }

   public static LineIterator lineIterator(Reader var0) {
      return new LineIterator(var0);
   }

   public static int read(InputStream var0, byte[] var1) {
      return read((InputStream)var0, (byte[])var1, 0, var1.length);
   }

   public static int read(InputStream var0, byte[] var1, int var2, int var3) {
      if (var3 < 0) {
         throw new IllegalArgumentException("Length must not be negative: " + var3);
      } else {
         int var4;
         int var6;
         for(var4 = var3; var4 > 0; var4 -= var6) {
            int var5 = var3 - var4;
            var6 = var0.read(var1, var2 + var5, var4);
            if (-1 == var6) {
               break;
            }
         }

         return var3 - var4;
      }
   }

   public static int read(ReadableByteChannel var0, ByteBuffer var1) {
      int var2 = var1.remaining();

      while(var1.remaining() > 0) {
         int var3 = var0.read(var1);
         if (-1 == var3) {
            break;
         }
      }

      return var2 - var1.remaining();
   }

   public static int read(Reader var0, char[] var1) {
      return read((Reader)var0, (char[])var1, 0, var1.length);
   }

   public static int read(Reader var0, char[] var1, int var2, int var3) {
      if (var3 < 0) {
         throw new IllegalArgumentException("Length must not be negative: " + var3);
      } else {
         int var4;
         int var6;
         for(var4 = var3; var4 > 0; var4 -= var6) {
            int var5 = var3 - var4;
            var6 = var0.read(var1, var2 + var5, var4);
            if (-1 == var6) {
               break;
            }
         }

         return var3 - var4;
      }
   }

   public static void readFully(InputStream var0, byte[] var1) {
      readFully((InputStream)var0, (byte[])var1, 0, var1.length);
   }

   public static void readFully(InputStream var0, byte[] var1, int var2, int var3) {
      int var4 = read(var0, var1, var2, var3);
      if (var4 != var3) {
         throw new EOFException("Length to read: " + var3 + " actual: " + var4);
      }
   }

   public static byte[] readFully(InputStream var0, int var1) {
      byte[] var2 = byteArray(var1);
      readFully((InputStream)var0, (byte[])var2, 0, var2.length);
      return var2;
   }

   public static void readFully(ReadableByteChannel var0, ByteBuffer var1) {
      int var2 = var1.remaining();
      int var3 = read(var0, var1);
      if (var3 != var2) {
         throw new EOFException("Length to read: " + var2 + " actual: " + var3);
      }
   }

   public static void readFully(Reader var0, char[] var1) {
      readFully((Reader)var0, (char[])var1, 0, var1.length);
   }

   public static void readFully(Reader var0, char[] var1, int var2, int var3) {
      int var4 = read(var0, var1, var2, var3);
      if (var4 != var3) {
         throw new EOFException("Length to read: " + var3 + " actual: " + var4);
      }
   }

   /** @deprecated */
   @Deprecated
   public static List<String> readLines(InputStream var0) {
      return readLines(var0, Charset.defaultCharset());
   }

   public static List<String> readLines(InputStream var0, Charset var1) {
      InputStreamReader var2 = new InputStreamReader(var0, Charsets.toCharset(var1));
      return readLines((Reader)var2);
   }

   public static List<String> readLines(InputStream var0, String var1) {
      return readLines(var0, Charsets.toCharset(var1));
   }

   public static List<String> readLines(Reader var0) {
      BufferedReader var1 = toBufferedReader(var0);
      ArrayList var2 = new ArrayList();

      String var3;
      while((var3 = var1.readLine()) != null) {
         var2.add(var3);
      }

      return var2;
   }

   public static byte[] resourceToByteArray(String var0) {
      return resourceToByteArray(var0, (ClassLoader)null);
   }

   public static byte[] resourceToByteArray(String var0, ClassLoader var1) {
      return toByteArray(resourceToURL(var0, var1));
   }

   public static String resourceToString(String var0, Charset var1) {
      return resourceToString(var0, var1, (ClassLoader)null);
   }

   public static String resourceToString(String var0, Charset var1, ClassLoader var2) {
      return toString(resourceToURL(var0, var2), var1);
   }

   public static URL resourceToURL(String var0) {
      return resourceToURL(var0, (ClassLoader)null);
   }

   public static URL resourceToURL(String var0, ClassLoader var1) {
      URL var2 = var1 == null ? IOUtils.class.getResource(var0) : var1.getResource(var0);
      if (var2 == null) {
         throw new IOException("Resource not found: " + var0);
      } else {
         return var2;
      }
   }

   public static long skip(InputStream var0, long var1) {
      if (var1 < 0L) {
         throw new IllegalArgumentException("Skip count must be non-negative, actual: " + var1);
      } else {
         long var3;
         long var6;
         for(var3 = var1; var3 > 0L; var3 -= var6) {
            byte[] var5 = getByteArray();
            var6 = (long)var0.read(var5, 0, (int)Math.min(var3, (long)var5.length));
            if (var6 < 0L) {
               break;
            }
         }

         return var1 - var3;
      }
   }

   public static long skip(ReadableByteChannel var0, long var1) {
      if (var1 < 0L) {
         throw new IllegalArgumentException("Skip count must be non-negative, actual: " + var1);
      } else {
         ByteBuffer var3 = ByteBuffer.allocate((int)Math.min(var1, 8192L));

         long var4;
         int var6;
         for(var4 = var1; var4 > 0L; var4 -= (long)var6) {
            var3.position(0);
            var3.limit((int)Math.min(var4, 8192L));
            var6 = var0.read(var3);
            if (var6 == -1) {
               break;
            }
         }

         return var1 - var4;
      }
   }

   public static long skip(Reader var0, long var1) {
      if (var1 < 0L) {
         throw new IllegalArgumentException("Skip count must be non-negative, actual: " + var1);
      } else {
         long var3;
         long var6;
         for(var3 = var1; var3 > 0L; var3 -= var6) {
            char[] var5 = getCharArray();
            var6 = (long)var0.read(var5, 0, (int)Math.min(var3, (long)var5.length));
            if (var6 < 0L) {
               break;
            }
         }

         return var1 - var3;
      }
   }

   public static void skipFully(InputStream var0, long var1) {
      if (var1 < 0L) {
         throw new IllegalArgumentException("Bytes to skip must not be negative: " + var1);
      } else {
         long var3 = skip(var0, var1);
         if (var3 != var1) {
            throw new EOFException("Bytes to skip: " + var1 + " actual: " + var3);
         }
      }
   }

   public static void skipFully(ReadableByteChannel var0, long var1) {
      if (var1 < 0L) {
         throw new IllegalArgumentException("Bytes to skip must not be negative: " + var1);
      } else {
         long var3 = skip(var0, var1);
         if (var3 != var1) {
            throw new EOFException("Bytes to skip: " + var1 + " actual: " + var3);
         }
      }
   }

   public static void skipFully(Reader var0, long var1) {
      long var3 = skip(var0, var1);
      if (var3 != var1) {
         throw new EOFException("Chars to skip: " + var1 + " actual: " + var3);
      }
   }

   public static InputStream toBufferedInputStream(InputStream var0) {
      return ByteArrayOutputStream.toBufferedInputStream(var0);
   }

   public static InputStream toBufferedInputStream(InputStream var0, int var1) {
      return ByteArrayOutputStream.toBufferedInputStream(var0, var1);
   }

   public static BufferedReader toBufferedReader(Reader var0) {
      return var0 instanceof BufferedReader ? (BufferedReader)var0 : new BufferedReader(var0);
   }

   public static BufferedReader toBufferedReader(Reader var0, int var1) {
      return var0 instanceof BufferedReader ? (BufferedReader)var0 : new BufferedReader(var0, var1);
   }

   public static byte[] toByteArray(InputStream var0) {
      UnsynchronizedByteArrayOutputStream var1 = new UnsynchronizedByteArrayOutputStream();

      byte[] var3;
      try {
         ThresholdingOutputStream var2 = new ThresholdingOutputStream(Integer.MAX_VALUE, (var0x) -> {
            throw new IllegalArgumentException(String.format("Cannot read more than %,d into a byte array", Integer.MAX_VALUE));
         }, (var1x) -> {
            return var1;
         });

         try {
            copy((InputStream)var0, (OutputStream)var2);
            var3 = var1.toByteArray();
         } catch (Throwable var7) {
            try {
               var2.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }

            throw var7;
         }

         var2.close();
      } catch (Throwable var8) {
         try {
            var1.close();
         } catch (Throwable var5) {
            var8.addSuppressed(var5);
         }

         throw var8;
      }

      var1.close();
      return var3;
   }

   public static byte[] toByteArray(InputStream var0, int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Size must be equal or greater than zero: " + var1);
      } else if (var1 == 0) {
         return EMPTY_BYTE_ARRAY;
      } else {
         byte[] var2 = byteArray(var1);

         int var3;
         int var4;
         for(var3 = 0; var3 < var1 && (var4 = var0.read(var2, var3, var1 - var3)) != -1; var3 += var4) {
         }

         if (var3 != var1) {
            throw new IOException("Unexpected read size, current: " + var3 + ", expected: " + var1);
         } else {
            return var2;
         }
      }
   }

   public static byte[] toByteArray(InputStream var0, long var1) {
      if (var1 > 2147483647L) {
         throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + var1);
      } else {
         return toByteArray(var0, (int)var1);
      }
   }

   /** @deprecated */
   @Deprecated
   public static byte[] toByteArray(Reader var0) {
      return toByteArray(var0, Charset.defaultCharset());
   }

   public static byte[] toByteArray(Reader var0, Charset var1) {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();

      byte[] var3;
      try {
         copy((Reader)var0, (OutputStream)var2, (Charset)var1);
         var3 = var2.toByteArray();
      } catch (Throwable var6) {
         try {
            var2.close();
         } catch (Throwable var5) {
            var6.addSuppressed(var5);
         }

         throw var6;
      }

      var2.close();
      return var3;
   }

   public static byte[] toByteArray(Reader var0, String var1) {
      return toByteArray(var0, Charsets.toCharset(var1));
   }

   /** @deprecated */
   @Deprecated
   public static byte[] toByteArray(String var0) {
      return var0.getBytes(Charset.defaultCharset());
   }

   public static byte[] toByteArray(URI var0) {
      return toByteArray(var0.toURL());
   }

   public static byte[] toByteArray(URL var0) {
      URLConnection var1 = var0.openConnection();

      byte[] var2;
      try {
         var2 = toByteArray(var1);
      } finally {
         close(var1);
      }

      return var2;
   }

   public static byte[] toByteArray(URLConnection var0) {
      InputStream var1 = var0.getInputStream();

      byte[] var2;
      try {
         var2 = toByteArray(var1);
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

      return var2;
   }

   /** @deprecated */
   @Deprecated
   public static char[] toCharArray(InputStream var0) {
      return toCharArray(var0, Charset.defaultCharset());
   }

   public static char[] toCharArray(InputStream var0, Charset var1) {
      CharArrayWriter var2 = new CharArrayWriter();
      copy((InputStream)var0, (Writer)var2, (Charset)var1);
      return var2.toCharArray();
   }

   public static char[] toCharArray(InputStream var0, String var1) {
      return toCharArray(var0, Charsets.toCharset(var1));
   }

   public static char[] toCharArray(Reader var0) {
      CharArrayWriter var1 = new CharArrayWriter();
      copy((Reader)var0, (Writer)var1);
      return var1.toCharArray();
   }

   /** @deprecated */
   @Deprecated
   public static InputStream toInputStream(CharSequence var0) {
      return toInputStream(var0, Charset.defaultCharset());
   }

   public static InputStream toInputStream(CharSequence var0, Charset var1) {
      return toInputStream(var0.toString(), var1);
   }

   public static InputStream toInputStream(CharSequence var0, String var1) {
      return toInputStream(var0, Charsets.toCharset(var1));
   }

   /** @deprecated */
   @Deprecated
   public static InputStream toInputStream(String var0) {
      return toInputStream(var0, Charset.defaultCharset());
   }

   public static InputStream toInputStream(String var0, Charset var1) {
      return new ByteArrayInputStream(var0.getBytes(Charsets.toCharset(var1)));
   }

   public static InputStream toInputStream(String var0, String var1) {
      byte[] var2 = var0.getBytes(Charsets.toCharset(var1));
      return new ByteArrayInputStream(var2);
   }

   /** @deprecated */
   @Deprecated
   public static String toString(byte[] var0) {
      return new String(var0, Charset.defaultCharset());
   }

   public static String toString(byte[] var0, String var1) {
      return new String(var0, Charsets.toCharset(var1));
   }

   /** @deprecated */
   @Deprecated
   public static String toString(InputStream var0) {
      return toString(var0, Charset.defaultCharset());
   }

   public static String toString(InputStream var0, Charset var1) {
      StringBuilderWriter var2 = new StringBuilderWriter();

      String var3;
      try {
         copy((InputStream)var0, (Writer)var2, (Charset)var1);
         var3 = var2.toString();
      } catch (Throwable var6) {
         try {
            var2.close();
         } catch (Throwable var5) {
            var6.addSuppressed(var5);
         }

         throw var6;
      }

      var2.close();
      return var3;
   }

   public static String toString(InputStream var0, String var1) {
      return toString(var0, Charsets.toCharset(var1));
   }

   public static String toString(Reader var0) {
      StringBuilderWriter var1 = new StringBuilderWriter();

      String var2;
      try {
         copy((Reader)var0, (Writer)var1);
         var2 = var1.toString();
      } catch (Throwable var5) {
         try {
            var1.close();
         } catch (Throwable var4) {
            var5.addSuppressed(var4);
         }

         throw var5;
      }

      var1.close();
      return var2;
   }

   /** @deprecated */
   @Deprecated
   public static String toString(URI var0) {
      return toString(var0, Charset.defaultCharset());
   }

   public static String toString(URI var0, Charset var1) {
      return toString(var0.toURL(), Charsets.toCharset(var1));
   }

   public static String toString(URI var0, String var1) {
      return toString(var0, Charsets.toCharset(var1));
   }

   /** @deprecated */
   @Deprecated
   public static String toString(URL var0) {
      return toString(var0, Charset.defaultCharset());
   }

   public static String toString(URL var0, Charset var1) {
      InputStream var2 = var0.openStream();

      String var3;
      try {
         var3 = toString(var2, var1);
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

   public static String toString(URL var0, String var1) {
      return toString(var0, Charsets.toCharset(var1));
   }

   public static void write(byte[] var0, OutputStream var1) {
      if (var0 != null) {
         var1.write(var0);
      }

   }

   /** @deprecated */
   @Deprecated
   public static void write(byte[] var0, Writer var1) {
      write(var0, var1, Charset.defaultCharset());
   }

   public static void write(byte[] var0, Writer var1, Charset var2) {
      if (var0 != null) {
         var1.write(new String(var0, Charsets.toCharset(var2)));
      }

   }

   public static void write(byte[] var0, Writer var1, String var2) {
      write(var0, var1, Charsets.toCharset(var2));
   }

   /** @deprecated */
   @Deprecated
   public static void write(char[] var0, OutputStream var1) {
      write(var0, var1, Charset.defaultCharset());
   }

   public static void write(char[] var0, OutputStream var1, Charset var2) {
      if (var0 != null) {
         var1.write((new String(var0)).getBytes(Charsets.toCharset(var2)));
      }

   }

   public static void write(char[] var0, OutputStream var1, String var2) {
      write(var0, var1, Charsets.toCharset(var2));
   }

   public static void write(char[] var0, Writer var1) {
      if (var0 != null) {
         var1.write(var0);
      }

   }

   /** @deprecated */
   @Deprecated
   public static void write(CharSequence var0, OutputStream var1) {
      write(var0, var1, Charset.defaultCharset());
   }

   public static void write(CharSequence var0, OutputStream var1, Charset var2) {
      if (var0 != null) {
         write(var0.toString(), var1, var2);
      }

   }

   public static void write(CharSequence var0, OutputStream var1, String var2) {
      write(var0, var1, Charsets.toCharset(var2));
   }

   public static void write(CharSequence var0, Writer var1) {
      if (var0 != null) {
         write(var0.toString(), var1);
      }

   }

   /** @deprecated */
   @Deprecated
   public static void write(String var0, OutputStream var1) {
      write(var0, var1, Charset.defaultCharset());
   }

   public static void write(String var0, OutputStream var1, Charset var2) {
      if (var0 != null) {
         var1.write(var0.getBytes(Charsets.toCharset(var2)));
      }

   }

   public static void write(String var0, OutputStream var1, String var2) {
      write(var0, var1, Charsets.toCharset(var2));
   }

   public static void write(String var0, Writer var1) {
      if (var0 != null) {
         var1.write(var0);
      }

   }

   /** @deprecated */
   @Deprecated
   public static void write(StringBuffer var0, OutputStream var1) {
      write(var0, var1, (String)null);
   }

   /** @deprecated */
   @Deprecated
   public static void write(StringBuffer var0, OutputStream var1, String var2) {
      if (var0 != null) {
         var1.write(var0.toString().getBytes(Charsets.toCharset(var2)));
      }

   }

   /** @deprecated */
   @Deprecated
   public static void write(StringBuffer var0, Writer var1) {
      if (var0 != null) {
         var1.write(var0.toString());
      }

   }

   public static void writeChunked(byte[] var0, OutputStream var1) {
      if (var0 != null) {
         int var2 = var0.length;

         int var4;
         for(int var3 = 0; var2 > 0; var3 += var4) {
            var4 = Math.min(var2, 8192);
            var1.write(var0, var3, var4);
            var2 -= var4;
         }
      }

   }

   public static void writeChunked(char[] var0, Writer var1) {
      if (var0 != null) {
         int var2 = var0.length;

         int var4;
         for(int var3 = 0; var2 > 0; var3 += var4) {
            var4 = Math.min(var2, 8192);
            var1.write(var0, var3, var4);
            var2 -= var4;
         }
      }

   }

   /** @deprecated */
   @Deprecated
   public static void writeLines(Collection<?> var0, String var1, OutputStream var2) {
      writeLines(var0, var1, var2, Charset.defaultCharset());
   }

   public static void writeLines(Collection<?> var0, String var1, OutputStream var2, Charset var3) {
      if (var0 != null) {
         if (var1 == null) {
            var1 = System.lineSeparator();
         }

         Charset var4 = Charsets.toCharset(var3);

         for(Iterator var5 = var0.iterator(); var5.hasNext(); var2.write(var1.getBytes(var4))) {
            Object var6 = var5.next();
            if (var6 != null) {
               var2.write(var6.toString().getBytes(var4));
            }
         }

      }
   }

   public static void writeLines(Collection<?> var0, String var1, OutputStream var2, String var3) {
      writeLines(var0, var1, var2, Charsets.toCharset(var3));
   }

   public static void writeLines(Collection<?> var0, String var1, Writer var2) {
      if (var0 != null) {
         if (var1 == null) {
            var1 = System.lineSeparator();
         }

         for(Iterator var3 = var0.iterator(); var3.hasNext(); var2.write(var1)) {
            Object var4 = var3.next();
            if (var4 != null) {
               var2.write(var4.toString());
            }
         }

      }
   }

   public static Writer writer(Appendable var0) {
      Objects.requireNonNull(var0, "appendable");
      if (var0 instanceof Writer) {
         return (Writer)var0;
      } else {
         return (Writer)(var0 instanceof StringBuilder ? new StringBuilderWriter((StringBuilder)var0) : new AppendableWriter(var0));
      }
   }

   static {
      DIR_SEPARATOR = File.separatorChar;
      EMPTY_BYTE_ARRAY = new byte[0];
      LINE_SEPARATOR = System.lineSeparator();
      LINE_SEPARATOR_UNIX = StandardLineSeparator.LF.getString();
      LINE_SEPARATOR_WINDOWS = StandardLineSeparator.CRLF.getString();
      SKIP_BYTE_BUFFER = ThreadLocal.withInitial(IOUtils::byteArray);
      SKIP_CHAR_BUFFER = ThreadLocal.withInitial(IOUtils::charArray);
   }
}
