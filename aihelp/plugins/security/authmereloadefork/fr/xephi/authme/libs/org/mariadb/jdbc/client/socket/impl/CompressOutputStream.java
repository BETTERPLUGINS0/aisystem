package fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.impl;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableByte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;

public class CompressOutputStream extends OutputStream {
   private static final int MIN_COMPRESSION_SIZE = 1536;
   private final OutputStream out;
   private final MutableByte sequence;
   private final byte[] header = new byte[7];
   private byte[] longPacketBuffer = null;

   public CompressOutputStream(OutputStream out, MutableByte compressionSequence) {
      this.out = out;
      this.sequence = compressionSequence;
   }

   public void write(byte[] b, int off, int len) throws IOException {
      if (len + (this.longPacketBuffer != null ? this.longPacketBuffer.length : 0) < 1536) {
         if (this.longPacketBuffer != null) {
            this.header[0] = (byte)(len + this.longPacketBuffer.length);
            this.header[1] = (byte)(len + this.longPacketBuffer.length >>> 8);
            this.header[2] = 0;
            this.header[3] = this.sequence.incrementAndGet();
            this.header[4] = 0;
            this.header[5] = 0;
            this.header[6] = 0;
            this.out.write(this.header, 0, 7);
            this.out.write(this.longPacketBuffer, 0, this.longPacketBuffer.length);
            this.out.write(b, off, len);
            this.longPacketBuffer = null;
            return;
         }

         this.header[0] = (byte)len;
         this.header[1] = (byte)(len >>> 8);
         this.header[2] = 0;
         this.header[3] = this.sequence.incrementAndGet();
         this.header[4] = 0;
         this.header[5] = 0;
         this.header[6] = 0;
         this.out.write(this.header, 0, 7);
         this.out.write(b, off, len);
      } else {
         int sent = 0;
         ByteArrayOutputStream baos = new ByteArrayOutputStream();

         try {
            DeflaterOutputStream deflater = new DeflaterOutputStream(baos);

            int compressLen;
            try {
               if (this.longPacketBuffer != null) {
                  deflater.write(this.longPacketBuffer, 0, this.longPacketBuffer.length);
                  sent = this.longPacketBuffer.length;
                  this.longPacketBuffer = null;
               }

               if (len + sent > 16777215) {
                  compressLen = len + sent - 16777215;
                  this.longPacketBuffer = new byte[compressLen];
                  System.arraycopy(b, off + 16777215 - sent, this.longPacketBuffer, 0, compressLen);
               }

               compressLen = Math.min(16777215 - sent, len);
               deflater.write(b, off, compressLen);
               sent += compressLen;
               deflater.finish();
            } catch (Throwable var11) {
               try {
                  deflater.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }

               throw var11;
            }

            deflater.close();
            byte[] compressedBytes = baos.toByteArray();
            compressLen = compressedBytes.length;
            this.header[0] = (byte)compressLen;
            this.header[1] = (byte)(compressLen >>> 8);
            this.header[2] = (byte)(compressLen >>> 16);
            this.header[3] = this.sequence.incrementAndGet();
            this.header[4] = (byte)sent;
            this.header[5] = (byte)(sent >>> 8);
            this.header[6] = (byte)(sent >>> 16);
            this.out.write(this.header, 0, 7);
            this.out.write(compressedBytes, 0, compressLen);
            this.out.flush();
         } catch (Throwable var12) {
            try {
               baos.close();
            } catch (Throwable var9) {
               var12.addSuppressed(var9);
            }

            throw var12;
         }

         baos.close();
      }

   }

   public void flush() throws IOException {
      if (this.longPacketBuffer != null) {
         byte[] b = this.longPacketBuffer;
         this.longPacketBuffer = null;
         this.write(b, 0, b.length);
      }

      this.out.flush();
      this.sequence.set((byte)-1);
   }

   public void close() throws IOException {
      this.out.close();
   }

   public void write(int b) throws IOException {
      throw new IOException("NOT EXPECTED !");
   }
}
