package fr.xephi.authme.libs.org.postgresql.largeobject;

import fr.xephi.authme.libs.org.postgresql.jdbc.ResourceLock;
import fr.xephi.authme.libs.org.postgresql.util.ByteStreamWriter;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import org.checkerframework.checker.index.qual.Positive;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BlobOutputStream extends OutputStream {
   static final int DEFAULT_MAX_BUFFER_SIZE = 524288;
   @Nullable
   private LargeObject lo;
   private final ResourceLock lock;
   @Nullable
   private byte[] buf;
   @Positive
   private final int maxBufferSize;
   private int bufferPosition;

   public BlobOutputStream(LargeObject lo) {
      this(lo, 524288);
   }

   public BlobOutputStream(LargeObject lo, int bufferSize) {
      this.lock = new ResourceLock();
      this.lo = lo;
      this.maxBufferSize = Integer.highestOneBit(Math.max(bufferSize, 1));
   }

   private byte[] growBuffer(int extraBytes) {
      byte[] buf = this.buf;
      if (buf == null || buf.length != this.maxBufferSize && buf.length - this.bufferPosition < extraBytes) {
         int newSize = Math.min(this.maxBufferSize, Integer.highestOneBit(this.bufferPosition + extraBytes) * 2);
         byte[] newBuffer = new byte[newSize];
         if (buf != null && this.bufferPosition != 0) {
            System.arraycopy(buf, 0, newBuffer, 0, this.bufferPosition);
         }

         this.buf = newBuffer;
         return newBuffer;
      } else {
         return buf;
      }
   }

   public void write(int b) throws IOException {
      long loId = 0L;

      try {
         ResourceLock ignore = this.lock.obtain();

         try {
            LargeObject lo = this.checkClosed();
            loId = lo.getLongOID();
            byte[] buf = this.growBuffer(16);
            if (this.bufferPosition >= buf.length) {
               lo.write(buf);
               this.bufferPosition = 0;
            }

            buf[this.bufferPosition++] = (byte)b;
         } catch (Throwable var8) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         if (ignore != null) {
            ignore.close();
         }

      } catch (SQLException var9) {
         throw new IOException(GT.tr("Can not write data to large object {0}, requested write length: {1}", loId, 1), var9);
      }
   }

   public void write(byte[] b, int off, int len) throws IOException {
      long loId = 0L;

      try {
         ResourceLock ignore = this.lock.obtain();

         try {
            LargeObject lo = this.checkClosed();
            loId = lo.getLongOID();
            byte[] buf = this.buf;
            int totalData = this.bufferPosition + len;
            int tailLength = this.maxBufferSize >= 8192 ? totalData % 8192 : (this.maxBufferSize >= 2048 ? totalData % 2048 : 0);
            if (totalData >= this.maxBufferSize) {
               int writeFromBuffer = Math.min(this.bufferPosition, totalData - tailLength);
               int writeFromB = Math.max(0, totalData - writeFromBuffer - tailLength);
               if (buf != null && this.bufferPosition > 0) {
                  if (writeFromB == 0) {
                     lo.write(buf, 0, writeFromBuffer);
                  } else {
                     lo.write(ByteStreamWriter.of(ByteBuffer.wrap(buf, 0, writeFromBuffer), ByteBuffer.wrap(b, off, writeFromB)));
                  }

                  if (writeFromBuffer >= this.bufferPosition) {
                     this.bufferPosition = 0;
                  } else {
                     System.arraycopy(buf, writeFromBuffer, buf, 0, this.bufferPosition - writeFromBuffer);
                     this.bufferPosition -= writeFromBuffer;
                  }
               } else {
                  lo.write(b, off, writeFromB);
               }

               len -= writeFromB;
               off += writeFromB;
            }

            if (len > 0) {
               buf = this.growBuffer(len);
               System.arraycopy(b, off, buf, this.bufferPosition, len);
               this.bufferPosition += len;
            }
         } catch (Throwable var14) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var13) {
                  var14.addSuppressed(var13);
               }
            }

            throw var14;
         }

         if (ignore != null) {
            ignore.close();
         }

      } catch (SQLException var15) {
         throw new IOException(GT.tr("Can not write data to large object {0}, requested write length: {1}", loId, len), var15);
      }
   }

   public void flush() throws IOException {
      long loId = 0L;

      try {
         ResourceLock ignore = this.lock.obtain();

         try {
            LargeObject lo = this.checkClosed();
            loId = lo.getLongOID();
            byte[] buf = this.buf;
            if (buf != null && this.bufferPosition > 0) {
               lo.write(buf, 0, this.bufferPosition);
            }

            this.bufferPosition = 0;
         } catch (Throwable var7) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (ignore != null) {
            ignore.close();
         }

      } catch (SQLException var8) {
         throw new IOException(GT.tr("Can not flush large object {0}", loId), var8);
      }
   }

   public void close() throws IOException {
      long loId = 0L;

      try {
         ResourceLock ignore = this.lock.obtain();

         try {
            LargeObject lo = this.lo;
            if (lo != null) {
               loId = lo.getLongOID();
               this.flush();
               lo.close();
               this.lo = null;
            }
         } catch (Throwable var7) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (ignore != null) {
            ignore.close();
         }

      } catch (SQLException var8) {
         throw new IOException(GT.tr("Can not close large object {0}", loId), var8);
      }
   }

   private LargeObject checkClosed() throws IOException {
      if (this.lo == null) {
         throw new IOException("BlobOutputStream is closed");
      } else {
         return this.lo;
      }
   }
}
