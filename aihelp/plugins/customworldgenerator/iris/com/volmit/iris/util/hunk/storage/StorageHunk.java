package com.volmit.iris.util.hunk.storage;

import com.volmit.iris.util.hunk.Hunk;
import lombok.Generated;

public abstract class StorageHunk<T> implements Hunk<T> {
   private final int width;
   private final int height;
   private final int depth;

   public StorageHunk(int width, int height, int depth) {
      if (var1 > 0 && var2 > 0 && var3 > 0) {
         this.width = var1;
         this.height = var2;
         this.depth = var3;
      } else {
         throw new RuntimeException("Unsupported size " + var1 + " " + var2 + " " + var3);
      }
   }

   public abstract void setRaw(int x, int y, int z, T t);

   public abstract T getRaw(int x, int y, int z);

   @Generated
   public int getWidth() {
      return this.width;
   }

   @Generated
   public int getHeight() {
      return this.height;
   }

   @Generated
   public int getDepth() {
      return this.depth;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof StorageHunk)) {
         return false;
      } else {
         StorageHunk var2 = (StorageHunk)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getWidth() != var2.getWidth()) {
            return false;
         } else if (this.getHeight() != var2.getHeight()) {
            return false;
         } else {
            return this.getDepth() == var2.getDepth();
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof StorageHunk;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = var2 * 59 + this.getWidth();
      var3 = var3 * 59 + this.getHeight();
      var3 = var3 * 59 + this.getDepth();
      return var3;
   }

   @Generated
   public String toString() {
      int var10000 = this.getWidth();
      return "StorageHunk(width=" + var10000 + ", height=" + this.getHeight() + ", depth=" + this.getDepth() + ")";
   }
}
