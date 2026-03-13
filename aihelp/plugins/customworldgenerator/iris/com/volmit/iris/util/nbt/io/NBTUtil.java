package com.volmit.iris.util.nbt.io;

import com.volmit.iris.util.nbt.tag.Tag;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.util.zip.GZIPInputStream;

public final class NBTUtil {
   private NBTUtil() {
   }

   public static void write(NamedTag tag, File file, boolean compressed) {
      FileOutputStream var3 = new FileOutputStream(var1);

      try {
         (new NBTSerializer(var2)).toStream((NamedTag)var0, var3);
      } catch (Throwable var7) {
         try {
            var3.close();
         } catch (Throwable var6) {
            var7.addSuppressed(var6);
         }

         throw var7;
      }

      var3.close();
   }

   public static void write(NamedTag tag, OutputStream out, boolean compressed) {
      (new NBTSerializer(var2)).toStream(var0, var1);
   }

   public static void write(Tag<?> tag, OutputStream out, boolean compressed) {
      write(new NamedTag((String)null, var0), var1, var2);
   }

   public static void write(NamedTag tag, String file, boolean compressed) {
      write(var0, new File(var1), var2);
   }

   public static void write(NamedTag tag, File file) {
      write(var0, var1, true);
   }

   public static void write(NamedTag tag, String file) {
      write(var0, new File(var1), true);
   }

   public static void write(Tag<?> tag, File file, boolean compressed) {
      write(new NamedTag((String)null, var0), var1, var2);
   }

   public static void write(Tag<?> tag, String file, boolean compressed) {
      write(new NamedTag((String)null, var0), new File(var1), var2);
   }

   public static void write(Tag<?> tag, File file) {
      write(new NamedTag((String)null, var0), var1, true);
   }

   public static void write(Tag<?> tag, String file) {
      write(new NamedTag((String)null, var0), new File(var1), true);
   }

   public static NamedTag read(File file, boolean compressed) {
      FileInputStream var2 = new FileInputStream(var0);

      NamedTag var3;
      try {
         var3 = (new NBTDeserializer(var1)).fromStream(var2);
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

   public static NamedTag read(InputStream in, boolean compressed) {
      return (new NBTDeserializer(var1)).fromStream(var0);
   }

   public static NamedTag read(String file, boolean compressed) {
      return read(new File(var0), var1);
   }

   public static NamedTag read(File file) {
      FileInputStream var1 = new FileInputStream(var0);

      NamedTag var2;
      try {
         var2 = (new NBTDeserializer(false)).fromStream(detectDecompression(var1));
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

   public static NamedTag read(String file) {
      return read(new File(var0));
   }

   private static InputStream detectDecompression(InputStream is) {
      PushbackInputStream var1 = new PushbackInputStream(var0, 2);
      int var2 = (var1.read() & 255) + (var1.read() << 8);
      var1.unread(var2 >> 8);
      var1.unread(var2 & 255);
      return (InputStream)(var2 == 35615 ? new GZIPInputStream(var1) : var1);
   }
}
