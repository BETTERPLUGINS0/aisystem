package com.volmit.iris.util.nbt.mca.palette;

import com.volmit.iris.util.nbt.tag.CompoundTag;
import com.volmit.iris.util.nbt.tag.ListTag;
import java.util.function.Function;
import java.util.function.Predicate;

public class MCALinearPalette<T> implements MCAPalette<T> {
   private final MCAIdMapper<T> registry;
   private final T[] values;
   private final MCAPaletteResize<T> resizeHandler;
   private final Function<CompoundTag, T> reader;
   private final int bits;
   private int size;

   public MCALinearPalette(MCAIdMapper<T> var0, int var1, MCAPaletteResize<T> var2, Function<CompoundTag, T> var3) {
      this.registry = var1;
      this.values = new Object[1 << var2];
      this.bits = var2;
      this.resizeHandler = var3;
      this.reader = var4;
   }

   public int idFor(T var0) {
      int var2;
      for(var2 = 0; var2 < this.size; ++var2) {
         if (this.values[var2] == var1) {
            return var2;
         }
      }

      var2 = this.size;
      if (var2 < this.values.length) {
         this.values[var2] = var1;
         ++this.size;
         return var2;
      } else {
         return this.resizeHandler.onResize(this.bits + 1, var1);
      }
   }

   public boolean maybeHas(Predicate<T> var0) {
      for(int var2 = 0; var2 < this.size; ++var2) {
         if (var1.test(this.values[var2])) {
            return true;
         }
      }

      return false;
   }

   public T valueFor(int var0) {
      return var1 >= 0 && var1 < this.size ? this.values[var1] : null;
   }

   public int getSize() {
      return this.size;
   }

   public void read(ListTag var0) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         this.values[var2] = this.reader.apply((CompoundTag)var1.get(var2));
      }

      this.size = var1.size();
   }
}
