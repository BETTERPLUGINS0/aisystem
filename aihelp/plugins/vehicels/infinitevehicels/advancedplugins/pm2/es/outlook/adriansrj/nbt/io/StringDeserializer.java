package es.outlook.adriansrj.nbt.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

public interface StringDeserializer<T> extends Deserializer<T> {
   T fromReader(Reader var1) throws IOException;

   default T fromString(String s) throws IOException {
      return this.fromReader(new StringReader(s));
   }

   default T fromStream(InputStream stream) throws IOException {
      Reader reader = new InputStreamReader(stream);
      Throwable var3 = null;

      Object var4;
      try {
         var4 = this.fromReader(reader);
      } catch (Throwable var13) {
         var3 = var13;
         throw var13;
      } finally {
         if (reader != null) {
            if (var3 != null) {
               try {
                  reader.close();
               } catch (Throwable var12) {
                  var3.addSuppressed(var12);
               }
            } else {
               reader.close();
            }
         }

      }

      return var4;
   }

   default T fromFile(File file) throws IOException {
      Reader reader = new FileReader(file);
      Throwable var3 = null;

      Object var4;
      try {
         var4 = this.fromReader(reader);
      } catch (Throwable var13) {
         var3 = var13;
         throw var13;
      } finally {
         if (reader != null) {
            if (var3 != null) {
               try {
                  reader.close();
               } catch (Throwable var12) {
                  var3.addSuppressed(var12);
               }
            } else {
               reader.close();
            }
         }

      }

      return var4;
   }

   default T fromBytes(byte[] data) throws IOException {
      return this.fromReader(new StringReader(new String(data)));
   }
}
