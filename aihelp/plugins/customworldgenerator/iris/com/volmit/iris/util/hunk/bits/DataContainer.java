package com.volmit.iris.util.hunk.bits;

import com.volmit.iris.util.data.Varint;
import it.unimi.dsi.fastutil.ints.Int2IntRBTreeMap;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataContainer<T> {
   private static final boolean TRIM = Boolean.getBoolean("iris.trim-palette");
   protected static final int INITIAL_BITS = 3;
   protected static final int LINEAR_BITS_LIMIT = 4;
   protected static final int LINEAR_INITIAL_LENGTH = (int)Math.pow(2.0D, 4.0D) + 2;
   protected static final int[] BIT = computeBitLimits();
   private final Lock read;
   private final Lock write;
   private volatile Palette<T> palette;
   private volatile DataBits data;
   private final int length;
   private final Writable<T> writer;

   public DataContainer(Writable<T> writer, int length) {
      ReentrantReadWriteLock var3 = new ReentrantReadWriteLock();
      this.read = var3.readLock();
      this.write = var3.writeLock();
      this.writer = var1;
      this.length = var2;
      this.data = new DataBits(3, var2);
      this.palette = this.newPalette(3);
   }

   public DataContainer(DataInputStream din, Writable<T> writer) {
      ReentrantReadWriteLock var3 = new ReentrantReadWriteLock();
      this.read = var3.readLock();
      this.write = var3.writeLock();
      this.writer = var2;
      this.length = Varint.readUnsignedVarInt((DataInput)var1);
      this.palette = this.newPalette(var1);
      this.data = new DataBits(this.palette.bits(), this.length, var1);
      this.trim();
   }

   private static int[] computeBitLimits() {
      int[] var0 = new int[16];

      for(int var1 = 0; var1 < var0.length; ++var1) {
         var0[var1] = (int)Math.pow(2.0D, (double)var1);
      }

      return var0;
   }

   protected static int bits(int size) {
      if (BIT[3] >= var0) {
         return 3;
      } else {
         for(int var1 = 0; var1 < BIT.length; ++var1) {
            if (BIT[var1] >= var0) {
               return var1;
            }
         }

         return BIT.length - 1;
      }
   }

   public String toString() {
      int var10000 = this.length;
      return "DataContainer <" + var10000 + " x " + this.data.getBits() + " bits> -> Palette<" + this.palette.getClass().getSimpleName().replaceAll("\\QPalette\\E", "") + ">: " + this.palette.size() + " " + this.data.toString() + " PalBit: " + this.palette.bits();
   }

   public byte[] write() {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      this.write(var1);
      return var1.toByteArray();
   }

   public void write(OutputStream out) {
      this.writeDos(new DataOutputStream(var1));
   }

   public void writeDos(DataOutputStream dos) {
      this.write.lock();

      try {
         this.trim();
         Varint.writeUnsignedVarInt(this.length, var1);
         Varint.writeUnsignedVarInt(this.palette.size(), var1);
         this.palette.iterateIO((var2, var3) -> {
            this.writer.writeNodeData(var1, var2);
         });
         this.data.write(var1);
         var1.flush();
      } finally {
         this.write.unlock();
      }

   }

   private Palette<T> newPalette(DataInputStream din) {
      int var2 = Varint.readUnsignedVarInt((DataInput)var1);
      Palette var3 = this.newPalette(bits(var2 + 1));
      var3.from(var2, this.writer, var1);
      return var3;
   }

   private Palette<T> newPalette(int bits) {
      return (Palette)(var1 <= 4 ? new LinearPalette(LINEAR_INITIAL_LENGTH) : new HashPalette());
   }

   public void set(int position, T t) {
      this.read.lock();

      int var3;
      try {
         var3 = this.palette.id(var2);
         if (var3 == -1) {
            var3 = this.palette.add(var2);
            if (this.palette.bits() == this.data.getBits()) {
               this.data.set(var1, var3);
               return;
            }
         }
      } finally {
         this.read.unlock();
      }

      this.write.lock();

      try {
         this.updateBits();
         this.data.set(var1, var3);
      } finally {
         this.write.unlock();
      }

   }

   private void updateBits() {
      int var1 = this.palette.bits();
      if (var1 != this.data.getBits()) {
         if (this.data.getBits() <= 4 != var1 <= 4) {
            this.palette = this.newPalette(var1).from(this.palette);
         }

         this.data = this.data.setBits(var1);
      }
   }

   public T get(int position) {
      this.read.lock();

      Object var3;
      try {
         int var2 = this.data.get(var1);
         if (var2 > 0) {
            var3 = this.palette.get(var2);
            return var3;
         }

         var3 = null;
      } finally {
         this.read.unlock();
      }

      return var3;
   }

   public int size() {
      return this.data.getSize();
   }

   private void trim() {
      Int2IntRBTreeMap var1 = new Int2IntRBTreeMap();

      int var2;
      for(var2 = 0; var2 < this.length; ++var2) {
         int var3 = this.data.get(var2);
         if (var3 > 0) {
            var1.put(var3, var3);
         }
      }

      if (var1.size() != this.palette.size()) {
         var2 = bits(var1.size() + 1);
         Palette var6 = this.newPalette(var2);
         var1.replaceAll((var2x, var3x) -> {
            return var6.add(this.palette.get(var2x));
         });
         DataBits var4 = new DataBits(var2, this.length);

         for(int var5 = 0; var5 < this.length; ++var5) {
            var4.set(var5, var1.get(this.data.get(var5)));
         }

         this.data = var4;
         this.palette = var6;
      }
   }
}
