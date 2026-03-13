package org.apache.commons.io.output;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class FileWriterWithEncoding extends Writer {
   private final Writer out;

   public FileWriterWithEncoding(String var1, String var2) {
      this(new File(var1), var2, false);
   }

   public FileWriterWithEncoding(String var1, String var2, boolean var3) {
      this(new File(var1), var2, var3);
   }

   public FileWriterWithEncoding(String var1, Charset var2) {
      this(new File(var1), var2, false);
   }

   public FileWriterWithEncoding(String var1, Charset var2, boolean var3) {
      this(new File(var1), var2, var3);
   }

   public FileWriterWithEncoding(String var1, CharsetEncoder var2) {
      this(new File(var1), var2, false);
   }

   public FileWriterWithEncoding(String var1, CharsetEncoder var2, boolean var3) {
      this(new File(var1), var2, var3);
   }

   public FileWriterWithEncoding(File var1, String var2) {
      this(var1, var2, false);
   }

   public FileWriterWithEncoding(File var1, String var2, boolean var3) {
      this.out = initWriter(var1, var2, var3);
   }

   public FileWriterWithEncoding(File var1, Charset var2) {
      this(var1, var2, false);
   }

   public FileWriterWithEncoding(File var1, Charset var2, boolean var3) {
      this.out = initWriter(var1, var2, var3);
   }

   public FileWriterWithEncoding(File var1, CharsetEncoder var2) {
      this(var1, var2, false);
   }

   public FileWriterWithEncoding(File var1, CharsetEncoder var2, boolean var3) {
      this.out = initWriter(var1, var2, var3);
   }

   private static Writer initWriter(File var0, Object var1, boolean var2) {
      Objects.requireNonNull(var0, "file");
      Objects.requireNonNull(var1, "encoding");
      OutputStream var3 = null;
      boolean var4 = var0.exists();

      try {
         var3 = Files.newOutputStream(var0.toPath(), var2 ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
         if (var1 instanceof Charset) {
            return new OutputStreamWriter(var3, (Charset)var1);
         } else {
            return var1 instanceof CharsetEncoder ? new OutputStreamWriter(var3, (CharsetEncoder)var1) : new OutputStreamWriter(var3, (String)var1);
         }
      } catch (RuntimeException | IOException var8) {
         try {
            IOUtils.close((Closeable)var3);
         } catch (IOException var7) {
            var8.addSuppressed(var7);
         }

         if (!var4) {
            FileUtils.deleteQuietly(var0);
         }

         throw var8;
      }
   }

   public void write(int var1) {
      this.out.write(var1);
   }

   public void write(char[] var1) {
      this.out.write(var1);
   }

   public void write(char[] var1, int var2, int var3) {
      this.out.write(var1, var2, var3);
   }

   public void write(String var1) {
      this.out.write(var1);
   }

   public void write(String var1, int var2, int var3) {
      this.out.write(var1, var2, var3);
   }

   public void flush() {
      this.out.flush();
   }

   public void close() {
      this.out.close();
   }
}
