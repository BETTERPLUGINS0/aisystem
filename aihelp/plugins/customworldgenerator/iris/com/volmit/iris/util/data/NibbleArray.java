package com.volmit.iris.util.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.StringJoiner;

public class NibbleArray implements Writable {
   private static final int[] MASKS = new int[8];
   private final int size;
   private final Object lock = new Object();
   private byte[] data;
   private int depth;
   private byte mask;
   private transient int bitIndex;
   private transient int byteIndex;
   private transient int bitInByte;

   public NibbleArray(int capacity, DataInputStream in) {
      this.size = var1;
      this.read(var2);
   }

   public NibbleArray(int nibbleDepth, int capacity) {
      if (var1 <= 8 && var1 >= 1) {
         int var3 = var1 * var2;
         this.size = var2;
         this.depth = var1;
         this.data = new byte[(var3 + var3 % 8) / 8];
         this.mask = (byte)maskFor(var1);
      } else {
         throw new IllegalArgumentException();
      }
   }

   public NibbleArray(int nibbleDepth, int capacity, NibbleArray existing) {
      if (var1 <= 8 && var1 >= 1) {
         int var4 = var1 * var2;
         this.size = var2;
         this.depth = var1;
         this.data = new byte[(var4 + var4 % 8) / 8];
         this.mask = (byte)maskFor(var1);

         for(int var5 = 0; var5 < Math.min(this.size, var3.size()); ++var5) {
            this.set(var5, var3.get(var5));
         }

      } else {
         throw new IllegalArgumentException();
      }
   }

   public static int maskFor(int amountOfBits) {
      return powerOfTwo(var0) - 1;
   }

   public static int powerOfTwo(int power) {
      int var1 = 1;

      for(int var2 = 0; var2 < var0; ++var2) {
         var1 *= 2;
      }

      return var1;
   }

   public static String binaryString(byte b, ByteOrder byteOrder) {
      String var2 = String.format("%8s", Integer.toBinaryString(var0 & 255)).replace(' ', '0');
      return var1.equals(ByteOrder.BIG_ENDIAN) ? var2 : reverse(var2);
   }

   public static String reverse(String str) {
      return (new StringBuilder(var0)).reverse().toString();
   }

   public void write(DataOutputStream o) {
      var1.writeByte(this.depth + -128);
      var1.write(this.data);
   }

   public void read(DataInputStream i) {
      this.depth = var1.readByte() - -128;
      int var2 = this.depth * this.size;
      this.data = new byte[(var2 + var2 % 8) / 8];
      this.mask = (byte)maskFor(this.depth);
      var1.read(this.data);
   }

   public int size() {
      return this.size;
   }

   public byte get(int index) {
      synchronized(this.lock) {
         this.bitIndex = var1 * this.depth;
         this.byteIndex = this.bitIndex >> 3;
         this.bitInByte = this.bitIndex & 7;
         int var3 = this.data[this.byteIndex] >> this.bitInByte;
         if (this.bitInByte + this.depth > 8) {
            var3 |= this.data[this.byteIndex + 1] << this.bitInByte;
         }

         return (byte)(var3 & this.mask);
      }
   }

   public byte get(int x, int y, int z) {
      return this.get(this.index(var1, var2, var3));
   }

   public int index(int x, int y, int z) {
      return var2 << 8 | var3 << 4 | var1;
   }

   public void set(int x, int y, int z, int nibble) {
      this.set(this.index(var1, var2, var3), var4);
   }

   public void set(int index, int nibble) {
      this.set(var1, (byte)var2);
   }

   public void set(int index, byte nybble) {
      synchronized(this.lock) {
         this.bitIndex = var1 * this.depth;
         this.byteIndex = this.bitIndex >> 3;
         this.bitInByte = this.bitIndex & 7;
         this.data[this.byteIndex] = (byte)((~(this.data[this.byteIndex] & this.mask << this.bitInByte) & this.data[this.byteIndex] | (var2 & this.mask) << this.bitInByte) & 255);
         if (this.bitInByte + this.depth > 8) {
            this.data[this.byteIndex + 1] = (byte)((~(this.data[this.byteIndex + 1] & MASKS[this.bitInByte + this.depth - 8]) & this.data[this.byteIndex + 1] | (var2 & this.mask) >> 8 - this.bitInByte) & 255);
         }

      }
   }

   public String toBitsString() {
      return this.toBitsString(ByteOrder.BIG_ENDIAN);
   }

   public String toBitsString(ByteOrder byteOrder) {
      StringJoiner var2 = new StringJoiner(" ");
      byte[] var3 = this.data;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         byte var6 = var3[var5];
         var2.add(binaryString(var6, var1));
      }

      return var2.toString();
   }

   public void clear() {
      Arrays.fill(this.data, (byte)0);
   }

   public void setAll(byte nibble) {
      for(int var2 = 0; var2 < this.size; ++var2) {
         this.set(var2, var1);
      }

   }

   public void setAll(int nibble) {
      for(int var2 = 0; var2 < this.size; ++var2) {
         this.set(var2, (byte)var1);
      }

   }

   static {
      for(int var0 = 0; var0 < MASKS.length; ++var0) {
         MASKS[var0] = maskFor(var0);
      }

   }
}
