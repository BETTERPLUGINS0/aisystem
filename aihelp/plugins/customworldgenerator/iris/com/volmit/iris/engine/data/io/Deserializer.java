package com.volmit.iris.engine.data.io;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface Deserializer<T> {
   T fromStream(InputStream stream) throws IOException;

   default T fromFile(File file) throws IOException {
      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

      Object var3;
      try {
         var3 = this.fromStream(bis);
      } catch (Throwable var6) {
         try {
            bis.close();
         } catch (Throwable var5) {
            var6.addSuppressed(var5);
         }

         throw var6;
      }

      bis.close();
      return var3;
   }

   default T fromBytes(byte[] data) throws IOException {
      ByteArrayInputStream stream = new ByteArrayInputStream(data);
      return this.fromStream(stream);
   }

   default T fromResource(Class<?> clazz, String path) throws IOException {
      InputStream stream = clazz.getClassLoader().getResourceAsStream(path);

      Object var4;
      try {
         if (stream == null) {
            throw new IOException("resource \"" + path + "\" not found");
         }

         var4 = this.fromStream(stream);
      } catch (Throwable var7) {
         if (stream != null) {
            try {
               stream.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (stream != null) {
         stream.close();
      }

      return var4;
   }

   default T fromURL(URL url) throws IOException {
      InputStream stream = url.openStream();

      Object var3;
      try {
         var3 = this.fromStream(stream);
      } catch (Throwable var6) {
         if (stream != null) {
            try {
               stream.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (stream != null) {
         stream.close();
      }

      return var3;
   }
}
