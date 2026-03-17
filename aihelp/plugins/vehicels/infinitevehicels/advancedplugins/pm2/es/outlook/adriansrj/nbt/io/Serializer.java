package es.outlook.adriansrj.nbt.io;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public interface Serializer<T> {
   void toStream(T var1, OutputStream var2) throws IOException;

   default void toFile(T object, File file) throws IOException {
      BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
      Throwable var4 = null;

      try {
         this.toStream(object, bos);
      } catch (Throwable var13) {
         var4 = var13;
         throw var13;
      } finally {
         if (bos != null) {
            if (var4 != null) {
               try {
                  bos.close();
               } catch (Throwable var12) {
                  var4.addSuppressed(var12);
               }
            } else {
               bos.close();
            }
         }

      }

   }

   default byte[] toBytes(T object) throws IOException {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      this.toStream(object, bos);
      bos.close();
      return bos.toByteArray();
   }
}
