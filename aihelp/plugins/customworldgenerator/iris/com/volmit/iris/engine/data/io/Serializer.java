package com.volmit.iris.engine.data.io;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public interface Serializer<T> {
   void toStream(T object, OutputStream out) throws IOException;

   default void toFile(T object, File file) throws IOException {
      BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

      try {
         this.toStream(object, bos);
      } catch (Throwable var7) {
         try {
            bos.close();
         } catch (Throwable var6) {
            var7.addSuppressed(var6);
         }

         throw var7;
      }

      bos.close();
   }

   default byte[] toBytes(T object) throws IOException {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      this.toStream(object, bos);
      bos.close();
      return bos.toByteArray();
   }
}
