package fr.xephi.authme.libs.com.maxmind.db;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

final class BufferHolder {
   private final ByteBuffer buffer;

   BufferHolder(File database, Reader.FileMode mode) throws IOException {
      RandomAccessFile file = new RandomAccessFile(database, "r");
      Throwable var4 = null;

      try {
         FileChannel channel = file.getChannel();
         Throwable var6 = null;

         try {
            if (mode == Reader.FileMode.MEMORY) {
               this.buffer = ByteBuffer.wrap(new byte[(int)channel.size()]);
               if (channel.read(this.buffer) != this.buffer.capacity()) {
                  throw new IOException("Unable to read " + database.getName() + " into memory. Unexpected end of stream.");
               }
            } else {
               this.buffer = channel.map(MapMode.READ_ONLY, 0L, channel.size());
            }
         } catch (Throwable var29) {
            var6 = var29;
            throw var29;
         } finally {
            if (channel != null) {
               if (var6 != null) {
                  try {
                     channel.close();
                  } catch (Throwable var28) {
                     var6.addSuppressed(var28);
                  }
               } else {
                  channel.close();
               }
            }

         }
      } catch (Throwable var31) {
         var4 = var31;
         throw var31;
      } finally {
         if (file != null) {
            if (var4 != null) {
               try {
                  file.close();
               } catch (Throwable var27) {
                  var4.addSuppressed(var27);
               }
            } else {
               file.close();
            }
         }

      }

   }

   BufferHolder(InputStream stream) throws IOException {
      if (null == stream) {
         throw new NullPointerException("Unable to use a NULL InputStream");
      } else {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         byte[] bytes = new byte[16384];

         int br;
         while(-1 != (br = stream.read(bytes))) {
            baos.write(bytes, 0, br);
         }

         this.buffer = ByteBuffer.wrap(baos.toByteArray());
      }
   }

   synchronized ByteBuffer get() {
      return this.buffer.duplicate();
   }
}
