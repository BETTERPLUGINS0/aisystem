package fr.xephi.authme.libs.org.postgresql.core;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;

public class VisibleBufferedInputStream extends InputStream {
   private static final int MINIMUM_READ = 1024;
   private static final int STRING_SCAN_SPAN = 1024;
   private final InputStream wrapped;
   private byte[] buffer;
   private int index;
   private int endIndex;
   private boolean timeoutRequested;

   public VisibleBufferedInputStream(InputStream in, int bufferSize) {
      this.wrapped = in;
      this.buffer = new byte[bufferSize < 1024 ? 1024 : bufferSize];
   }

   public int read() throws IOException {
      return this.ensureBytes(1) ? this.buffer[this.index++] & 255 : -1;
   }

   public int peek() throws IOException {
      return this.ensureBytes(1) ? this.buffer[this.index] & 255 : -1;
   }

   public byte readRaw() {
      return this.buffer[this.index++];
   }

   public boolean ensureBytes(int n) throws IOException {
      return this.ensureBytes(n, true);
   }

   public boolean ensureBytes(int n, boolean block) throws IOException {
      for(int required = n - this.endIndex + this.index; required > 0; required = n - this.endIndex + this.index) {
         if (!this.readMore(required, block)) {
            return false;
         }
      }

      return true;
   }

   private boolean readMore(int wanted, boolean block) throws IOException {
      if (this.endIndex == this.index) {
         this.index = 0;
         this.endIndex = 0;
      }

      int canFit = this.buffer.length - this.endIndex;
      if (canFit < wanted) {
         if (this.index + canFit > wanted + 1024) {
            this.compact();
         } else {
            this.doubleBuffer();
         }

         canFit = this.buffer.length - this.endIndex;
      }

      int read = 0;

      try {
         read = this.wrapped.read(this.buffer, this.endIndex, canFit);
         if (!block && read == 0) {
            return false;
         }
      } catch (SocketTimeoutException var6) {
         if (!block) {
            return false;
         }

         if (this.timeoutRequested) {
            throw var6;
         }
      }

      if (read < 0) {
         return false;
      } else {
         this.endIndex += read;
         return true;
      }
   }

   private void doubleBuffer() {
      byte[] buf = new byte[this.buffer.length * 2];
      this.moveBufferTo(buf);
      this.buffer = buf;
   }

   private void compact() {
      this.moveBufferTo(this.buffer);
   }

   private void moveBufferTo(byte[] dest) {
      int size = this.endIndex - this.index;
      System.arraycopy(this.buffer, this.index, dest, 0, size);
      this.index = 0;
      this.endIndex = size;
   }

   public int read(byte[] to, int off, int len) throws IOException {
      if ((off | len | off + len | to.length - (off + len)) < 0) {
         throw new IndexOutOfBoundsException();
      } else if (len == 0) {
         return 0;
      } else {
         int avail = this.endIndex - this.index;
         if (len - avail < 1024) {
            this.ensureBytes(len);
            avail = this.endIndex - this.index;
         }

         if (avail > 0) {
            if (len <= avail) {
               System.arraycopy(this.buffer, this.index, to, off, len);
               this.index += len;
               return len;
            }

            System.arraycopy(this.buffer, this.index, to, off, avail);
            len -= avail;
            off += avail;
         }

         int read = avail;
         this.index = 0;
         this.endIndex = 0;

         do {
            int r;
            try {
               r = this.wrapped.read(to, off, len);
            } catch (SocketTimeoutException var8) {
               if (read == 0 && this.timeoutRequested) {
                  throw var8;
               }

               return read;
            }

            if (r <= 0) {
               return read == 0 ? r : read;
            }

            read += r;
            off += r;
            len -= r;
         } while(len > 0);

         return read;
      }
   }

   public long skip(long n) throws IOException {
      int avail = this.endIndex - this.index;
      if ((long)avail >= n) {
         this.index = (int)((long)this.index + n);
         return n;
      } else {
         n -= (long)avail;
         this.index = 0;
         this.endIndex = 0;
         return (long)avail + this.wrapped.skip(n);
      }
   }

   public int available() throws IOException {
      int avail = this.endIndex - this.index;
      return avail > 0 ? avail : this.wrapped.available();
   }

   public void close() throws IOException {
      this.wrapped.close();
   }

   public byte[] getBuffer() {
      return this.buffer;
   }

   public int getIndex() {
      return this.index;
   }

   public int scanCStringLength() throws IOException {
      int pos = this.index;

      do {
         while(pos >= this.endIndex) {
            if (!this.readMore(1024, true)) {
               throw new EOFException();
            }

            pos = this.index;
         }
      } while(this.buffer[pos++] != 0);

      return pos - this.index;
   }

   public void setTimeoutRequested(boolean timeoutRequested) {
      this.timeoutRequested = timeoutRequested;
   }

   public InputStream getWrapped() {
      return this.wrapped;
   }
}
