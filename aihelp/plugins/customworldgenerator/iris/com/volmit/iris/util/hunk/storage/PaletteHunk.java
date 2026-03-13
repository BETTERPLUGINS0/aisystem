package com.volmit.iris.util.hunk.storage;

import com.volmit.iris.util.function.Consumer4;
import com.volmit.iris.util.function.Consumer4IO;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.hunk.bits.DataContainer;
import com.volmit.iris.util.hunk.bits.Writable;
import lombok.Generated;

public class PaletteHunk<T> extends StorageHunk<T> implements Hunk<T> {
   private DataContainer<T> data;

   public PaletteHunk(int w, int h, int d, Writable<T> writer) {
      super(var1, var2, var3);
      this.data = new DataContainer(var4, var1 * var2 * var3);
   }

   public void setPalette(DataContainer<T> c) {
      this.data = var1;
   }

   public boolean isMapped() {
      return false;
   }

   private int index(int x, int y, int z) {
      return var3 * this.getWidth() * this.getHeight() + var2 * this.getWidth() + var1;
   }

   public synchronized Hunk<T> iterateSync(Consumer4<Integer, Integer, Integer, T> c) {
      for(int var2 = 0; var2 < this.getWidth(); ++var2) {
         for(int var3 = 0; var3 < this.getHeight(); ++var3) {
            for(int var4 = 0; var4 < this.getDepth(); ++var4) {
               Object var5 = this.getRaw(var2, var3, var4);
               if (var5 != null) {
                  var1.accept(var2, var3, var4, var5);
               }
            }
         }
      }

      return this;
   }

   public synchronized Hunk<T> iterateSyncIO(Consumer4IO<Integer, Integer, Integer, T> c) {
      for(int var2 = 0; var2 < this.getWidth(); ++var2) {
         for(int var3 = 0; var3 < this.getHeight(); ++var3) {
            for(int var4 = 0; var4 < this.getDepth(); ++var4) {
               Object var5 = this.getRaw(var2, var3, var4);
               if (var5 != null) {
                  var1.accept(var2, var3, var4, var5);
               }
            }
         }
      }

      return this;
   }

   public void setRaw(int x, int y, int z, T t) {
      this.data.set(this.index(var1, var2, var3), var4);
   }

   public T getRaw(int x, int y, int z) {
      return this.data.get(this.index(var1, var2, var3));
   }

   @Generated
   public DataContainer<T> getData() {
      return this.data;
   }

   @Generated
   public void setData(final DataContainer<T> data) {
      this.data = var1;
   }

   @Generated
   public String toString() {
      return "PaletteHunk(data=" + String.valueOf(this.getData()) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof PaletteHunk)) {
         return false;
      } else {
         PaletteHunk var2 = (PaletteHunk)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            DataContainer var3 = this.getData();
            DataContainer var4 = var2.getData();
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
      return var1 instanceof PaletteHunk;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      DataContainer var3 = this.getData();
      int var4 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }
}
