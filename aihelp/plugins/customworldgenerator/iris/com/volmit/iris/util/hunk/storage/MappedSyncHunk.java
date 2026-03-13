package com.volmit.iris.util.hunk.storage;

import com.volmit.iris.util.function.Consumer4;
import com.volmit.iris.util.function.Consumer4IO;
import com.volmit.iris.util.hunk.Hunk;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Generated;

public class MappedSyncHunk<T> extends StorageHunk<T> implements Hunk<T> {
   private final Map<Integer, T> data = new HashMap();

   public MappedSyncHunk(int w, int h, int d) {
      super(var1, var2, var3);
   }

   public int getEntryCount() {
      return this.data.size();
   }

   public boolean isMapped() {
      return true;
   }

   public boolean isEmpty() {
      synchronized(this.data) {
         return this.data.isEmpty();
      }
   }

   public void setRaw(int x, int y, int z, T t) {
      synchronized(this.data) {
         if (var4 == null) {
            this.data.remove(this.index(var1, var2, var3));
         } else {
            this.data.put(this.index(var1, var2, var3), var4);
         }
      }
   }

   private Integer index(int x, int y, int z) {
      return var3 * this.getWidth() * this.getHeight() + var2 * this.getWidth() + var1;
   }

   public synchronized Hunk<T> iterateSync(Consumer4<Integer, Integer, Integer, T> c) {
      synchronized(this.data) {
         Iterator var5 = this.data.entrySet().iterator();

         while(var5.hasNext()) {
            Entry var6 = (Entry)var5.next();
            int var3 = (Integer)var6.getKey();
            int var4 = var3 / (this.getWidth() * this.getHeight());
            var3 -= var4 * this.getWidth() * this.getHeight();
            var1.accept(var3 % this.getWidth(), var3 / this.getWidth(), var4, var6.getValue());
         }

         return this;
      }
   }

   public synchronized Hunk<T> iterateSyncIO(Consumer4IO<Integer, Integer, Integer, T> c) {
      synchronized(this.data) {
         Iterator var5 = this.data.entrySet().iterator();

         while(var5.hasNext()) {
            Entry var6 = (Entry)var5.next();
            int var3 = (Integer)var6.getKey();
            int var4 = var3 / (this.getWidth() * this.getHeight());
            var3 -= var4 * this.getWidth() * this.getHeight();
            var1.accept(var3 % this.getWidth(), var3 / this.getWidth(), var4, var6.getValue());
         }

         return this;
      }
   }

   public void empty(T b) {
      synchronized(this.data) {
         this.data.clear();
      }
   }

   public T getRaw(int x, int y, int z) {
      synchronized(this.data) {
         return this.data.get(this.index(var1, var2, var3));
      }
   }

   @Generated
   public Map<Integer, T> getData() {
      return this.data;
   }

   @Generated
   public String toString() {
      return "MappedSyncHunk(data=" + String.valueOf(this.getData()) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof MappedSyncHunk)) {
         return false;
      } else {
         MappedSyncHunk var2 = (MappedSyncHunk)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            Map var3 = this.getData();
            Map var4 = var2.getData();
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
      return var1 instanceof MappedSyncHunk;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      Map var3 = this.getData();
      int var4 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }
}
