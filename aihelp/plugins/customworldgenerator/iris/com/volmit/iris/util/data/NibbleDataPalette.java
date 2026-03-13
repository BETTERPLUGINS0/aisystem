package com.volmit.iris.util.data;

import com.volmit.iris.util.collection.KList;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Iterator;

public abstract class NibbleDataPalette<T> implements Writable {
   private static final int DEFAULT_BITS_PER_BLOCK = 4;
   private static final int CAPACITY = 4096;
   private int bpb = 4;
   private NibbleArray data;
   private KList<T> palette = new KList();

   public NibbleDataPalette(T defaultValue) {
      this.data = new NibbleArray(this.bpb, 4096);
      this.data.setAll((byte)-128);
      this.getPaletteId(var1);
   }

   public abstract T readType(DataInputStream i);

   public abstract void writeType(T t, DataOutputStream o);

   public void write(DataOutputStream o) {
      var1.writeByte(this.bpb + -128);
      var1.writeByte(this.palette.size() + -128);
      Iterator var2 = this.palette.iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         this.writeType(var3, var1);
      }

      this.data.write(var1);
   }

   public void read(DataInputStream i) {
      this.bpb = var1.readByte() - -128;
      this.palette = new KList();
      int var2 = var1.readByte() - -128;

      for(int var3 = 0; var3 < var2; ++var3) {
         this.palette.add((Object)this.readType(var1));
      }

      this.data = new NibbleArray(4096, var1);
   }

   private void expand() {
      if (this.bpb < 8) {
         this.changeBitsPerBlock(this.bpb + 1);
      } else {
         throw new IndexOutOfBoundsException("The Data Palette can only handle at most 256 block types per 16x16x16 region. We cannot use more than 8 bits per block!");
      }
   }

   public final void optimize() {
      int var1 = this.bpb;
      int var2 = this.palette.size();

      for(int var3 = 1; var3 < this.bpb; ++var3) {
         if (Math.pow(2.0D, (double)var3) > (double)var2) {
            var1 = var3;
            break;
         }
      }

      this.changeBitsPerBlock(var1);
   }

   private void changeBitsPerBlock(int bits) {
      this.bpb = var1;
      this.data = new NibbleArray(this.bpb, 4096, this.data);
   }

   public final void set(int x, int y, int z, T d) {
      this.data.set(this.getCoordinateIndex(var1, var2, var3), this.getPaletteId(var4));
   }

   public final T get(int x, int y, int z) {
      return this.palette.get(this.data.get(this.getCoordinateIndex(var1, var2, var3)));
   }

   private int getPaletteId(T d) {
      int var2 = this.palette.indexOf(var1);
      if (var2 == -1) {
         var2 = this.palette.size();
         this.palette.add((Object)var1);
         if ((double)this.palette.size() > Math.pow(2.0D, (double)this.bpb)) {
            this.expand();
         }
      }

      return var2 + -128;
   }

   private int getCoordinateIndex(int x, int y, int z) {
      return var2 << 8 | var3 << 4 | var1;
   }
}
