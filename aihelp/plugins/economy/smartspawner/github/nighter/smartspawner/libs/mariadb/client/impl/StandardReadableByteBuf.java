package github.nighter.smartspawner.libs.mariadb.client.impl;

import github.nighter.smartspawner.libs.mariadb.MariaDbBlob;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import java.nio.charset.StandardCharsets;

public final class StandardReadableByteBuf implements ReadableByteBuf {
   public byte[] buf;
   public int pos = 0;
   private int limit;

   public StandardReadableByteBuf(byte[] buf, int limit) {
      this.buf = buf;
      this.limit = limit;
   }

   public StandardReadableByteBuf(byte[] buf) {
      this.buf = buf;
      this.limit = buf.length;
   }

   public int readableBytes() {
      return this.limit - this.pos;
   }

   public int pos() {
      return this.pos;
   }

   public byte[] buf() {
      return this.buf;
   }

   public void buf(byte[] buf, int limit, int pos) {
      this.buf = buf;
      this.limit = limit;
      this.pos = pos;
   }

   public void pos(int pos) {
      this.pos = pos;
   }

   public void skip() {
      ++this.pos;
   }

   public void skip(int length) {
      this.pos += length;
   }

   public void skipLengthEncoded() {
      byte len = this.buf[this.pos++];
      switch(len) {
      case -5:
         return;
      case -4:
         this.skip(this.readUnsignedShort());
         return;
      case -3:
         this.skip(this.readUnsignedMedium());
         return;
      case -2:
         this.skip((int)(4L + this.readUnsignedInt()));
         return;
      default:
         this.pos += len & 255;
      }
   }

   public MariaDbBlob readBlob(int length) {
      this.pos += length;
      return MariaDbBlob.safeMariaDbBlob(this.buf, this.pos - length, length);
   }

   public long atoll(int length) {
      boolean negate = false;
      int idx = 0;
      long result = 0L;
      if (length > 0 && this.buf[this.pos] == 45) {
         negate = true;
         ++this.pos;
         ++idx;
      }

      while(idx++ < length) {
         result = result * 10L + (long)this.buf[this.pos++] - 48L;
      }

      return negate ? -1L * result : result;
   }

   public long atoull(int length) {
      long result = 0L;

      for(int idx = 0; idx < length; ++idx) {
         result = result * 10L + (long)this.buf[this.pos++] - 48L;
      }

      return result;
   }

   public byte getByte() {
      return this.buf[this.pos];
   }

   public byte getByte(int index) {
      return this.buf[index];
   }

   public short getUnsignedByte() {
      return (short)(this.buf[this.pos] & 255);
   }

   public long readLongLengthEncodedNotNull() {
      int type = this.buf[this.pos++] & 255;
      if (type < 251) {
         return (long)type;
      } else {
         switch(type) {
         case 252:
            return (long)this.readUnsignedShort();
         case 253:
            return (long)this.readUnsignedMedium();
         default:
            return this.readLong();
         }
      }
   }

   public int readIntLengthEncodedNotNull() {
      int type = this.buf[this.pos++] & 255;
      if (type < 251) {
         return type;
      } else {
         switch(type) {
         case 252:
            return this.readUnsignedShort();
         case 253:
            return this.readUnsignedMedium();
         case 254:
            return (int)this.readLong();
         default:
            return type;
         }
      }
   }

   public int skipIdentifier() {
      int l = this.buf[this.pos++] & 255;
      if (l < 251) {
         this.pos += l;
         return this.pos;
      } else {
         int val = this.readUnsignedShort();
         this.pos += val;
         return this.pos;
      }
   }

   public Integer readLength() {
      int type = this.readUnsignedByte();
      switch(type) {
      case 251:
         return null;
      case 252:
         return this.readUnsignedShort();
      case 253:
         return this.readUnsignedMedium();
      case 254:
         return (int)this.readLong();
      default:
         return Integer.valueOf(type);
      }
   }

   public byte readByte() {
      return this.buf[this.pos++];
   }

   public short readUnsignedByte() {
      return (short)(this.buf[this.pos++] & 255);
   }

   public short readShort() {
      return (short)((this.buf[this.pos++] & 255) + (this.buf[this.pos++] << 8));
   }

   public int readUnsignedShort() {
      return (this.buf[this.pos++] & 255) + (this.buf[this.pos++] << 8) & '\uffff';
   }

   public int readMedium() {
      int value = this.readUnsignedMedium();
      if ((value & 8388608) != 0) {
         value |= -16777216;
      }

      return value;
   }

   public int readUnsignedMedium() {
      return (this.buf[this.pos++] & 255) + ((this.buf[this.pos++] & 255) << 8) + ((this.buf[this.pos++] & 255) << 16);
   }

   public int readInt() {
      return (this.buf[this.pos++] & 255) + ((this.buf[this.pos++] & 255) << 8) + ((this.buf[this.pos++] & 255) << 16) + ((this.buf[this.pos++] & 255) << 24);
   }

   public int readIntBE() {
      return ((this.buf[this.pos++] & 255) << 24) + ((this.buf[this.pos++] & 255) << 16) + ((this.buf[this.pos++] & 255) << 8) + (this.buf[this.pos++] & 255);
   }

   public long readUnsignedInt() {
      return (long)((this.buf[this.pos++] & 255) + ((this.buf[this.pos++] & 255) << 8) + ((this.buf[this.pos++] & 255) << 16)) + ((long)(this.buf[this.pos++] & 255) << 24) & 4294967295L;
   }

   public long readLong() {
      return ((long)this.buf[this.pos++] & 255L) + (((long)this.buf[this.pos++] & 255L) << 8) + (((long)this.buf[this.pos++] & 255L) << 16) + (((long)this.buf[this.pos++] & 255L) << 24) + (((long)this.buf[this.pos++] & 255L) << 32) + (((long)this.buf[this.pos++] & 255L) << 40) + (((long)this.buf[this.pos++] & 255L) << 48) + (((long)this.buf[this.pos++] & 255L) << 56);
   }

   public long readLongBE() {
      return (((long)this.buf[this.pos++] & 255L) << 56) + (((long)this.buf[this.pos++] & 255L) << 48) + (((long)this.buf[this.pos++] & 255L) << 40) + (((long)this.buf[this.pos++] & 255L) << 32) + (((long)this.buf[this.pos++] & 255L) << 24) + (((long)this.buf[this.pos++] & 255L) << 16) + (((long)this.buf[this.pos++] & 255L) << 8) + ((long)this.buf[this.pos++] & 255L);
   }

   public void readBytes(byte[] dst) {
      System.arraycopy(this.buf, this.pos, dst, 0, dst.length);
      this.pos += dst.length;
   }

   public byte[] readBytesNullEnd() {
      int initialPosition = this.pos;

      int cnt;
      for(cnt = 0; this.readableBytes() > 0 && this.buf[this.pos++] != 0; ++cnt) {
      }

      byte[] dst = new byte[cnt];
      System.arraycopy(this.buf, initialPosition, dst, 0, dst.length);
      return dst;
   }

   public StandardReadableByteBuf readLengthBuffer() {
      int len = this.readIntLengthEncodedNotNull();
      StandardReadableByteBuf b = new StandardReadableByteBuf(this.buf, this.pos + len);
      b.pos = this.pos;
      this.pos += len;
      return b;
   }

   public String readString(int length) {
      this.pos += length;
      return new String(this.buf, this.pos - length, length, StandardCharsets.UTF_8);
   }

   public String readAscii(int length) {
      this.pos += length;
      return new String(this.buf, this.pos - length, length, StandardCharsets.US_ASCII);
   }

   public String readStringNullEnd() {
      int initialPosition = this.pos;

      int cnt;
      for(cnt = 0; this.readableBytes() > 0 && this.buf[this.pos++] != 0; ++cnt) {
      }

      return new String(this.buf, initialPosition, cnt, StandardCharsets.UTF_8);
   }

   public String readStringEof() {
      int initialPosition = this.pos;
      this.pos = this.limit;
      return new String(this.buf, initialPosition, this.pos - initialPosition, StandardCharsets.UTF_8);
   }

   public float readFloat() {
      return Float.intBitsToFloat(this.readInt());
   }

   public double readDouble() {
      return Double.longBitsToDouble(this.readLong());
   }

   public double readDoubleBE() {
      return Double.longBitsToDouble(this.readLongBE());
   }
}
