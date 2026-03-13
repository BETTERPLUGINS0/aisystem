package com.volmit.iris.engine.data.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

public interface StringDeserializer<T> extends Deserializer<T> {
   T fromReader(Reader reader) throws IOException;

   default T fromString(String s) throws IOException {
      return this.fromReader(new StringReader(s));
   }

   default T fromStream(InputStream stream) throws IOException {
      InputStreamReader reader = new InputStreamReader(stream);

      Object var3;
      try {
         var3 = this.fromReader(reader);
      } catch (Throwable var6) {
         try {
            reader.close();
         } catch (Throwable var5) {
            var6.addSuppressed(var5);
         }

         throw var6;
      }

      reader.close();
      return var3;
   }

   default T fromFile(File file) throws IOException {
      FileReader reader = new FileReader(file);

      Object var3;
      try {
         var3 = this.fromReader(reader);
      } catch (Throwable var6) {
         try {
            reader.close();
         } catch (Throwable var5) {
            var6.addSuppressed(var5);
         }

         throw var6;
      }

      reader.close();
      return var3;
   }

   default T fromBytes(byte[] data) throws IOException {
      return this.fromReader(new StringReader(new String(data)));
   }
}
