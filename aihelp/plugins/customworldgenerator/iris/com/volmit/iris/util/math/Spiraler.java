package com.volmit.iris.util.math;

public class Spiraler {
   private final Spiraled spiraled;
   int x;
   int z;
   int dx;
   int dz;
   int sizeX;
   int sizeZ;
   int t;
   int maxI;
   int i;
   int ox = 0;
   int oz = 0;

   public Spiraler(int sizeX, int sizeZ, Spiraled spiraled) {
      this.spiraled = var3;
      this.retarget(var1, var2);
   }

   static void Spiral(int X, int Y) {
   }

   public void drain() {
      while(this.hasNext()) {
         this.next();
      }

   }

   public Spiraler setOffset(int ox, int oz) {
      this.ox = var1;
      this.oz = var2;
      return this;
   }

   public void retarget(int sizeX, int sizeZ) {
      this.sizeX = var1;
      this.sizeZ = var2;
      this.x = this.z = this.dx = 0;
      this.dz = -1;
      this.i = 0;
      this.t = Math.max(var1, var2);
      this.maxI = this.t * this.t;
   }

   public boolean hasNext() {
      return this.i < this.maxI;
   }

   public void next() {
      if (-this.sizeX / 2 <= this.x && this.x <= this.sizeX / 2 && -this.sizeZ / 2 <= this.z && this.z <= this.sizeZ / 2) {
         this.spiraled.on(this.x + this.ox, this.z + this.oz);
      }

      if (this.x == this.z || this.x < 0 && this.x == -this.z || this.x > 0 && this.x == 1 - this.z) {
         this.t = this.dx;
         this.dx = -this.dz;
         this.dz = this.t;
      }

      this.x += this.dx;
      this.z += this.dz;
      ++this.i;
   }

   public int count() {
      int var1;
      for(var1 = 0; this.hasNext(); ++var1) {
         this.next();
      }

      return var1;
   }
}
