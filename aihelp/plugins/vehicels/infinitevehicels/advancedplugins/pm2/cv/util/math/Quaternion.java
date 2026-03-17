package advancedplugins.pm2.cv.util.math;

import me.PM2.infinitevehicles.math.util.FastMath;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Quaternion {
   protected double x;
   protected double y;
   protected double z;
   protected double w;

   public Quaternion() {
      this.x = 0.0D;
      this.y = 0.0D;
      this.z = 0.0D;
      this.w = 0.0D;
   }

   public Quaternion(Quaternion copy) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.w = var1.w;
   }

   public Quaternion(double x, double y, double z, double w) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
      this.w = var7;
   }

   public double getX() {
      return this.x;
   }

   public void setX(double x) {
      this.x = var1;
   }

   public double getY() {
      return this.y;
   }

   public void setY(double y) {
      this.y = var1;
   }

   public double getZ() {
      return this.z;
   }

   public void setZ(double z) {
      this.z = var1;
   }

   public double getW() {
      return this.w;
   }

   public void setW(double w) {
      this.w = var1;
   }

   public Quaternion setFromEuler(Euler euler) {
      double var2 = FastMath.toRadians(var1.x);
      double var4 = FastMath.toRadians(var1.y);
      double var6 = FastMath.toRadians(var1.z);
      double var8 = FastMath.cos(var2 / 2.0D);
      double var10 = FastMath.cos(var4 / 2.0D);
      double var12 = FastMath.cos(var6 / 2.0D);
      double var14 = FastMath.sin(var2 / 2.0D);
      double var16 = FastMath.sin(var4 / 2.0D);
      double var18 = FastMath.sin(var6 / 2.0D);
      switch(var1.order) {
      case XYZ:
         this.x = var14 * var10 * var12 + var8 * var16 * var18;
         this.y = var8 * var16 * var12 - var14 * var10 * var18;
         this.z = var8 * var10 * var18 + var14 * var16 * var12;
         this.w = var8 * var10 * var12 - var14 * var16 * var18;
         break;
      case YXZ:
         this.x = var14 * var10 * var12 + var8 * var16 * var18;
         this.y = var8 * var16 * var12 - var14 * var10 * var18;
         this.z = var8 * var10 * var18 - var14 * var16 * var12;
         this.w = var8 * var10 * var12 + var14 * var16 * var18;
         break;
      case ZXY:
         this.x = var14 * var10 * var12 - var8 * var16 * var18;
         this.y = var8 * var16 * var12 + var14 * var10 * var18;
         this.z = var8 * var10 * var18 + var14 * var16 * var12;
         this.w = var8 * var10 * var12 - var14 * var16 * var18;
         break;
      case ZYX:
         this.x = var14 * var10 * var12 - var8 * var16 * var18;
         this.y = var8 * var16 * var12 + var14 * var10 * var18;
         this.z = var8 * var10 * var18 - var14 * var16 * var12;
         this.w = var8 * var10 * var12 + var14 * var16 * var18;
         break;
      case YZX:
         this.x = var14 * var10 * var12 + var8 * var16 * var18;
         this.y = var8 * var16 * var12 + var14 * var10 * var18;
         this.z = var8 * var10 * var18 - var14 * var16 * var12;
         this.w = var8 * var10 * var12 - var14 * var16 * var18;
         break;
      case XZY:
         this.x = var14 * var10 * var12 - var8 * var16 * var18;
         this.y = var8 * var16 * var12 - var14 * var10 * var18;
         this.z = var8 * var10 * var18 + var14 * var16 * var12;
         this.w = var8 * var10 * var12 + var14 * var16 * var18;
         break;
      default:
         throw new IllegalStateException("not implemented");
      }

      return this;
   }

   public double length() {
      return FastMath.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
   }

   public Quaternion normalize() {
      double var1 = this.length();
      if (Double.compare(var1, 0.0D) == 0) {
         this.x = 0.0D;
         this.y = 0.0D;
         this.z = 0.0D;
         this.w = 1.0D;
      } else {
         var1 = 1.0D / var1;
         this.x *= var1;
         this.y *= var1;
         this.z *= var1;
         this.w *= var1;
      }

      return this;
   }

   public Quaternion copy() {
      return new Quaternion(this);
   }

   public boolean equals(Object o) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Quaternion)) {
         return false;
      } else {
         Quaternion var2 = (Quaternion)var1;
         return (new EqualsBuilder()).append(this.x, var2.x).append(this.y, var2.y).append(this.z, var2.z).append(this.w, var2.w).isEquals();
      }
   }

   public int hashCode() {
      return (new HashCodeBuilder(17, 37)).append(this.x).append(this.y).append(this.z).append(this.w).toHashCode();
   }
}
