package fr.xephi.authme.libs.org.postgresql.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

public class ByteBufferByteStreamWriter implements ByteStreamWriter {
   private final ByteBuffer buf;
   private final int length;

   public ByteBufferByteStreamWriter(ByteBuffer buf) {
      this.buf = buf;
      this.length = buf.remaining();
   }

   public int getLength() {
      return this.length;
   }

   public void writeTo(ByteStreamWriter.ByteStreamTarget target) throws IOException {
      if (this.buf.hasArray()) {
         target.getOutputStream().write(this.buf.array(), this.buf.arrayOffset() + this.buf.position(), this.buf.remaining());
      } else {
         WritableByteChannel c = Channels.newChannel(target.getOutputStream());

         try {
            c.write(this.buf);
         } catch (Throwable var6) {
            if (c != null) {
               try {
                  c.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }
            }

            throw var6;
         }

         if (c != null) {
            c.close();
         }

      }
   }
}
