package github.nighter.smartspawner.libs.mariadb.client.socket.impl;

import github.nighter.smartspawner.libs.mariadb.client.util.MutableByte;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class CompressInputStream extends InputStream {
   private final InputStream in;
   private final MutableByte sequence;
   private final byte[] header = new byte[7];
   private int end;
   private int pos;
   private volatile byte[] buf;

   public CompressInputStream(InputStream in, MutableByte compressionSequence) {
      this.in = in;
      this.sequence = compressionSequence;
   }

   public int read(byte[] b, int off, int len) throws IOException {
      if (len == 0) {
         return 0;
      } else {
         int totalReads = 0;

         do {
            if (this.end - this.pos <= 0) {
               this.retrieveBuffer();
            }

            int copyLength = Math.min(len - totalReads, this.end - this.pos);
            System.arraycopy(this.buf, this.pos, b, off + totalReads, copyLength);
            this.pos += copyLength;
            totalReads += copyLength;
         } while(totalReads < len && super.available() > 0);

         return totalReads;
      }
   }

   private void retrieveBuffer() throws IOException {
      int remaining = 7;
      int readOffset = 0;

      int compressedPacketLength;
      do {
         compressedPacketLength = this.in.read(this.header, readOffset, remaining);
         if (compressedPacketLength < 0) {
            throw new EOFException("unexpected end of stream, read " + readOffset + " bytes from 7 (socket was closed by server)");
         }

         remaining -= compressedPacketLength;
         readOffset += compressedPacketLength;
      } while(remaining > 0);

      compressedPacketLength = (this.header[0] & 255) + ((this.header[1] & 255) << 8) + ((this.header[2] & 255) << 16);
      this.sequence.set(this.header[3]);
      int packetLength = (this.header[4] & 255) + ((this.header[5] & 255) << 8) + ((this.header[6] & 255) << 16);
      boolean compressed = packetLength != 0;
      remaining = compressedPacketLength;
      byte[] intermediaryBuf = new byte[compressedPacketLength];
      readOffset = 0;

      do {
         int count = this.in.read(intermediaryBuf, readOffset, remaining);
         if (count < 0) {
            throw new EOFException("unexpected end of stream, read " + ((compressed ? compressedPacketLength : packetLength) - remaining) + " bytes from " + (compressed ? compressedPacketLength : packetLength) + " (socket was closed by server)");
         }

         remaining -= count;
         readOffset += count;
      } while(remaining > 0);

      if (compressed) {
         this.buf = new byte[packetLength];
         Inflater inflater = new Inflater();
         inflater.setInput(intermediaryBuf);

         try {
            int actualUncompressBytes = inflater.inflate(this.buf);
            if (actualUncompressBytes != packetLength) {
               throw new IOException("Invalid exception length after decompression " + actualUncompressBytes + ",expected " + packetLength);
            }
         } catch (DataFormatException var9) {
            throw new IOException(var9);
         }

         inflater.end();
         this.end = packetLength;
      } else {
         this.buf = intermediaryBuf;
         this.end = compressedPacketLength;
      }

      this.pos = 0;
   }

   public long skip(long n) throws IOException {
      return (long)this.read(new byte[(int)n], 0, (int)n);
   }

   public int available() throws IOException {
      return this.in.available();
   }

   public void close() throws IOException {
      this.in.close();
   }

   public void mark(int readlimit) {
      this.in.mark(readlimit);
   }

   public void reset() throws IOException {
      this.in.reset();
   }

   public boolean markSupported() {
      return this.in.markSupported();
   }

   public int read() throws IOException {
      throw new IOException("NOT IMPLEMENTED !");
   }
}
