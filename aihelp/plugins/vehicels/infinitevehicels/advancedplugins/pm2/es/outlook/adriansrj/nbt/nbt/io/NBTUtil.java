package es.outlook.adriansrj.nbt.nbt.io;

import es.outlook.adriansrj.nbt.nbt.tag.Tag;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.zip.GZIPInputStream;

public final class NBTUtil {
   private NBTUtil() {
   }

   public static void write(NamedTag var0, File var1, boolean var2) {
      FileOutputStream var3 = new FileOutputStream(var1);
      Throwable var4 = null;

      try {
         (new NBTSerializer(var2)).toStream((NamedTag)var0, var3);
      } catch (Throwable var13) {
         var4 = var13;
         throw var13;
      } finally {
         if (var3 != null) {
            if (var4 != null) {
               try {
                  var3.close();
               } catch (Throwable var12) {
                  var4.addSuppressed(var12);
               }
            } else {
               var3.close();
            }
         }

      }

   }

   public static void write(NamedTag var0, String var1, boolean var2) {
      write(var0, new File(var1), var2);
   }

   public static void write(NamedTag var0, File var1) {
      write(var0, var1, true);
   }

   public static void write(NamedTag var0, String var1) {
      write(var0, new File(var1), true);
   }

   public static void write(Tag<?> var0, File var1, boolean var2) {
      write(new NamedTag((String)null, var0), var1, var2);
   }

   public static void write(Tag<?> var0, String var1, boolean var2) {
      write(new NamedTag((String)null, var0), new File(var1), var2);
   }

   public static void write(Tag<?> var0, File var1) {
      write(new NamedTag((String)null, var0), var1, true);
   }

   public static void write(Tag<?> var0, String var1) {
      write(new NamedTag((String)null, var0), new File(var1), true);
   }

   public static void writeLE(NamedTag var0, File var1, boolean var2) {
      FileOutputStream var3 = new FileOutputStream(var1);
      Throwable var4 = null;

      try {
         (new NBTSerializer(var2, true)).toStream((NamedTag)var0, var3);
      } catch (Throwable var13) {
         var4 = var13;
         throw var13;
      } finally {
         if (var3 != null) {
            if (var4 != null) {
               try {
                  var3.close();
               } catch (Throwable var12) {
                  var4.addSuppressed(var12);
               }
            } else {
               var3.close();
            }
         }

      }

   }

   public static void writeLE(NamedTag var0, String var1, boolean var2) {
      writeLE(var0, new File(var1), var2);
   }

   public static void writeLE(NamedTag var0, File var1) {
      writeLE(var0, var1, true);
   }

   public static void writeLE(NamedTag var0, String var1) {
      writeLE(var0, new File(var1), true);
   }

   public static void writeLE(Tag<?> var0, File var1, boolean var2) {
      writeLE(new NamedTag((String)null, var0), var1, var2);
   }

   public static void writeLE(Tag<?> var0, String var1, boolean var2) {
      writeLE(new NamedTag((String)null, var0), new File(var1), var2);
   }

   public static void writeLE(Tag<?> var0, File var1) {
      writeLE(new NamedTag((String)null, var0), var1, true);
   }

   public static void writeLE(Tag<?> var0, String var1) {
      writeLE(new NamedTag((String)null, var0), new File(var1), true);
   }

   public static NamedTag read(File var0, boolean var1) {
      FileInputStream var2 = new FileInputStream(var0);
      Throwable var3 = null;

      NamedTag var4;
      try {
         var4 = (new NBTDeserializer(var1)).fromStream(var2);
      } catch (Throwable var13) {
         var3 = var13;
         throw var13;
      } finally {
         if (var2 != null) {
            if (var3 != null) {
               try {
                  var2.close();
               } catch (Throwable var12) {
                  var3.addSuppressed(var12);
               }
            } else {
               var2.close();
            }
         }

      }

      return var4;
   }

   public static NamedTag read(String var0, boolean var1) {
      return read(new File(var0), var1);
   }

   public static NamedTag read(File var0) {
      FileInputStream var1 = new FileInputStream(var0);
      Throwable var2 = null;

      NamedTag var3;
      try {
         var3 = (new NBTDeserializer(false)).fromStream(detectDecompression(var1));
      } catch (Throwable var12) {
         var2 = var12;
         throw var12;
      } finally {
         if (var1 != null) {
            if (var2 != null) {
               try {
                  var1.close();
               } catch (Throwable var11) {
                  var2.addSuppressed(var11);
               }
            } else {
               var1.close();
            }
         }

      }

      return var3;
   }

   public static NamedTag read(String var0) {
      return read(new File(var0));
   }

   public static NamedTag readLE(File var0, boolean var1) {
      FileInputStream var2 = new FileInputStream(var0);
      Throwable var3 = null;

      NamedTag var4;
      try {
         var4 = (new NBTDeserializer(var1, true)).fromStream(var2);
      } catch (Throwable var13) {
         var3 = var13;
         throw var13;
      } finally {
         if (var2 != null) {
            if (var3 != null) {
               try {
                  var2.close();
               } catch (Throwable var12) {
                  var3.addSuppressed(var12);
               }
            } else {
               var2.close();
            }
         }

      }

      return var4;
   }

   public static NamedTag readLE(String var0, boolean var1) {
      return readLE(new File(var0), var1);
   }

   public static NamedTag readLE(File var0) {
      FileInputStream var1 = new FileInputStream(var0);
      Throwable var2 = null;

      NamedTag var3;
      try {
         var3 = (new NBTDeserializer(false, true)).fromStream(detectDecompression(var1));
      } catch (Throwable var12) {
         var2 = var12;
         throw var12;
      } finally {
         if (var1 != null) {
            if (var2 != null) {
               try {
                  var1.close();
               } catch (Throwable var11) {
                  var2.addSuppressed(var11);
               }
            } else {
               var1.close();
            }
         }

      }

      return var3;
   }

   public static NamedTag readLE(String var0) {
      return readLE(new File(var0));
   }

   private static InputStream detectDecompression(InputStream var0) {
      PushbackInputStream var1 = new PushbackInputStream(var0, 2);
      int var2 = (var1.read() & 255) + (var1.read() << 8);
      var1.unread(var2 >> 8);
      var1.unread(var2 & 255);
      return (InputStream)(var2 == 35615 ? new GZIPInputStream(var1) : var1);
   }
}
