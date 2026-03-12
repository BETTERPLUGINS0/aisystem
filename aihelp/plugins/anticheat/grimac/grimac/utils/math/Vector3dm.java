package ac.grim.grimac.utils.math;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3f;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.io.Serializable;
import java.util.Random;
import lombok.Generated;

public class Vector3dm implements Cloneable, Serializable {
   private static final long serialVersionUID = -2657651106777219169L;
   private static final Random random = new Random();
   public static final double epsilon = 1.0E-6D;
   protected double x;
   protected double y;
   protected double z;

   @Contract(
      pure = true
   )
   public Vector3dm() {
      this.x = 0.0D;
      this.y = 0.0D;
      this.z = 0.0D;
   }

   @Contract(
      pure = true
   )
   public Vector3dm(int x, int y, int z) {
      this.x = (double)x;
      this.y = (double)y;
      this.z = (double)z;
   }

   @Contract(
      pure = true
   )
   public Vector3dm(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   @Contract(
      pure = true
   )
   public Vector3dm(float x, float y, float z) {
      this.x = (double)x;
      this.y = (double)y;
      this.z = (double)z;
   }

   @Contract("_, _ -> new")
   @NotNull
   public static Vector3dm min(@NotNull Vector3dm a, @NotNull Vector3dm b) {
      return new Vector3dm(Math.min(a.x, b.x), Math.min(a.y, b.y), Math.min(a.z, b.z));
   }

   @Contract("_, _ -> new")
   @NotNull
   public static Vector3dm max(@NotNull Vector3dm a, @NotNull Vector3dm b) {
      return new Vector3dm(Math.max(a.x, b.x), Math.max(a.y, b.y), Math.max(a.z, b.z));
   }

   @Contract(" -> new")
   @NotNull
   public static Vector3dm getRandom() {
      return new Vector3dm(random.nextDouble(), random.nextDouble(), random.nextDouble());
   }

   @NotNull
   public Vector3dm add(@NotNull Vector3dm vec) {
      return this.add(vec.x, vec.y, vec.z);
   }

   @NotNull
   public Vector3dm add(double x, double y, double z) {
      this.x += x;
      this.y += y;
      this.z += z;
      return this;
   }

   @NotNull
   public Vector3dm subtract(@NotNull Vector3dm vec) {
      return this.subtract(vec.x, vec.y, vec.z);
   }

   @NotNull
   public Vector3dm subtract(double x, double y, double z) {
      this.x -= x;
      this.y -= y;
      this.z -= z;
      return this;
   }

   @NotNull
   public Vector3dm multiply(@NotNull Vector3dm vec) {
      return this.multiply(vec.x, vec.y, vec.z);
   }

   @NotNull
   public Vector3dm multiply(double x, double y, double z) {
      this.x *= x;
      this.y *= y;
      this.z *= z;
      return this;
   }

   @NotNull
   public Vector3dm divide(@NotNull Vector3dm vec) {
      this.x /= vec.x;
      this.y /= vec.y;
      this.z /= vec.z;
      return this;
   }

   @NotNull
   public Vector3dm copy(@NotNull Vector3dm vec) {
      this.x = vec.x;
      this.y = vec.y;
      this.z = vec.z;
      return this;
   }

   public double length() {
      return Math.sqrt(GrimMath.square(this.x) + GrimMath.square(this.y) + GrimMath.square(this.z));
   }

   public double lengthSquared() {
      return GrimMath.square(this.x) + GrimMath.square(this.y) + GrimMath.square(this.z);
   }

   public double distance(@NotNull Vector3dm o) {
      return Math.sqrt(this.distanceSquared(o));
   }

   public double distanceSquared(@NotNull Vector3dm o) {
      return this.distanceSquared(o.x, o.y, o.z);
   }

   public double distance(double oX, double oY, double oZ) {
      return Math.sqrt(this.distanceSquared(oX, oY, oZ));
   }

   public double distanceSquared(double oX, double oY, double oZ) {
      return GrimMath.square(this.x - oX) + GrimMath.square(this.y - oY) + GrimMath.square(this.z - oZ);
   }

   @NotNull
   public Vector3dm midpoint(@NotNull Vector3dm other) {
      this.x = (this.x + other.x) / 2.0D;
      this.y = (this.y + other.y) / 2.0D;
      this.z = (this.z + other.z) / 2.0D;
      return this;
   }

   @NotNull
   public Vector3dm getMidpoint(@NotNull Vector3dm other) {
      double x = (this.x + other.x) / 2.0D;
      double y = (this.y + other.y) / 2.0D;
      double z = (this.z + other.z) / 2.0D;
      return new Vector3dm(x, y, z);
   }

   @NotNull
   public Vector3dm multiply(int m) {
      this.x *= (double)m;
      this.y *= (double)m;
      this.z *= (double)m;
      return this;
   }

   @NotNull
   public Vector3dm multiply(double m) {
      this.x *= m;
      this.y *= m;
      this.z *= m;
      return this;
   }

   @NotNull
   public Vector3dm multiply(float m) {
      this.x *= (double)m;
      this.y *= (double)m;
      this.z *= (double)m;
      return this;
   }

   public double dot(@NotNull Vector3dm other) {
      return this.x * other.x + this.y * other.y + this.z * other.z;
   }

   @NotNull
   public Vector3dm crossProduct(@NotNull Vector3dm o) {
      double newX = this.y * o.z - o.y * this.z;
      double newY = this.z * o.x - o.z * this.x;
      double newZ = this.x * o.y - o.x * this.y;
      this.x = newX;
      this.y = newY;
      this.z = newZ;
      return this;
   }

   @NotNull
   public Vector3dm getCrossProduct(@NotNull Vector3dm o) {
      double x = this.y * o.z - o.y * this.z;
      double y = this.z * o.x - o.z * this.x;
      double z = this.x * o.y - o.x * this.y;
      return new Vector3dm(x, y, z);
   }

   @NotNull
   public Vector3dm normalize() {
      double length = this.length();
      this.x /= length;
      this.y /= length;
      this.z /= length;
      return this;
   }

   @NotNull
   public Vector3dm zero() {
      this.x = 0.0D;
      this.y = 0.0D;
      this.z = 0.0D;
      return this;
   }

   public boolean isZero() {
      return this.x == 0.0D && this.y == 0.0D && this.z == 0.0D;
   }

   @NotNull
   Vector3dm normalizeZeros() {
      if (this.x == -0.0D) {
         this.x = 0.0D;
      }

      if (this.y == -0.0D) {
         this.y = 0.0D;
      }

      if (this.z == -0.0D) {
         this.z = 0.0D;
      }

      return this;
   }

   public boolean isInAABB(@NotNull Vector3dm min, @NotNull Vector3dm max) {
      return this.x >= min.x && this.x <= max.x && this.y >= min.y && this.y <= max.y && this.z >= min.z && this.z <= max.z;
   }

   public boolean isInSphere(@NotNull Vector3dm origin, double radius) {
      return GrimMath.square(origin.x - this.x) + GrimMath.square(origin.y - this.y) + GrimMath.square(origin.z - this.z) <= GrimMath.square(radius);
   }

   public boolean isNormalized() {
      return Math.abs(this.lengthSquared() - 1.0D) < 1.0E-6D;
   }

   @NotNull
   public Vector3dm rotateAroundX(double angle) {
      double angleCos = Math.cos(angle);
      double angleSin = Math.sin(angle);
      double y = angleCos * this.getY() - angleSin * this.getZ();
      double z = angleSin * this.getY() + angleCos * this.getZ();
      return this.setY(y).setZ(z);
   }

   @NotNull
   public Vector3dm rotateAroundY(double angle) {
      double angleCos = Math.cos(angle);
      double angleSin = Math.sin(angle);
      double x = angleCos * this.getX() + angleSin * this.getZ();
      double z = -angleSin * this.getX() + angleCos * this.getZ();
      return this.setX(x).setZ(z);
   }

   @NotNull
   public Vector3dm rotateAroundZ(double angle) {
      double angleCos = Math.cos(angle);
      double angleSin = Math.sin(angle);
      double x = angleCos * this.getX() - angleSin * this.getY();
      double y = angleSin * this.getX() + angleCos * this.getY();
      return this.setX(x).setY(y);
   }

   @NotNull
   public Vector3dm setX(int x) {
      this.x = (double)x;
      return this;
   }

   @NotNull
   public Vector3dm setX(double x) {
      this.x = x;
      return this;
   }

   @NotNull
   public Vector3dm setX(float x) {
      this.x = (double)x;
      return this;
   }

   public int getBlockX() {
      return GrimMath.mojangFloor(this.x);
   }

   @NotNull
   public Vector3dm setY(int y) {
      this.y = (double)y;
      return this;
   }

   @NotNull
   public Vector3dm setY(double y) {
      this.y = y;
      return this;
   }

   @NotNull
   public Vector3dm setY(float y) {
      this.y = (double)y;
      return this;
   }

   public int getBlockY() {
      return GrimMath.mojangFloor(this.y);
   }

   @NotNull
   public Vector3dm setZ(int z) {
      this.z = (double)z;
      return this;
   }

   @NotNull
   public Vector3dm setZ(double z) {
      this.z = z;
      return this;
   }

   @NotNull
   public Vector3dm setZ(float z) {
      this.z = (double)z;
      return this;
   }

   public int getBlockZ() {
      return GrimMath.mojangFloor(this.z);
   }

   @Contract(
      value = "null -> false",
      pure = true
   )
   public boolean equals(Object obj) {
      boolean var10000;
      if (obj instanceof Vector3dm) {
         Vector3dm other = (Vector3dm)obj;
         if (Math.abs(this.x - other.x) < 1.0E-6D && Math.abs(this.y - other.y) < 1.0E-6D && Math.abs(this.z - other.z) < 1.0E-6D && this.getClass().equals(obj.getClass())) {
            var10000 = true;
            return var10000;
         }
      }

      var10000 = false;
      return var10000;
   }

   public int hashCode() {
      int hash = 7;
      int hash = 79 * hash + Long.hashCode(Double.doubleToLongBits(this.x));
      hash = 79 * hash + Long.hashCode(Double.doubleToLongBits(this.y));
      hash = 79 * hash + Long.hashCode(Double.doubleToLongBits(this.z));
      return hash;
   }

   @NotNull
   public Vector3dm clone() {
      try {
         return (Vector3dm)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new Error(var2);
      }
   }

   public String toString() {
      return this.x + "," + this.y + "," + this.z;
   }

   @NotNull
   public Vector3f toVector3f() {
      return new Vector3f((float)this.x, (float)this.y, (float)this.z);
   }

   @NotNull
   public Vector3d toVector3d() {
      return new Vector3d(this.x, this.y, this.z);
   }

   @Generated
   public double getX() {
      return this.x;
   }

   @Generated
   public double getY() {
      return this.y;
   }

   @Generated
   public double getZ() {
      return this.z;
   }
}
