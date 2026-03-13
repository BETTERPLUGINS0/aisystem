package com.volmit.iris.util.hunk.storage;

import com.volmit.iris.util.hunk.Hunk;
import java.util.concurrent.atomic.AtomicLongArray;
import lombok.Generated;

public class AtomicLongHunk extends StorageHunk<Long> implements Hunk<Long> {
   private final AtomicLongArray data;

   public AtomicLongHunk(int w, int h, int d) {
      super(var1, var2, var3);
      this.data = new AtomicLongArray(var1 * var2 * var3);
   }

   public boolean isAtomic() {
      return true;
   }

   public void setRaw(int x, int y, int z, Long t) {
      this.data.set(this.index(var1, var2, var3), var4);
   }

   public Long getRaw(int x, int y, int z) {
      return this.data.get(this.index(var1, var2, var3));
   }

   private int index(int x, int y, int z) {
      return var3 * this.getWidth() * this.getHeight() + var2 * this.getWidth() + var1;
   }

   @Generated
   public AtomicLongArray getData() {
      return this.data;
   }

   @Generated
   public String toString() {
      return "AtomicLongHunk(data=" + String.valueOf(this.getData()) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof AtomicLongHunk)) {
         return false;
      } else {
         AtomicLongHunk var2 = (AtomicLongHunk)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            AtomicLongArray var3 = this.getData();
            AtomicLongArray var4 = var2.getData();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof AtomicLongHunk;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      AtomicLongArray var3 = this.getData();
      int var4 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }
}
