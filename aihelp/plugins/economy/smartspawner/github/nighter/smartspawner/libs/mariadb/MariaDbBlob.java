package github.nighter.smartspawner.libs.mariadb;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;

public class MariaDbBlob implements Blob, Serializable {
   private static final long serialVersionUID = -4736603161284649490L;
   protected byte[] data;
   protected transient int offset;
   protected transient int length;

   public MariaDbBlob() {
      this.data = new byte[0];
      this.offset = 0;
      this.length = 0;
   }

   public MariaDbBlob(byte[] bytes) {
      if (bytes == null) {
         throw new IllegalArgumentException("byte array is null");
      } else {
         this.data = bytes;
         this.offset = 0;
         this.length = bytes.length;
      }
   }

   public MariaDbBlob(byte[] bytes, int offset, int length) {
      if (bytes == null) {
         throw new IllegalArgumentException("byte array is null");
      } else {
         this.data = bytes;
         this.offset = offset;
         this.length = Math.min(bytes.length - offset, length);
      }
   }

   private MariaDbBlob(int offset, int length, byte[] bytes) {
      this.data = bytes;
      this.offset = offset;
      this.length = length;
   }

   public static MariaDbBlob safeMariaDbBlob(byte[] bytes, int offset, int length) {
      return new MariaDbBlob(offset, length, bytes);
   }

   public long length() {
      return (long)this.length;
   }

   public byte[] getBytes(long pos, int length) throws SQLException {
      if (pos < 1L) {
         throw new SQLException(String.format("Out of range (position should be > 0, but is %s)", pos));
      } else {
         int offset = this.offset + (int)(pos - 1L);
         byte[] result = new byte[length];
         System.arraycopy(this.data, offset, result, 0, Math.min(this.length - (int)(pos - 1L), length));
         return result;
      }
   }

   public InputStream getBinaryStream() throws SQLException {
      return this.getBinaryStream(1L, (long)this.length);
   }

   public InputStream getBinaryStream(long pos, long length) throws SQLException {
      if (pos < 1L) {
         throw new SQLException("Out of range (position should be > 0)");
      } else if (pos - 1L > (long)this.length) {
         throw new SQLException("Out of range (position > stream size)");
      } else if (pos + length - 1L > (long)this.length) {
         throw new SQLException("Out of range (position + length - 1 > streamSize)");
      } else {
         return new ByteArrayInputStream(this.data, this.offset + (int)pos - 1, (int)length);
      }
   }

   public long position(byte[] pattern, long start) throws SQLException {
      this.validateInputs(start);
      if (pattern.length == 0) {
         return 0L;
      } else if (start < 1L) {
         throw new SQLException(String.format("Out of range (position should be > 0, but is %s)", start));
      } else if (start > (long)this.length) {
         throw new SQLException("Out of range (start > stream size)");
      } else {
         return this.searchPattern(pattern, start);
      }
   }

   private long searchPattern(byte[] pattern, long start) {
      int searchStart = (int)((long)this.offset + start - 1L);
      int searchEnd = this.offset + this.length - pattern.length;

      for(int i = searchStart; i <= searchEnd; ++i) {
         if (this.isPatternMatch(pattern, i)) {
            return (long)(i + 1 - this.offset);
         }
      }

      return -1L;
   }

   private boolean isPatternMatch(byte[] pattern, int position) {
      for(int j = 0; j < pattern.length; ++j) {
         if (this.data[position + j] != pattern[j]) {
            return false;
         }
      }

      return true;
   }

   private void validateInputs(long start) throws SQLException {
      if (start < 1L) {
         throw new SQLException(String.format("Out of range (position should be > 0, but is %s)", start));
      } else if (start > (long)this.length) {
         throw new SQLException("Out of range (start > stream size)");
      }
   }

   public long position(Blob pattern, long start) throws SQLException {
      byte[] blobBytes = pattern.getBytes(1L, (int)pattern.length());
      return this.position(blobBytes, start);
   }

   public int setBytes(long pos, byte[] bytes) throws SQLException {
      if (pos < 1L) {
         throw new SQLException("pos should be > 0, first position is 1.");
      } else {
         int arrayPos = (int)pos - 1;
         if (this.length > arrayPos + bytes.length) {
            System.arraycopy(bytes, 0, this.data, this.offset + arrayPos, bytes.length);
         } else {
            byte[] newContent = new byte[arrayPos + bytes.length];
            if (Math.min(arrayPos, this.length) > 0) {
               System.arraycopy(this.data, this.offset, newContent, 0, Math.min(arrayPos, this.length));
            }

            System.arraycopy(bytes, 0, newContent, arrayPos, bytes.length);
            this.data = newContent;
            this.length = arrayPos + bytes.length;
            this.offset = 0;
         }

         return bytes.length;
      }
   }

   public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
      if (pos < 1L) {
         throw new SQLException("pos should be > 0, first position is 1.");
      } else {
         int arrayPos = (int)pos - 1;
         int byteToWrite = Math.min(bytes.length - offset, len);
         if (this.length > arrayPos + byteToWrite) {
            System.arraycopy(bytes, offset, this.data, this.offset + arrayPos, byteToWrite);
         } else {
            byte[] newContent = new byte[arrayPos + byteToWrite];
            if (Math.min(arrayPos, this.length) > 0) {
               System.arraycopy(this.data, this.offset, newContent, 0, Math.min(arrayPos, this.length));
            }

            System.arraycopy(bytes, offset, newContent, arrayPos, byteToWrite);
            this.data = newContent;
            this.length = arrayPos + byteToWrite;
            this.offset = 0;
         }

         return byteToWrite;
      }
   }

   public OutputStream setBinaryStream(long pos) throws SQLException {
      if (pos < 1L) {
         throw new SQLException("Invalid position in blob");
      } else {
         if (this.offset > 0) {
            byte[] tmp = new byte[this.length];
            System.arraycopy(this.data, this.offset, tmp, 0, this.length);
            this.data = tmp;
            this.offset = 0;
         }

         return new MariaDbBlob.BlobOutputStream(this, (int)(pos - 1L) + this.offset);
      }
   }

   public void truncate(long len) {
      if (len >= 0L && len < (long)this.length) {
         this.length = (int)len;
      }

   }

   public void free() {
      this.data = new byte[0];
      this.offset = 0;
      this.length = 0;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         MariaDbBlob that = (MariaDbBlob)o;
         if (this.length != that.length) {
            return false;
         } else {
            for(int i = 0; i < this.length; ++i) {
               if (this.data[this.offset + i] != that.data[that.offset + i]) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = Arrays.hashCode(this.data);
      result = 31 * result + this.offset;
      result = 31 * result + this.length;
      return result;
   }

   public void close() throws SQLException {
      this.data = null;
   }

   static class BlobOutputStream extends OutputStream {
      private final MariaDbBlob blob;
      private int pos;

      public BlobOutputStream(MariaDbBlob blob, int pos) {
         this.blob = blob;
         this.pos = pos;
      }

      public void write(int bit) {
         if (this.pos >= this.blob.length) {
            byte[] tmp = new byte[2 * this.blob.length + 1];
            System.arraycopy(this.blob.data, this.blob.offset, tmp, 0, this.blob.length);
            this.blob.data = tmp;
            this.pos -= this.blob.offset;
            this.blob.offset = 0;
            ++this.blob.length;
         }

         this.blob.data[this.pos++] = (byte)bit;
      }

      public void write(byte[] buf, int off, int len) throws IOException {
         if (off < 0) {
            throw new IOException("Invalid offset " + off);
         } else if (len < 0) {
            throw new IOException("Invalid len " + len);
         } else {
            int realLen = Math.min(buf.length - off, len);
            if (this.pos + realLen >= this.blob.length) {
               int newLen = 2 * this.blob.length + realLen;
               byte[] tmp = new byte[newLen];
               System.arraycopy(this.blob.data, this.blob.offset, tmp, 0, this.blob.length);
               this.blob.data = tmp;
               this.pos -= this.blob.offset;
               this.blob.offset = 0;
               this.blob.length = this.pos + realLen;
            }

            System.arraycopy(buf, off, this.blob.data, this.pos, realLen);
            this.pos += realLen;
         }
      }

      public void write(byte[] buf) throws IOException {
         this.write(buf, 0, buf.length);
      }
   }
}
