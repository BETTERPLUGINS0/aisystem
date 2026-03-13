package com.volmit.iris.util.data.palette;

import com.volmit.iris.Iris;
import com.volmit.iris.util.math.M;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import java.util.List;

public class PalettedContainer<T> implements PaletteResize<T> {
   public static final int GLOBAL_PALETTE_BITS = 9;
   public static final int MIN_PALETTE_SIZE = 4;
   private static final int SIZE = 4096;
   private final PaletteResize<T> dummyPaletteResize = (var0, var1) -> {
      return 0;
   };
   protected BitStorage storage;
   private Palette<T> palette;
   private int bits;

   public PalettedContainer() {
      this.setBits(4);
   }

   private static int getIndex(int var0, int var1, int var2) {
      return var1 << 8 | var2 << 4 | var0;
   }

   private void setBits(int var0) {
      if (var1 != this.bits) {
         this.bits = var1;
         if (this.bits <= 4) {
            this.bits = 4;
            this.palette = new LinearPalette(this.bits, this);
         } else {
            this.palette = new HashMapPalette(this.bits, this);
         }

         this.palette.idFor((Object)null);
         this.storage = new BitStorage(this.bits, 4096);
      }
   }

   public int onResize(int var0, T var1) {
      BitStorage var3 = this.storage;
      Palette var4 = this.palette;
      this.setBits(var1);

      for(int var5 = 0; var5 < var3.getSize(); ++var5) {
         Object var6 = var4.valueFor(var3.get(var5));
         if (var6 != null) {
            this.set(var5, var6);
         }
      }

      return this.palette.idFor(var2);
   }

   public T getAndSet(int var0, int var1, int var2, T var3) {
      return this.getAndSet(getIndex(var1, var2, var3), var4);
   }

   public T getAndSetUnchecked(int var0, int var1, int var2, T var3) {
      return this.getAndSet(getIndex(var1, var2, var3), var4);
   }

   private T getAndSet(int var0, T var1) {
      int var3 = this.palette.idFor(var2);
      int var4 = this.storage.getAndSet(var1, var3);
      return this.palette.valueFor(var4);
   }

   public void set(int var0, int var1, int var2, T var3) {
      this.set(getIndex(var1, var2, var3), var4);
   }

   private void set(int var0, T var1) {
      int var3 = this.palette.idFor(var2);
      if (M.r(0.003D)) {
         Iris.info("ID for " + String.valueOf(var2) + " is " + var3 + " Palette: " + this.palette.getSize());
      }

      this.storage.set(var1, var3);
   }

   public T get(int var0, int var1, int var2) {
      return this.get(getIndex(var1, var2, var3));
   }

   protected T get(int var0) {
      return this.palette.valueFor(this.storage.get(var1));
   }

   public void read(List<T> palette, long[] data) {
      int var3 = Math.max(4, Mth.ceillog2(var1.size()));
      if (var3 != this.bits) {
         this.setBits(var3);
      }

      this.palette.read(var1);
      int var4 = var2.length * 64 / 4096;
      if (var4 == this.bits) {
         System.arraycopy(var2, 0, this.storage.getRaw(), 0, var2.length);
      } else {
         BitStorage var5 = new BitStorage(var4, 4096, var2);

         for(int var6 = 0; var6 < 4096; ++var6) {
            this.storage.set(var6, var5.get(var6));
         }
      }

   }

   public long[] write(List<T> toList) {
      HashMapPalette var2 = new HashMapPalette(this.bits, this.dummyPaletteResize);
      Object var3 = null;
      int var4 = 0;
      int[] var5 = new int[4096];

      int var6;
      for(var6 = 0; var6 < 4096; ++var6) {
         Object var7 = this.get(var6);
         if (var7 != var3) {
            var3 = var7;
            var4 = var2.idFor(var7);
         }

         var5[var6] = var4;
      }

      var2.write(var1);
      var6 = Math.max(4, Mth.ceillog2(var1.size()));
      BitStorage var9 = new BitStorage(var6, 4096);

      for(int var8 = 0; var8 < var5.length; ++var8) {
         var9.set(var8, var5[var8]);
      }

      return var9.getRaw();
   }

   public void count(CountConsumer<T> var0) {
      Int2IntOpenHashMap var2 = new Int2IntOpenHashMap();
      this.storage.getAll((var1x) -> {
         var2.put(var1x, var2.get(var1x) + 1);
      });
      var2.int2IntEntrySet().forEach((var2x) -> {
         var1.accept(this.palette.valueFor(var2x.getIntKey()), var2x.getIntValue());
      });
   }
}
