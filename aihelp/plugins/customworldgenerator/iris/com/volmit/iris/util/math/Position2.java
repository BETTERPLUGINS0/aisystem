package com.volmit.iris.util.math;

import com.volmit.iris.engine.object.IrisPosition;
import org.bukkit.util.Vector;

public class Position2 {
   private int x;
   private int z;

   public Position2(int x, int z) {
      this.x = var1;
      this.z = var2;
   }

   public Position2(Vector center) {
      this.x = var1.getBlockX();
      this.z = var1.getBlockZ();
   }

   public int getX() {
      return this.x;
   }

   public void setX(int x) {
      this.x = var1;
   }

   public int getZ() {
      return this.z;
   }

   public void setZ(int z) {
      this.z = var1;
   }

   public String toString() {
      return "[" + this.x + "," + this.z + "]";
   }

   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = 31 * var2 + this.x;
      var3 = 31 * var3 + this.z;
      return var3;
   }

   public Position2 regionToChunk() {
      return new Position2(this.x << 5, this.z << 5);
   }

   public boolean equals(Object obj) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Position2)) {
         return false;
      } else {
         Position2 var2 = (Position2)var1;
         return this.x == var2.x && this.z == var2.z;
      }
   }

   public double distance(Position2 center) {
      return Math.pow((double)(var1.getX() - this.x), 2.0D) + Math.pow((double)(var1.getZ() - this.z), 2.0D);
   }

   public Position2 add(int x, int z) {
      return new Position2(this.x + var1, this.z + var2);
   }

   public Position2 blockToChunk() {
      return new Position2(this.x >> 4, this.z >> 4);
   }

   public IrisPosition toIris() {
      return new IrisPosition(this.x, 23, this.z);
   }
}
