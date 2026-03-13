package com.volmit.iris.util.cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import lombok.Generated;

public class ShortCache implements ArrayCache<Short> {
   private final int width;
   private final int height;
   private final short[] cache;

   public ShortCache(int width, int height) {
      this.width = var1;
      this.height = var2;
      this.cache = new short[var1 * var2];
   }

   public void set(int i, Short v) {
      this.cache[var1] = var2;
   }

   public Short get(int i) {
      return this.cache[var1];
   }

   public void writeCache(DataOutputStream dos) {
      var1.writeInt(this.width);
      var1.writeInt(this.height);

      for(int var2 = 0; var2 < this.width * this.height; ++var2) {
         var1.writeShort(this.get(var2));
      }

   }

   public Short readNodeData(DataInputStream din) {
      return var1.readShort();
   }

   public void writeNodeData(DataOutputStream dos, Short integer) {
      var1.writeShort(var2);
   }

   public void iset(int i, int v) {
      this.set(var1, (short)var2);
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
