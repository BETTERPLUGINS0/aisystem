package com.volmit.iris.util.cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import lombok.Generated;

public class FloatCache implements ArrayCache<Float> {
   private final int width;
   private final int height;
   private final float[] cache;

   public FloatCache(File file) {
      this(new DataInputStream(new FileInputStream(var1)));
   }

   public FloatCache(DataInputStream din) {
      this(var1.readInt(), var1.readInt());

      for(int var2 = 0; var2 < this.width * this.height; ++var2) {
         this.cache[var2] = var1.readFloat();
      }

      var1.close();
   }

   public FloatCache(int width, int height) {
      this.width = var1;
      this.height = var2;
      this.cache = new float[var1 * var2];
   }

   public void set(int i, Float v) {
      this.cache[var1] = var2;
   }

   public Float get(int i) {
      return this.cache[var1];
   }

   public void writeCache(DataOutputStream dos) {
      var1.writeInt(this.width);
      var1.writeInt(this.height);

      for(int var2 = 0; var2 < this.width * this.height; ++var2) {
         var1.writeFloat(this.get(var2));
      }

   }

   public Float readNodeData(DataInputStream din) {
      return var1.readFloat();
   }

   public void writeNodeData(DataOutputStream dos, Float integer) {
      var1.writeFloat(var2);
   }

   public void iset(int i, int v) {
      this.set(var1, (float)var2);
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
