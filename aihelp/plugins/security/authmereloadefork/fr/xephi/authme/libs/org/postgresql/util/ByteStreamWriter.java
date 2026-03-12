package fr.xephi.authme.libs.org.postgresql.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public interface ByteStreamWriter {
   int getLength();

   void writeTo(ByteStreamWriter.ByteStreamTarget var1) throws IOException;

   static ByteStreamWriter of(ByteBuffer... buf) {
      return (ByteStreamWriter)(buf.length == 1 ? new ByteBufferByteStreamWriter(buf[0]) : new ByteBuffersByteStreamWriter(buf));
   }

   public interface ByteStreamTarget {
      OutputStream getOutputStream();
   }
}
