package org.terraform.data;

import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public record SimpleLocation(int x, int y, int z) {
   public SimpleLocation(@NotNull SimpleLocation other) {
      this(other.x, other.y, other.z);
   }

   public SimpleLocation(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   @NotNull
   public SimpleLocation getRelative(int x, int y, int z) {
      return new SimpleLocation(this.x + x, this.y + y, this.z + z);
   }

   @NotNull
   public SimpleLocation getRelative(@NotNull BlockFace face) {
      return new SimpleLocation(this.x + face.getModX(), this.y + face.getModY(), this.z + face.getModZ());
   }

   @NotNull
   public SimpleLocation getRelative(@NotNull BlockFace face, int i) {
      return new SimpleLocation(this.x + face.getModX() * i, this.y + face.getModY() * i, this.z + face.getModZ() * i);
   }

   public float distance(@NotNull SimpleLocation o) {
      return (float)Math.sqrt(Math.pow((double)(o.x - this.x), 2.0D) + Math.pow((double)(o.y - this.y), 2.0D) + Math.pow((double)(o.z - this.z), 2.0D));
   }

   public float distanceSqr(@NotNull SimpleLocation o) {
      return (float)(Math.pow((double)(o.x - this.x), 2.0D) + Math.pow((double)(o.y - this.y), 2.0D) + Math.pow((double)(o.z - this.z), 2.0D));
   }

   public float distanceQuad(@NotNull SimpleLocation o) {
      return (float)Math.pow(Math.pow((double)(o.x - this.x), 2.0D) + Math.pow((double)(o.y - this.y), 2.0D) + Math.pow((double)(o.z - this.z), 2.0D), 4.0D);
   }

   public float distanceSqr(int nx, int ny, int nz) {
      return (float)(Math.pow((double)(nx - this.x), 2.0D) + Math.pow((double)(ny - this.y), 2.0D) + Math.pow((double)(nz - this.z), 2.0D));
   }

   public float twoDAngleTo(@NotNull SimpleLocation o) {
      if (o.x == this.x && o.z == this.z) {
         return 0.0F;
      } else if (o.x == this.x && o.z > this.z) {
         return 0.0F;
      } else if (o.x == this.x) {
         return 3.1415927F;
      } else if (o.x > this.x && o.z == this.z) {
         return 1.5707964F;
      } else if (o.x < this.x && o.z == this.z) {
         return 4.712389F;
      } else if (o.x > this.x && o.z > this.z) {
         return (float)Math.atan((double)(o.x - this.x) / (double)(o.z - this.z));
      } else if (o.x > this.x) {
         return (float)(Math.atan((double)(this.z - o.z) / (double)(o.x - this.x)) + 1.5707963267948966D);
      } else {
         return o.z < this.z ? (float)(Math.atan((double)(this.x - o.x) / (double)(this.z - o.z)) + 3.141592653589793D) : (float)(Math.atan((double)(o.z - this.z) / (double)(this.x - o.x)) + 4.71238898038469D);
      }
   }

   public float twoDAngleWrapTo(@NotNull SimpleLocation o) {
      if (o.x == this.x && o.z == this.z) {
         return 0.0F;
      } else if (o.x == this.x && o.z > this.z) {
         return 0.0F;
      } else if (o.x == this.x) {
         return 3.1415927F;
      } else if (o.x > this.x && o.z == this.z) {
         return 1.5707964F;
      } else if (o.x < this.x && o.z == this.z) {
         return 1.5707964F;
      } else if (o.x > this.x && o.z > this.z) {
         return (float)Math.atan((double)(o.x - this.x) / (double)(o.z - this.z));
      } else if (o.x > this.x) {
         return (float)(Math.atan((double)(this.z - o.z) / (double)(o.x - this.x)) + 1.5707963267948966D);
      } else {
         return o.z < this.z ? (float)(Math.atan((double)(this.z - o.z) / (double)(this.x - o.x)) + 1.5707963267948966D) : (float)Math.atan((double)(this.x - o.x) / (double)(o.z - this.z));
      }
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getZ() {
      return this.z;
   }

   @NotNull
   public String toString() {
      return this.x + "," + this.y + "," + this.z;
   }

   @NotNull
   public SimpleLocation getAtY(int newy) {
      return new SimpleLocation(this.x, newy, this.z);
   }

   @NotNull
   public SimpleLocation getUp(int n) {
      return this.getRelative(0, n, 0);
   }

   @NotNull
   public SimpleLocation getUp() {
      return this.getRelative(0, 1, 0);
   }

   @NotNull
   public SimpleLocation getDown(int n) {
      return this.getRelative(0, -n, 0);
   }

   @NotNull
   public SimpleLocation getDown() {
      return this.getRelative(0, -1, 0);
   }

   public int x() {
      return this.x;
   }

   public int y() {
      return this.y;
   }

   public int z() {
      return this.z;
   }
}
