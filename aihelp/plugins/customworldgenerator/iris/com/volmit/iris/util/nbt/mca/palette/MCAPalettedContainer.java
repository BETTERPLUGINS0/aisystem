package com.volmit.iris.util.nbt.mca.palette;

import com.volmit.iris.util.nbt.tag.CompoundTag;
import com.volmit.iris.util.nbt.tag.ListTag;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class MCAPalettedContainer<T> implements MCAPaletteResize<T> {
   public static final int GLOBAL_PALETTE_BITS = 9;
   public static final int MIN_PALETTE_SIZE = 4;
   private static final int SIZE = 4096;
   private final MCAPalette<T> globalPalette;
   private final MCAPaletteResize<T> dummyPaletteResize = (var0, var1x) -> {
      return 0;
   };
   private final MCAIdMapper<T> registry;
   private final Function<CompoundTag, T> reader;
   private final Function<T, CompoundTag> writer;
   private final T defaultValue;
   protected MCABitStorage storage;
   private MCAPalette<T> palette;
   private int bits;

   public MCAPalettedContainer(MCAPalette<T> var0, MCAIdMapper<T> var1, Function<CompoundTag, T> var2, Function<T, CompoundTag> var3, T var4) {
      this.globalPalette = var1;
      this.registry = var2;
      this.reader = var3;
      this.writer = var4;
      this.defaultValue = var5;
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
            this.palette = new MCALinearPalette(this.registry, this.bits, this, this.reader);
         } else if (this.bits < 9) {
            this.palette = new MCAHashMapPalette(this.registry, this.bits, this, this.reader, this.writer);
         } else {
            this.palette = this.globalPalette;
            this.bits = MCAMth.ceillog2(this.registry.size());
         }

         this.palette.idFor(this.defaultValue);
         this.storage = new MCABitStorage(this.bits, 4096);
      }
   }

   public int onResize(int var0, T var1) {
      MCABitStorage var3 = this.storage;
      MCAPalette var4 = this.palette;
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
      Object var5 = this.palette.valueFor(var4);
      return var5 == null ? this.defaultValue : var5;
   }

   public void set(int var0, int var1, int var2, T var3) {
      this.set(getIndex(var1, var2, var3), var4);
   }

   private void set(int var0, T var1) {
      int var3 = this.palette.idFor(var2);
      this.storage.set(var1, var3);
   }

   public T get(int var0, int var1, int var2) {
      return this.get(getIndex(var1, var2, var3));
   }

   protected T get(int var0) {
      Object var2 = this.palette.valueFor(this.storage.get(var1));
      return var2 == null ? this.defaultValue : var2;
   }

   public void read(ListTag var0, long[] var1) {
      int var3 = Math.max(4, MCAMth.ceillog2(var1.size()));
      if (var3 != this.bits) {
         this.setBits(var3);
      }

      this.palette.read(var1);
      int var4 = var2.length * 64 / 4096;
      if (this.palette == this.globalPalette) {
         MCAHashMapPalette var5 = new MCAHashMapPalette(this.registry, var3, this.dummyPaletteResize, this.reader, this.writer);
         var5.read(var1);
         MCABitStorage var6 = new MCABitStorage(var3, 4096, var2);

         for(int var7 = 0; var7 < 4096; ++var7) {
            this.storage.set(var7, this.globalPalette.idFor(var5.valueFor(var6.get(var7))));
         }
      } else if (var4 == this.bits) {
         System.arraycopy(var2, 0, this.storage.getRaw(), 0, var2.length);
      } else {
         MCABitStorage var8 = new MCABitStorage(var4, 4096, var2);

         for(int var9 = 0; var9 < 4096; ++var9) {
            this.storage.set(var9, var8.get(var9));
         }
      }

   }

   public void write(CompoundTag var0, String var1, String var2) {
      MCAHashMapPalette var4 = new MCAHashMapPalette(this.registry, this.bits, this.dummyPaletteResize, this.reader, this.writer);
      Object var5 = this.defaultValue;
      int var6 = var4.idFor(this.defaultValue);
      int[] var7 = new int[4096];

      for(int var8 = 0; var8 < 4096; ++var8) {
         Object var9 = this.get(var8);
         if (var9 != var5) {
            var5 = var9;
            var6 = var4.idFor(var9);
         }

         var7[var8] = var6;
      }

      ListTag var12 = ListTag.createUnchecked(CompoundTag.class);
      var4.write(var12);
      var1.put(var2, var12);
      int var13 = Math.max(4, MCAMth.ceillog2(var12.size()));
      MCABitStorage var10 = new MCABitStorage(var13, 4096);

      for(int var11 = 0; var11 < var7.length; ++var11) {
         var10.set(var11, var7[var11]);
      }

      var1.putLongArray(var3, var10.getRaw());
   }

   public boolean maybeHas(Predicate<T> var0) {
      return this.palette.maybeHas(var1);
   }

   public void count(MCACountConsumer<T> var0) {
      Int2IntOpenHashMap var2 = new Int2IntOpenHashMap();
      this.storage.getAll((var1x) -> {
         var2.put(var1x, var2.get(var1x) + 1);
      });
      var2.int2IntEntrySet().forEach((var2x) -> {
         var1.accept(this.palette.valueFor(var2x.getIntKey()), var2x.getIntValue());
      });
   }
}
