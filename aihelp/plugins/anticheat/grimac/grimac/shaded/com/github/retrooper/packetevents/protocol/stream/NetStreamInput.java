package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stream;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/** @deprecated */
@Deprecated
public class NetStreamInput extends FilterInputStream {
   public NetStreamInput(InputStream in) {
      super(in);
   }

   public boolean readBoolean() {
      return this.readByte() == 1;
   }

   public byte readByte() {
      return (byte)this.readUnsignedByte();
   }

   public int readUnsignedByte() {
      int b = 0;

      try {
         b = this.read();
      } catch (IOException var3) {
         var3.printStackTrace();
      }

      if (b < 0) {
         throw new IllegalStateException();
      } else {
         return b;
      }
   }

   public short readShort() {
      return (short)this.readUnsignedShort();
   }

   public int readUnsignedShort() {
      int ch1 = this.readUnsignedByte();
      int ch2 = this.readUnsignedByte();
      return (ch1 << 8) + ch2;
   }

   public char readChar() {
      return (char)this.readUnsignedShort();
   }

   public int readInt() {
      int ch1 = this.readUnsignedByte();
      int ch2 = this.readUnsignedByte();
      int ch3 = this.readUnsignedByte();
      int ch4 = this.readUnsignedByte();
      return (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + ch4;
   }

   public int readVarInt() {
      int value = 0;
      int size = 0;

      do {
         byte b;
         if (((b = this.readByte()) & 128) != 128) {
            return value | (b & 127) << size * 7;
         }

         value |= (b & 127) << size++ * 7;
      } while(size <= 5);

      throw new IllegalStateException("VarInt too long (length must be <= 5)");
   }

   public long readLong() {
      byte[] read = this.readBytes(8);
      return ((long)read[0] << 56) + ((long)(read[1] & 255) << 48) + ((long)(read[2] & 255) << 40) + ((long)(read[3] & 255) << 32) + ((long)(read[4] & 255) << 24) + (long)((read[5] & 255) << 16) + (long)((read[6] & 255) << 8) + (long)((read[7] & 255) << 0);
   }

   public long readVarLong() {
      long value = 0L;
      int size = 0;

      do {
         byte b;
         if (((b = this.readByte()) & 128) != 128) {
            return value | (long)(b & 127) << size * 7;
         }

         value |= (long)(b & 127) << size++ * 7;
      } while(size <= 10);

      throw new IllegalStateException("VarLong too long (length must be <= 10)");
   }

   public float readFloat() {
      return Float.intBitsToFloat(this.readInt());
   }

   public double readDouble() {
      return Double.longBitsToDouble(this.readLong());
   }

   public byte[] readBytes(int length) {
      if (length < 0) {
         throw new IllegalArgumentException("Array cannot have length less than 0.");
      } else {
         byte[] b = new byte[length];

         int count;
         for(int n = 0; n < length; n += count) {
            count = 0;

            try {
               count = this.read(b, n, length - n);
            } catch (IOException var6) {
               var6.printStackTrace();
            }

            if (count < 0) {
               throw new IllegalStateException();
            }
         }

         return b;
      }
   }

   public int readBytes(byte[] b) {
      try {
         return this.read(b);
      } catch (IOException var3) {
         var3.printStackTrace();
         return -1;
      }
   }

   public int readBytes(byte[] b, int offset, int length) {
      try {
         return this.read(b, offset, length);
      } catch (IOException var5) {
         var5.printStackTrace();
         return -1;
      }
   }

   public short[] readShorts(int length) {
      if (length < 0) {
         throw new IllegalArgumentException("Array cannot have length less than 0.");
      } else {
         short[] s = new short[length];
         int read = this.readShorts(s);
         if (read < length) {
            throw new IllegalStateException();
         } else {
            return s;
         }
      }
   }

   public int readShorts(short[] s) {
      return this.readShorts(s, 0, s.length);
   }

   public int readShorts(short[] s, int offset, int length) {
      for(int index = offset; index < offset + length; ++index) {
         try {
            s[index] = this.readShort();
         } catch (Exception var6) {
            return index - offset;
         }
      }

      return length;
   }

   public int[] readInts(int length) {
      if (length < 0) {
         throw new IllegalArgumentException("Array cannot have length less than 0.");
      } else {
         int[] i = new int[length];
         int read = this.readInts(i);
         if (read < length) {
            throw new IllegalStateException();
         } else {
            return i;
         }
      }
   }

   public int readInts(int[] i) {
      return this.readInts(i, 0, i.length);
   }

   public int readInts(int[] i, int offset, int length) {
      for(int index = offset; index < offset + length; ++index) {
         try {
            i[index] = this.readInt();
         } catch (Exception var6) {
            return index - offset;
         }
      }

      return length;
   }

   public long[] readLongs(int length) {
      if (length < 0) {
         throw new IllegalArgumentException("Array cannot have length less than 0.");
      } else {
         long[] l = new long[length];
         int read = this.readLongs(l);
         if (read < length) {
            throw new IllegalStateException();
         } else {
            return l;
         }
      }
   }

   public int readLongs(long[] l) {
      return this.readLongs(l, 0, l.length);
   }

   public int readLongs(long[] l, int offset, int length) {
      for(int index = offset; index < offset + length; ++index) {
         try {
            l[index] = this.readLong();
         } catch (Exception var6) {
            return index - offset;
         }
      }

      return length;
   }

   public String readString() {
      int length = this.readVarInt();
      byte[] bytes = this.readBytes(length);
      return new String(bytes, StandardCharsets.UTF_8);
   }

   public UUID readUUID() {
      return new UUID(this.readLong(), this.readLong());
   }
}
