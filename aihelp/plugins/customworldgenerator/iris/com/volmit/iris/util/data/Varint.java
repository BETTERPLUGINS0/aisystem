package com.volmit.iris.util.data;

import java.io.DataInput;
import java.io.DataOutput;

public final class Varint {
   private Varint() {
   }

   public static void writeSignedVarLong(long value, DataOutput out) {
      writeUnsignedVarLong(var0 << 1 ^ var0 >> 63, var2);
   }

   public static void writeUnsignedVarLong(long value, DataOutput out) {
      while((var0 & -128L) != 0L) {
         var2.writeByte((int)var0 & 127 | 128);
         var0 >>>= 7;
      }

      var2.writeByte((int)var0 & 127);
   }

   public static void writeSignedVarInt(int value, DataOutput out) {
      writeUnsignedVarInt(var0 << 1 ^ var0 >> 31, var1);
   }

   public static void writeUnsignedVarInt(int value, DataOutput out) {
      while((long)(var0 & -128) != 0L) {
         var1.writeByte(var0 & 127 | 128);
         var0 >>>= 7;
      }

      var1.writeByte(var0 & 127);
   }

   public static byte[] writeSignedVarInt(int value) {
      return writeUnsignedVarInt(var0 << 1 ^ var0 >> 31);
   }

   public static byte[] writeUnsignedVarInt(int value) {
      byte[] var1 = new byte[10];

      int var2;
      for(var2 = 0; (long)(var0 & -128) != 0L; var0 >>>= 7) {
         var1[var2++] = (byte)(var0 & 127 | 128);
      }

      var1[var2] = (byte)(var0 & 127);

      byte[] var3;
      for(var3 = new byte[var2 + 1]; var2 >= 0; --var2) {
         var3[var2] = var1[var2];
      }

      return var3;
   }

   public static long readSignedVarLong(DataInput in) {
      long var1 = readUnsignedVarLong(var0);
      long var3 = (var1 << 63 >> 63 ^ var1) >> 1;
      return var3 ^ var1 & Long.MIN_VALUE;
   }

   public static long readUnsignedVarLong(DataInput in) {
      long var1 = 0L;
      int var3 = 0;

      do {
         long var4;
         if (((var4 = (long)var0.readByte()) & 128L) == 0L) {
            return var1 | var4 << var3;
         }

         var1 |= (var4 & 127L) << var3;
         var3 += 7;
      } while(var3 <= 63);

      throw new IllegalArgumentException("Variable length quantity is too long");
   }

   public static int readSignedVarInt(DataInput in) {
      int var1 = readUnsignedVarInt(var0);
      int var2 = (var1 << 31 >> 31 ^ var1) >> 1;
      return var2 ^ var1 & Integer.MIN_VALUE;
   }

   public static int readUnsignedVarInt(DataInput in) {
      int var1 = 0;
      int var2 = 0;

      do {
         byte var3;
         if (((var3 = var0.readByte()) & 128) == 0) {
            return var1 | var3 << var2;
         }

         var1 |= (var3 & 127) << var2;
         var2 += 7;
      } while(var2 <= 35);

      throw new IllegalArgumentException("Variable length quantity is too long");
   }

   public static int readSignedVarInt(byte[] bytes) {
      int var1 = readUnsignedVarInt(var0);
      int var2 = (var1 << 31 >> 31 ^ var1) >> 1;
      return var2 ^ var1 & Integer.MIN_VALUE;
   }

   public static int readUnsignedVarInt(byte[] bytes) {
      int var1 = 0;
      int var2 = 0;
      byte var3 = -128;
      byte[] var4 = var0;
      int var5 = var0.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         byte var7 = var4[var6];
         var3 = var7;
         if ((var7 & 128) == 0) {
            break;
         }

         var1 |= (var7 & 127) << var2;
         var2 += 7;
         if (var2 > 35) {
            throw new IllegalArgumentException("Variable length quantity is too long");
         }
      }

      return var1 | var3 << var2;
   }
}
