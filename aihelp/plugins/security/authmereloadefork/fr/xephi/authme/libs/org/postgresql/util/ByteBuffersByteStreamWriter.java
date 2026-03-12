package fr.xephi.authme.libs.org.postgresql.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

class ByteBuffersByteStreamWriter implements ByteStreamWriter {
   private final ByteBuffer[] buffers;
   private final int length;

   ByteBuffersByteStreamWriter(ByteBuffer... buffers) {
      this.buffers = buffers;
      int length = 0;
      ByteBuffer[] var3 = buffers;
      int var4 = buffers.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ByteBuffer buffer = var3[var5];
         length += buffer.remaining();
      }

      this.length = length;
   }

   public int getLength() {
      return this.length;
   }

   public void writeTo(ByteStreamWriter.ByteStreamTarget target) throws IOException {
      boolean allArraysAreAccessible = true;
      ByteBuffer[] var3 = this.buffers;
      int var4 = var3.length;

      int var5;
      for(var5 = 0; var5 < var4; ++var5) {
         ByteBuffer buffer = var3[var5];
         if (!buffer.hasArray()) {
            allArraysAreAccessible = false;
            break;
         }
      }

      OutputStream os = target.getOutputStream();
      int var15;
      if (allArraysAreAccessible) {
         ByteBuffer[] var13 = this.buffers;
         var5 = var13.length;

         for(var15 = 0; var15 < var5; ++var15) {
            ByteBuffer buffer = var13[var15];
            os.write(buffer.array(), buffer.arrayOffset() + buffer.position(), buffer.remaining());
         }

      } else {
         WritableByteChannel c = Channels.newChannel(os);

         try {
            ByteBuffer[] var14 = this.buffers;
            var15 = var14.length;

            for(int var7 = 0; var7 < var15; ++var7) {
               ByteBuffer buffer = var14[var7];
               if (buffer.hasArray()) {
                  os.write(buffer.array(), buffer.arrayOffset() + buffer.position(), buffer.remaining());
               } else {
                  c.write(buffer);
               }
            }
         } catch (Throwable var10) {
            if (c != null) {
               try {
                  c.close();
               } catch (Throwable var9) {
                  var10.addSuppressed(var9);
               }
            }

            throw var10;
         }

         if (c != null) {
            c.close();
         }

      }
   }
}
