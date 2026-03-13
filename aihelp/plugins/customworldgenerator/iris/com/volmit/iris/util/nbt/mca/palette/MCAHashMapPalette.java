package com.volmit.iris.util.nbt.mca.palette;

import com.volmit.iris.util.nbt.tag.CompoundTag;
import com.volmit.iris.util.nbt.tag.ListTag;
import com.volmit.iris.util.nbt.tag.Tag;
import java.util.function.Function;
import java.util.function.Predicate;

public class MCAHashMapPalette<T> implements MCAPalette<T> {
   private final MCAIdMapper<T> registry;
   private final MCACrudeIncrementalIntIdentityHashBiMap<T> values;
   private final MCAPaletteResize<T> resizeHandler;
   private final Function<CompoundTag, T> reader;
   private final Function<T, CompoundTag> writer;
   private final int bits;

   public MCAHashMapPalette(MCAIdMapper<T> var0, int var1, MCAPaletteResize<T> var2, Function<CompoundTag, T> var3, Function<T, CompoundTag> var4) {
      this.registry = var1;
      this.bits = var2;
      this.resizeHandler = var3;
      this.reader = var4;
      this.writer = var5;
      this.values = new MCACrudeIncrementalIntIdentityHashBiMap(1 << var2);
   }

   public int idFor(T var0) {
      int var2 = this.values.getId(var1);
      if (var2 == -1) {
         var2 = this.values.add(var1);
         if (var2 >= 1 << this.bits) {
            var2 = this.resizeHandler.onResize(this.bits + 1, var1);
         }
      }

      return var2;
   }

   public boolean maybeHas(Predicate<T> var0) {
      for(int var2 = 0; var2 < this.getSize(); ++var2) {
         if (var1.test(this.values.byId(var2))) {
            return true;
         }
      }

      return false;
   }

   public T valueFor(int var0) {
      return this.values.byId(var1);
   }

   public int getSize() {
      return this.values.size();
   }

   public void read(ListTag var0) {
      this.values.clear();

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         this.values.add(this.reader.apply((CompoundTag)var1.get(var2)));
      }

   }

   public void write(ListTag var0) {
      for(int var2 = 0; var2 < this.getSize(); ++var2) {
         var1.add((Tag)this.writer.apply(this.values.byId(var2)));
      }

   }
}
