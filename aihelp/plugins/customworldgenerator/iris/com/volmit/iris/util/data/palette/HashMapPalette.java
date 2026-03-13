package com.volmit.iris.util.data.palette;

import com.volmit.iris.Iris;
import com.volmit.iris.util.collection.KMap;
import java.util.List;

public class HashMapPalette<T> implements Palette<T> {
   private final KMap<T, Integer> values;
   private final PaletteResize<T> resizeHandler;
   private final int bits;
   private int id;

   public HashMapPalette(int var1, PaletteResize<T> var2) {
      this.bits = var1;
      this.resizeHandler = var2;
      this.values = new KMap();
      this.id = 1;
   }

   public int idFor(T var0) {
      return var1 == null ? 0 : (Integer)this.values.computeIfAbsent(var1, (var2) -> {
         int var3 = this.id++;
         if (var3 >= 1 << this.bits) {
            Iris.info(var3 + " to...");
            var3 = this.resizeHandler.onResize(this.bits + 1, var1);
            Iris.info(var3 + "..");
         }

         return var3;
      });
   }

   public T valueFor(int var0) {
      return this.values.getKey(var1);
   }

   public int getSize() {
      return this.values.size();
   }

   public void read(List<T> data) {
      var1.forEach(this::idFor);
   }

   public void write(List<T> toList) {
      var1.addAll(this.values.keySet());
   }
}
