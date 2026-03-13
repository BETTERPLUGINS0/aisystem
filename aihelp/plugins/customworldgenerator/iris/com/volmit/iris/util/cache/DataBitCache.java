package com.volmit.iris.util.cache;

import com.volmit.iris.util.hunk.bits.DataContainer;
import java.io.DataOutputStream;
import lombok.Generated;

public abstract class DataBitCache<T> implements ArrayCache<T> {
   private final int width;
   private final int height;
   private final DataContainer<T> cache;

   public DataBitCache(int width, int height) {
      this.width = var1;
      this.height = var2;
      this.cache = new DataContainer(this, var1 * var2);
   }

   public void set(int i, T v) {
      this.cache.set(var1, var2);
   }

   public T get(int i) {
      return this.cache.get(var1);
   }

   public void writeCache(DataOutputStream dos) {
      var1.writeInt(this.width);
      var1.writeInt(this.height);
      this.cache.writeDos(var1);
   }

   @Generated
   public int getWidth() {
      return this.width;
   }

   @Generated
   public int getHeight() {
      return this.height;
   }
}
