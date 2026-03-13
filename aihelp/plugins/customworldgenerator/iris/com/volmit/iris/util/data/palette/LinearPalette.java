package com.volmit.iris.util.data.palette;

import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class LinearPalette<T> implements Palette<T> {
   private final AtomicReferenceArray<T> values;
   private final PaletteResize<T> resizeHandler;
   private final int bits;
   private int size;

   public LinearPalette(int var1, PaletteResize<T> var2) {
      this.values = new AtomicReferenceArray(1 << var1);
      this.bits = var1;
      this.resizeHandler = var2;
   }

   public int idFor(T var0) {
      int var2;
      for(var2 = 0; var2 < this.size; ++var2) {
         if (this.values.get(var2) == null && var1 == null) {
            return var2;
         }

         if (this.values.get(var2) != null && this.values.get(var2).equals(var1)) {
            return var2;
         }
      }

      var2 = this.size;
      if (var2 < this.values.length()) {
         this.values.set(var2, var1);
         ++this.size;
         return var2;
      } else {
         return this.resizeHandler.onResize(this.bits + 1, var1);
      }
   }

   public T valueFor(int var0) {
      return var1 >= 0 && var1 < this.size ? this.values.get(var1) : null;
   }

   public int getSize() {
      return this.size;
   }

   public void read(List<T> fromList) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         this.values.set(var2, var1.get(var2));
      }

      this.size = var1.size();
   }

   public void write(List<T> toList) {
      for(int var2 = 0; var2 < this.size; ++var2) {
         Object var3 = this.values.get(var2);
         var1.add(var3);
      }

   }
}
