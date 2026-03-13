package com.volmit.iris.util.data.palette;

import java.util.List;
import java.util.function.Predicate;

public class GlobalPalette<T> implements Palette<T> {
   private final IdMapper<T> registry;
   private final T defaultValue;

   public GlobalPalette(T... f) {
      IdMapper var2 = new IdMapper();
      Object[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object var6 = var3[var5];
         var2.add(var6);
      }

      this.registry = var2;
      this.defaultValue = var1[0];
   }

   public GlobalPalette(IdMapper<T> var0, T var1) {
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

   public void read(List<T> fromList) {
   }

   public void write(List<T> toList) {
   }
}
