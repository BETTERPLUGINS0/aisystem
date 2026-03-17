package es.outlook.adriansrj.nbt.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

public interface StringSerializer<T> extends Serializer<T> {
   void toWriter(T var1, Writer var2) throws IOException;

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
      Writer writer = new FileWriter(file);
      Throwable var4 = null;

      try {
         this.toWriter(object, writer);
      } catch (Throwable var13) {
         var4 = var13;
         throw var13;
      } finally {
         if (writer != null) {
            if (var4 != null) {
               try {
                  writer.close();
               } catch (Throwable var12) {
                  var4.addSuppressed(var12);
               }
            } else {
               writer.close();
            }
         }

      }

   }
}
