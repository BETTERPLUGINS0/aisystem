package advancedplugins.pm2.cv.util.math;

import advancedplugins.pm2.cv.enums.EnumAxisOrder;
import java.util.Objects;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import me.PM2.infinitevehicles.math.util.FastMath;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

public class Euler {
   protected double x;
   protected double y;
   protected double z;
   protected EnumAxisOrder order;

   public Euler(EnumAxisOrder order) {
      this.order = (EnumAxisOrder)Objects.requireNonNull(var1, "order cannot be null");
   }

   public Euler(Euler copy) {
      this.setX(var1.x);
      this.setY(var1.y);
      this.setZ(var1.z);
      this.order = var1.order;
   }

   public Euler(double x, double y, double z, EnumAxisOrder order) {
      this.setX(var1);
      this.setY(var3);
      this.setZ(var5);
      this.order = (EnumAxisOrder)Objects.requireNonNull(var7, "order cannot be null");
   }

   public Euler(@NotNull Vector3D components, @NotNull EnumAxisOrder order) {
      this(var1.getX(), var1.getY(), var1.getZ(), var2);
   }

   public Euler(EulerAngle bukkit, EnumAxisOrder order) {
      this(Math.toDegrees(var1.getX()), Math.toDegrees(var1.getY()), Math.toDegrees(var1.getZ()), var2);
   }

   public double getX() {
      return this.x;
   }

   public Euler setX(double x) {
      this.x = var1;
      return this;
   }

   public double getY() {
      return this.y;
   }

   public Euler setY(double y) {
      this.y = var1;
      return this;
   }

   public double getZ() {
      return this.z;
   }

   public Euler setZ(double z) {
      this.z = var1;
      return this;
   }

   public boolean isZero() {
      return Double.compare(this.x, 0.0D) == 0 && Double.compare(this.y, 0.0D) == 0 && Double.compare(this.z, 0.0D) == 0;
   }

   public EnumAxisOrder getOrder() {
      return this.order;
   }

   public Euler setOrder(EnumAxisOrder order) {
      this.order = (EnumAxisOrder)Objects.requireNonNull(var1, "order cannot be null");
      return this;
   }

   public Euler convertToOrder(EnumAxisOrder order) {
      if (var1 == this.order) {
         return this;
      } else {
         Euler var2 = (new Euler(var1)).setFromRotationMatrix((new Matrix4()).makeRotationMatrix((new Quaternion()).setFromEuler(this).normalize()));
         return this.set(var2.getX(), var2.getY(), var2.getZ(), var2.getOrder());
      }
   }

   public Euler set(double x, double y, double z, EnumAxisOrder order) {
      this.setX(var1);
      this.setY(var3);
      this.setZ(var5);
      this.setOrder(var7);
      return this;
   }

   public Euler add(double x, double y, double z) {
      this.setX(this.x + var1);
      this.setY(this.y + var3);
      this.setZ(this.z + var5);
      return this;
   }

   public Euler add(Euler euler) {
      this.add(var1.getX(), var1.getY(), var1.getZ());
      return this;
   }

   public Euler subtract(double x, double y, double z) {
      this.setX(this.x - var1);
      this.setY(this.y - var3);
      this.setZ(this.z - var5);
      return this;
   }

   public Euler subtract(Euler euler) {
      this.subtract(var1.getX(), var1.getY(), var1.getZ());
      return this;
   }

   public Euler setFromRotationMatrix(Matrix4 matrix4, EnumAxisOrder order) {
      this.setOrder(var2);
      double var3 = var1.elements[0];
      double var5 = var1.elements[4];
      double var7 = var1.elements[8];
      double var9 = var1.elements[1];
      double var11 = var1.elements[5];
      double var13 = var1.elements[9];
      double var15 = var1.elements[2];
      double var17 = var1.elements[6];
      double var19 = var1.elements[10];
      switch(var2) {
      case XYZ:
         this.y = FastMath.asin(ClampUtil.clamp(var7, -1.0D, 1.0D));
         if (FastMath.abs(var7) < 0.9999999D) {
            this.x = FastMath.atan2(-var13, var19);
            this.z = FastMath.atan2(-var5, var3);
         } else {
            this.x = FastMath.atan2(var17, var11);
            this.z = 0.0D;
         }
         break;
      case YXZ:
         this.x = FastMath.asin(-ClampUtil.clamp(var13, -1.0D, 1.0D));
         if (FastMath.abs(var13) < 0.9999999D) {
            this.y = FastMath.atan2(var7, var19);
            this.z = FastMath.atan2(var9, var11);
         } else {
            this.y = FastMath.atan2(-var15, var3);
            this.z = 0.0D;
         }
         break;
      case ZXY:
         this.x = FastMath.asin(ClampUtil.clamp(var17, -1.0D, 1.0D));
         if (FastMath.abs(var17) < 0.9999999D) {
            this.y = FastMath.atan2(-var15, var19);
            this.z = FastMath.atan2(-var5, var11);
         } else {
            this.y = 0.0D;
            this.z = FastMath.atan2(var9, var3);
         }
         break;
      case ZYX:
         this.y = FastMath.asin(-ClampUtil.clamp(var15, -1.0D, 1.0D));
         if (FastMath.abs(var15) < 0.9999999D) {
            this.x = FastMath.atan2(var17, var19);
            this.z = FastMath.atan2(var9, var3);
         } else {
            this.x = 0.0D;
            this.z = FastMath.atan2(-var5, var11);
         }
         break;
      case YZX:
         this.z = FastMath.asin(ClampUtil.clamp(var9, -1.0D, 1.0D));
         if (FastMath.abs(var9) < 0.9999999D) {
            this.x = FastMath.atan2(-var13, var11);
            this.y = FastMath.atan2(-var15, var3);
         } else {
            this.x = 0.0D;
            this.y = FastMath.atan2(var7, var19);
         }
         break;
      case XZY:
         this.z = FastMath.asin(-ClampUtil.clamp(var5, -1.0D, 1.0D));
         if (FastMath.abs(var5) < 0.9999999D) {
            this.x = FastMath.atan2(var17, var11);
            this.y = FastMath.atan2(var7, var3);
         } else {
            this.x = FastMath.atan2(-var13, var19);
            this.y = 0.0D;
         }
         break;
      default:
         throw new IllegalStateException("not implemented");
      }

      this.x = FastMath.toDegrees(this.x);
      this.y = FastMath.toDegrees(this.y);
      this.z = FastMath.toDegrees(this.z);
      return this;
   }

   public Euler setFromRotationMatrix(Matrix4 matrix4) {
      this.setFromRotationMatrix(var1, this.order);
      return this;
   }

   public Euler setFromQuaternion(Quaternion quaternion) {
      Matrix4 var2 = new Matrix4();
      var2.makeRotationMatrix(var1);
      this.setFromRotationMatrix(var2);
      return this;
   }

   public Euler copy() {
      return new Euler(this);
   }

   public Vector3D toVector() {
      return new Vector3D(this.x, this.y, this.z);
   }

   public boolean equals(Object o) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Euler)) {
         return false;
      } else {
         Euler var2 = (Euler)var1;
         return (new EqualsBuilder()).append(this.x, var2.x).append(this.y, var2.y).append(this.z, var2.z).isEquals();
      }
   }

   public int hashCode() {
      return (new HashCodeBuilder(17, 37)).append(this.x).append(this.y).append(this.z).toHashCode();
   }

   public String toString() {
      double var10000 = this.getX();
      return "Euler(x=" + var10000 + ", y=" + this.getY() + ", z=" + this.getZ() + ", order=" + String.valueOf(this.getOrder()) + ")";
   }
}
