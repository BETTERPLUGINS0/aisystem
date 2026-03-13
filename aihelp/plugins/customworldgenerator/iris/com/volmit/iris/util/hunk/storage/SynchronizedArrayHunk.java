package com.volmit.iris.util.hunk.storage;

import com.volmit.iris.util.hunk.Hunk;
import java.util.Arrays;
import lombok.Generated;

public class SynchronizedArrayHunk<T> extends StorageHunk<T> implements Hunk<T> {
   private final T[] data;

   public SynchronizedArrayHunk(int w, int h, int d) {
      super(var1, var2, var3);
      this.data = new Object[var1 * var2 * var3];
   }

   public void setRaw(int x, int y, int z, T t) {
      synchronized(this.data) {
         this.data[this.index(var1, var2, var3)] = var4;
      }
   }

   public T getRaw(int x, int y, int z) {
      synchronized(this.data) {
         return this.data[this.index(var1, var2, var3)];
      }
   }

   private int index(int x, int y, int z) {
      return var3 * this.getWidth() * this.getHeight() + var2 * this.getWidth() + var1;
   }

   public void fill(T t) {
      synchronized(this.data) {
         Arrays.fill(this.data, var1);
      }
   }

   @Generated
   public T[] getData() {
      return this.data;
   }

   @Generated
   public String toString() {
      return "SynchronizedArrayHunk(data=" + Arrays.deepToString(this.getData()) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof SynchronizedArrayHunk)) {
         return false;
      } else {
         SynchronizedArrayHunk var2 = (SynchronizedArrayHunk)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            return Arrays.deepEquals(this.getData(), var2.getData());
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof SynchronizedArrayHunk;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = var2 * 59 + Arrays.deepHashCode(this.getData());
      return var3;
   }
}
