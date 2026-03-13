package com.volmit.iris.engine.data.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

public interface StringSerializer<T> extends Serializer<T> {
   void toWriter(T object, Writer writer) throws IOException;

   default String toString(T object) throws IOException {
      Writer writer = new StringWriter();
      this.toWriter(object, writer);
      writer.flush();
      return writer.toString();
   }

   default void toStream(T object, OutputStream stream) throws IOException {
      Writer writer = new OutputStreamWriter(stream);
      this.toWriter(object, writer);
      writer.flush();
   }

   default void toFile(T object, File file) throws IOException {
      FileWriter writer = new FileWriter(file);

      try {
         this.toWriter(object, writer);
      } catch (Throwable var7) {
         try {
            writer.close();
         } catch (Throwable var6) {
            var7.addSuppressed(var6);
         }

         throw var7;
      }

      writer.close();
   }
}
