package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.Snippet;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

@Snippet("position-3d")
@Desc("Represents a position")
public class IrisPosition {
   @Desc("The x position")
   private int x;
   @Desc("The y position")
   private int y;
   @Desc("The z position")
   private int z;

   public IrisPosition(BlockVector bv) {
      this(var1.getBlockX(), var1.getBlockY(), var1.getBlockZ());
   }

   public IrisPosition(Location l) {
      this(var1.getBlockX(), var1.getBlockY(), var1.getBlockZ());
   }

   public IrisPosition(Vector v) {
      this(var1.getBlockX(), var1.getBlockY(), var1.getBlockZ());
   }

   public IrisPosition(double x, double y, double z) {
      this((int)var1, (int)var3, (int)var5);
   }

   public IrisPosition add(IrisPosition relativePosition) {
      return new IrisPosition(var1.x + this.x, var1.y + this.y, var1.z + this.z);
   }

   public IrisPosition sub(IrisPosition relativePosition) {
      return new IrisPosition(this.x - var1.x, this.y - var1.y, this.z - var1.z);
   }

   public Location toLocation(World world) {
      return new Location(var1, (double)this.x, (double)this.y, (double)this.z);
   }

   public IrisPosition copy() {
      return new IrisPosition(this.x, this.y, this.z);
   }

   public String toString() {
      int var10000 = this.getX();
      return "[" + var10000 + "," + this.getY() + "," + this.getZ() + "]";
   }

   public boolean isLongerThan(IrisPosition s, int maxLength) {
      return Math.abs(Math.pow((double)(var1.x - this.x), 2.0D) + Math.pow((double)(var1.y - this.y), 2.0D) + Math.pow((double)(var1.z - this.z), 2.0D)) > (double)(var2 * var2);
   }

   public Vector toVector() {
      return new Vector(this.x, this.y, this.z);
   }

   @Generated
   public IrisPosition() {
      this.x = 0;
      this.y = 0;
      this.z = 0;
   }

   @Generated
   public IrisPosition(final int x, final int y, final int z) {
      this.x = 0;
      this.y = 0;
      this.z = 0;
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   @Generated
   public int getX() {
      return this.x;
   }

   @Generated
   public int getY() {
      return this.y;
   }

   @Generated
   public int getZ() {
      return this.z;
   }

   @Generated
   public IrisPosition setX(final int x) {
      this.x = var1;
      return this;
   }

   @Generated
   public IrisPosition setY(final int y) {
      this.y = var1;
      return this;
   }

   @Generated
   public IrisPosition setZ(final int z) {
      this.z = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisPosition)) {
         return false;
      } else {
         IrisPosition var2 = (IrisPosition)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getX() != var2.getX()) {
            return false;
         } else if (this.getY() != var2.getY()) {
            return false;
         } else {
            return this.getZ() == var2.getZ();
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisPosition;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = var2 * 59 + this.getX();
      var3 = var3 * 59 + this.getY();
      var3 = var3 * 59 + this.getZ();
      return var3;
   }
}
