package es.outlook.adriansrj.nbt.io;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface Deserializer<T> {
   T fromStream(InputStream var1) throws IOException;

   default T fromFile(File file) throws IOException {
      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
      Throwable var3 = null;

      Object var4;
      try {
         var4 = this.fromStream(bis);
      } catch (Throwable var13) {
         var3 = var13;
         throw var13;
      } finally {
         if (bis != null) {
            if (var3 != null) {
               try {
                  bis.close();
               } catch (Throwable var12) {
                  var3.addSuppressed(var12);
               }
            } else {
               bis.close();
            }
         }

      }

      return var4;
   }

   default T fromBytes(byte[] data) throws IOException {
      ByteArrayInputStream stream = new ByteArrayInputStream(data);
      return this.fromStream(stream);
   }

   default T fromResource(Class<?> clazz, String path) throws IOException {
      InputStream stream = clazz.getClassLoader().getResourceAsStream(path);
      Throwable var4 = null;

      Object var5;
      try {
         if (stream == null) {
            throw new IOException("resource \"" + path + "\" not found");
         }

         var5 = this.fromStream(stream);
      } catch (Throwable var14) {
         var4 = var14;
         throw var14;
      } finally {
         if (stream != null) {
            if (var4 != null) {
               try {
                  stream.close();
               } catch (Throwable var13) {
                  var4.addSuppressed(var13);
               }
            } else {
               stream.close();
            }
         }

      }

      return var5;
   }

   default T fromURL(URL url) throws IOException {
      InputStream stream = url.openStream();
      Throwable var3 = null;

      Object var4;
      try {
         var4 = this.fromStream(stream);
      } catch (Throwable var13) {
         var3 = var13;
         throw var13;
      } finally {
         if (stream != null) {
            if (var3 != null) {
               try {
                  stream.close();
               } catch (Throwable var12) {
                  var3.addSuppressed(var12);
               }
            } else {
               stream.close();
            }
         }

      }

      return var4;
   }
}
