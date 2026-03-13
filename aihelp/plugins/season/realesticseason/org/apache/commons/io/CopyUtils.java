package org.apache.commons.io;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.Charset;

/** @deprecated */
@Deprecated
public class CopyUtils {
   public static void copy(byte[] var0, OutputStream var1) {
      var1.write(var0);
   }

   /** @deprecated */
   @Deprecated
   public static void copy(byte[] var0, Writer var1) {
      ByteArrayInputStream var2 = new ByteArrayInputStream(var0);
      copy((InputStream)var2, (Writer)var1);
   }

   public static void copy(byte[] var0, Writer var1, String var2) {
      ByteArrayInputStream var3 = new ByteArrayInputStream(var0);
      copy((InputStream)var3, (Writer)var1, var2);
   }

   public static int copy(InputStream var0, OutputStream var1) {
      byte[] var2 = IOUtils.byteArray();

      int var3;
      int var4;
      for(var3 = 0; -1 != (var4 = var0.read(var2)); var3 += var4) {
         var1.write(var2, 0, var4);
      }

      return var3;
   }

   public static int copy(Reader var0, Writer var1) {
      char[] var2 = IOUtils.getCharArray();

      int var3;
      int var4;
      for(var3 = 0; -1 != (var4 = var0.read(var2)); var3 += var4) {
         var1.write(var2, 0, var4);
      }

      return var3;
   }

   /** @deprecated */
   @Deprecated
   public static void copy(InputStream var0, Writer var1) {
      InputStreamReader var2 = new InputStreamReader(var0, Charset.defaultCharset());
      copy((Reader)var2, (Writer)var1);
   }

   public static void copy(InputStream var0, Writer var1, String var2) {
      InputStreamReader var3 = new InputStreamReader(var0, var2);
      copy((Reader)var3, (Writer)var1);
   }

   /** @deprecated */
   @Deprecated
   public static void copy(Reader var0, OutputStream var1) {
      OutputStreamWriter var2 = new OutputStreamWriter(var1, Charset.defaultCharset());
      copy((Reader)var0, (Writer)var2);
      var2.flush();
   }

   public static void copy(Reader var0, OutputStream var1, String var2) {
      OutputStreamWriter var3 = new OutputStreamWriter(var1, var2);
      copy((Reader)var0, (Writer)var3);
      var3.flush();
   }

   /** @deprecated */
   @Deprecated
   public static void copy(String var0, OutputStream var1) {
      StringReader var2 = new StringReader(var0);
      OutputStreamWriter var3 = new OutputStreamWriter(var1, Charset.defaultCharset());
      copy((Reader)var2, (Writer)var3);
      var3.flush();
   }

   public static void copy(String var0, OutputStream var1, String var2) {
      StringReader var3 = new StringReader(var0);
      OutputStreamWriter var4 = new OutputStreamWriter(var1, var2);
      copy((Reader)var3, (Writer)var4);
      var4.flush();
   }

   public static void copy(String var0, Writer var1) {
      var1.write(var0);
   }
}
