package com.volmit.iris.util.cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import lombok.Generated;

public class IntCache implements ArrayCache<Integer> {
   private final int width;
   private final int height;
   private final int[] cache;

   public IntCache(int width, int height) {
      this.width = var1;
      this.height = var2;
      this.cache = new int[var1 * var2];
   }

   public void set(int i, Integer v) {
      this.cache[var1] = var2;
   }

   public Integer get(int i) {
      return this.cache[var1];
   }

   public void writeCache(DataOutputStream dos) {
      var1.writeInt(this.width);
      var1.writeInt(this.height);

      for(int var2 = 0; var2 < this.width * this.height; ++var2) {
         var1.writeInt(this.get(var2));
      }

   }

   public Integer readNodeData(DataInputStream din) {
      return var1.readInt();
   }

   public void writeNodeData(DataOutputStream dos, Integer integer) {
      var1.writeInt(var2);
   }

   public void iset(int i, int v) {
      this.set(var1, var2);
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
