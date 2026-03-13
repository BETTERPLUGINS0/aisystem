package com.volmit.iris.util.nbt.mca.palette;

import com.volmit.iris.util.nbt.tag.ListTag;
import java.util.function.Predicate;

public class MCAGlobalPalette<T> implements MCAPalette<T> {
   private final MCAIdMapper<T> registry;
   private final T defaultValue;

   public MCAGlobalPalette(MCAIdMapper<T> var0, T var1) {
      this.registry = var1;
      this.defaultValue = var2;
   }

   public int idFor(T var0) {
      int var2 = this.registry.getId(var1);
      return var2 == -1 ? 0 : var2;
   }

   public boolean maybeHas(Predicate<T> var0) {
      return true;
   }

   public T valueFor(int var0) {
      Object var2 = this.registry.byId(var1);
      return var2 == null ? this.defaultValue : var2;
   }

   public int getSize() {
      return this.registry.size();
   }

   public void read(ListTag var0) {
   }
}
