package com.volmit.iris.util.hunk.storage;

import com.volmit.iris.engine.data.cache.Cache;
import com.volmit.iris.util.hunk.Hunk;
import java.util.Arrays;
import lombok.Generated;

public class ArrayHunk<T> extends StorageHunk<T> implements Hunk<T> {
   private final T[] data;

   public ArrayHunk(int w, int h, int d) {
      super(var1, var2, var3);
      this.data = new Object[var1 * var2 * var3];
   }

   public void setRaw(int x, int y, int z, T t) {
      this.data[this.index(var1, var2, var3)] = var4;
   }

   public T getRaw(int x, int y, int z) {
      return this.data[this.index(var1, var2, var3)];
   }

   private int index(int x, int y, int z) {
      return Cache.to1D(var1, var2, var3, this.getWidth(), this.getHeight());
   }

   public void fill(T t) {
      Arrays.fill(this.data, var1);
   }

   @Generated
   public T[] getData() {
      return this.data;
   }

   @Generated
   public String toString() {
      return "ArrayHunk(data=" + Arrays.deepToString(this.getData()) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof ArrayHunk)) {
         return false;
      } else {
         ArrayHunk var2 = (ArrayHunk)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            return Arrays.deepEquals(this.getData(), var2.getData());
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof ArrayHunk;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = var2 * 59 + Arrays.deepHashCode(this.getData());
      return var3;
   }
}
