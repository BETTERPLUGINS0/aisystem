package advancedplugins.pm2.cv.util.math;

import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import me.PM2.infinitevehicles.math.util.FastMath;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Matrix4 {
   public static final int M00 = 0;
   public static final int M01 = 4;
   public static final int M02 = 8;
   public static final int M03 = 12;
   public static final int M10 = 1;
   public static final int M11 = 5;
   public static final int M12 = 9;
   public static final int M13 = 13;
   public static final int M20 = 2;
   public static final int M21 = 6;
   public static final int M22 = 10;
   public static final int M23 = 14;
   public static final int M30 = 3;
   public static final int M31 = 7;
   public static final int M32 = 11;
   public static final int M33 = 15;
   protected final double[] elements = new double[]{1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D};

   protected static void multiply(double[] elementsA, double[] elementsB) {
      double var2 = var0[0] * var1[0] + var0[4] * var1[1] + var0[8] * var1[2] + var0[12] * var1[3];
      double var4 = var0[0] * var1[4] + var0[4] * var1[5] + var0[8] * var1[6] + var0[12] * var1[7];
      double var6 = var0[0] * var1[8] + var0[4] * var1[9] + var0[8] * var1[10] + var0[12] * var1[11];
      double var8 = var0[0] * var1[12] + var0[4] * var1[13] + var0[8] * var1[14] + var0[12] * var1[15];
      double var10 = var0[1] * var1[0] + var0[5] * var1[1] + var0[9] * var1[2] + var0[13] * var1[3];
      double var12 = var0[1] * var1[4] + var0[5] * var1[5] + var0[9] * var1[6] + var0[13] * var1[7];
      double var14 = var0[1] * var1[8] + var0[5] * var1[9] + var0[9] * var1[10] + var0[13] * var1[11];
      double var16 = var0[1] * var1[12] + var0[5] * var1[13] + var0[9] * var1[14] + var0[13] * var1[15];
      double var18 = var0[2] * var1[0] + var0[6] * var1[1] + var0[10] * var1[2] + var0[14] * var1[3];
      double var20 = var0[2] * var1[4] + var0[6] * var1[5] + var0[10] * var1[6] + var0[14] * var1[7];
      double var22 = var0[2] * var1[8] + var0[6] * var1[9] + var0[10] * var1[10] + var0[14] * var1[11];
      double var24 = var0[2] * var1[12] + var0[6] * var1[13] + var0[10] * var1[14] + var0[14] * var1[15];
      double var26 = var0[3] * var1[0] + var0[7] * var1[1] + var0[11] * var1[2] + var0[15] * var1[3];
      double var28 = var0[3] * var1[4] + var0[7] * var1[5] + var0[11] * var1[6] + var0[15] * var1[7];
      double var30 = var0[3] * var1[8] + var0[7] * var1[9] + var0[11] * var1[10] + var0[15] * var1[11];
      double var32 = var0[3] * var1[12] + var0[7] * var1[13] + var0[11] * var1[14] + var0[15] * var1[15];
      var0[0] = var2;
      var0[1] = var10;
      var0[2] = var18;
      var0[3] = var26;
      var0[4] = var4;
      var0[5] = var12;
      var0[6] = var20;
      var0[7] = var28;
      var0[8] = var6;
      var0[9] = var14;
      var0[10] = var22;
      var0[11] = var30;
      var0[12] = var8;
      var0[13] = var16;
      var0[14] = var24;
      var0[15] = var32;
   }

   public Matrix4() {
   }

   public Matrix4(Matrix4 copy) {
      System.arraycopy(var1.elements, 0, this.elements, 0, this.elements.length);
   }

   public Matrix4(double[] elements) {
      System.arraycopy(var1, 0, var1, 0, var1.length);
   }

   public double[] getElements() {
      return this.elements;
   }

   public Matrix4 set(Matrix4 matrix) {
      return this.set(var1.elements);
   }

   public Matrix4 set(double[] values) {
      System.arraycopy(var1, 0, this.elements, 0, this.elements.length);
      return this;
   }

   public Matrix4 set(int index, double value) {
      this.elements[var1] = var2;
      return this;
   }

   public Matrix4 set(float quaternionX, float quaternionY, float quaternionZ, float quaternionW) {
      return this.set(0.0F, 0.0F, 0.0F, var1, var2, var3, var4);
   }

   public Matrix4 set(float translationX, float translationY, float translationZ, float quaternionX, float quaternionY, float quaternionZ, float quaternionW) {
      float var8 = var4 * 2.0F;
      float var9 = var5 * 2.0F;
      float var10 = var6 * 2.0F;
      float var11 = var7 * var8;
      float var12 = var7 * var9;
      float var13 = var7 * var10;
      float var14 = var4 * var8;
      float var15 = var4 * var9;
      float var16 = var4 * var10;
      float var17 = var5 * var9;
      float var18 = var5 * var10;
      float var19 = var6 * var10;
      this.elements[0] = (double)(1.0F - (var17 + var19));
      this.elements[4] = (double)(var15 - var13);
      this.elements[8] = (double)(var16 + var12);
      this.elements[12] = (double)var1;
      this.elements[1] = (double)(var15 + var13);
      this.elements[5] = (double)(1.0F - (var14 + var19));
      this.elements[9] = (double)(var18 - var11);
      this.elements[13] = (double)var2;
      this.elements[2] = (double)(var16 - var12);
      this.elements[6] = (double)(var18 + var11);
      this.elements[10] = (double)(1.0F - (var14 + var17));
      this.elements[14] = (double)var3;
      this.elements[3] = 0.0D;
      this.elements[7] = 0.0D;
      this.elements[11] = 0.0D;
      this.elements[15] = 1.0D;
      return this;
   }

   public Matrix4 set(Vector3D xAxis, Vector3D yAxis, Vector3D zAxis, Vector3D translation) {
      this.elements[0] = var1.getX();
      this.elements[4] = var1.getY();
      this.elements[8] = var1.getZ();
      this.elements[1] = var2.getX();
      this.elements[5] = var2.getY();
      this.elements[9] = var2.getZ();
      this.elements[2] = var3.getX();
      this.elements[6] = var3.getY();
      this.elements[10] = var3.getZ();
      this.elements[12] = var4.getX();
      this.elements[13] = var4.getY();
      this.elements[14] = var4.getZ();
      this.elements[3] = 0.0D;
      this.elements[7] = 0.0D;
      this.elements[11] = 0.0D;
      this.elements[15] = 1.0D;
      return this;
   }

   public Matrix4 setTranslation(Vector3D vector) {
      this.elements[12] = var1.getX();
      this.elements[13] = var1.getY();
      this.elements[14] = var1.getZ();
      return this;
   }

   public Matrix4 translate(Vector3D vector) {
      double[] var10000 = this.elements;
      var10000[12] += var1.getX();
      var10000 = this.elements;
      var10000[13] += var1.getY();
      var10000 = this.elements;
      var10000[14] += var1.getZ();
      return this;
   }

   public Matrix4 multiply(Matrix4 matrix) {
      multiply(this.elements, var1.elements);
      return this;
   }

   public Matrix4 multiplyLeft(Matrix4 matrix) {
      Matrix4 var2 = new Matrix4(var1);
      multiply(var2.elements, this.elements);
      return this.set(var2);
   }

   public Matrix4 multiplyScalar(double scalar) {
      double[] var10000 = this.elements;
      var10000[0] *= var1;
      var10000 = this.elements;
      var10000[4] *= var1;
      var10000 = this.elements;
      var10000[8] *= var1;
      var10000 = this.elements;
      var10000[12] *= var1;
      var10000 = this.elements;
      var10000[1] *= var1;
      var10000 = this.elements;
      var10000[5] *= var1;
      var10000 = this.elements;
      var10000[9] *= var1;
      var10000 = this.elements;
      var10000[13] *= var1;
      var10000 = this.elements;
      var10000[2] *= var1;
      var10000 = this.elements;
      var10000[6] *= var1;
      var10000 = this.elements;
      var10000[10] *= var1;
      var10000 = this.elements;
      var10000[14] *= var1;
      var10000 = this.elements;
      var10000[3] *= var1;
      var10000 = this.elements;
      var10000[7] *= var1;
      var10000 = this.elements;
      var10000[11] *= var1;
      var10000 = this.elements;
      var10000[15] *= var1;
      return this;
   }

   public Matrix4 scale(Vector3D scale) {
      double[] var10000 = this.elements;
      var10000[0] *= var1.getX();
      var10000 = this.elements;
      var10000[5] *= var1.getY();
      var10000 = this.elements;
      var10000[10] *= var1.getZ();
      return this;
   }

   public Matrix4 makeScaleMatrix(double x, double y, double z) {
      this.set(new double[]{var1, 0.0D, 0.0D, 0.0D, 0.0D, var3, 0.0D, 0.0D, 0.0D, 0.0D, var5, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D});
      return this;
   }

   public Matrix4 makeRotationMatrix(Euler euler) {
      double var2 = FastMath.toRadians(var1.x);
      double var4 = FastMath.toRadians(var1.y);
      double var6 = FastMath.toRadians(var1.z);
      double var8 = FastMath.cos(var2);
      double var10 = FastMath.sin(var2);
      double var12 = FastMath.cos(var4);
      double var14 = FastMath.sin(var4);
      double var16 = FastMath.cos(var6);
      double var18 = FastMath.sin(var6);
      double var20;
      double var22;
      double var24;
      double var26;
      switch(var1.order) {
      case XYZ:
         var20 = var8 * var16;
         var22 = var8 * var18;
         var24 = var10 * var16;
         var26 = var10 * var18;
         this.elements[0] = var12 * var16;
         this.elements[4] = -var12 * var18;
         this.elements[8] = var14;
         this.elements[1] = var22 + var24 * var14;
         this.elements[5] = var20 - var26 * var14;
         this.elements[9] = -var10 * var12;
         this.elements[2] = var26 - var20 * var14;
         this.elements[6] = var24 + var22 * var14;
         this.elements[10] = var8 * var12;
         break;
      case YXZ:
         var20 = var12 * var16;
         var22 = var12 * var18;
         var24 = var14 * var16;
         var26 = var14 * var18;
         this.elements[0] = var20 + var26 * var10;
         this.elements[4] = var24 * var10 - var22;
         this.elements[8] = var8 * var14;
         this.elements[1] = var8 * var18;
         this.elements[5] = var8 * var16;
         this.elements[9] = -var10;
         this.elements[2] = var22 * var10 - var24;
         this.elements[6] = var26 + var20 * var10;
         this.elements[10] = var8 * var12;
         break;
      case ZXY:
         var20 = var12 * var16;
         var22 = var12 * var18;
         var24 = var14 * var16;
         var26 = var14 * var18;
         this.elements[0] = var20 - var26 * var10;
         this.elements[4] = -var8 * var18;
         this.elements[8] = var24 + var22 * var10;
         this.elements[1] = var22 + var24 * var10;
         this.elements[5] = var8 * var16;
         this.elements[9] = var26 - var20 * var10;
         this.elements[2] = -var8 * var14;
         this.elements[6] = var10;
         this.elements[10] = var8 * var12;
         break;
      case ZYX:
         var20 = var8 * var16;
         var22 = var8 * var18;
         var24 = var10 * var16;
         var26 = var10 * var18;
         this.elements[0] = var12 * var16;
         this.elements[4] = var24 * var14 - var22;
         this.elements[8] = var20 * var14 + var26;
         this.elements[1] = var12 * var18;
         this.elements[5] = var26 * var14 + var20;
         this.elements[9] = var22 * var14 - var24;
         this.elements[2] = -var14;
         this.elements[6] = var10 * var12;
         this.elements[10] = var8 * var12;
         break;
      case YZX:
         var20 = var8 * var12;
         var22 = var8 * var14;
         var24 = var10 * var12;
         var26 = var10 * var14;
         this.elements[0] = var12 * var16;
         this.elements[4] = var26 - var20 * var18;
         this.elements[8] = var24 * var18 + var22;
         this.elements[1] = var18;
         this.elements[5] = var8 * var16;
         this.elements[9] = -var10 * var16;
         this.elements[2] = -var14 * var16;
         this.elements[6] = var22 * var18 + var24;
         this.elements[10] = var20 - var26 * var18;
         break;
      case XZY:
         var20 = var8 * var12;
         var22 = var8 * var14;
         var24 = var10 * var12;
         var26 = var10 * var14;
         this.elements[0] = var12 * var16;
         this.elements[4] = -var18;
         this.elements[8] = var14 * var16;
         this.elements[1] = var20 * var18 + var26;
         this.elements[5] = var8 * var16;
         this.elements[9] = var22 * var18 - var24;
         this.elements[2] = var24 * var18 - var22;
         this.elements[6] = var10 * var16;
         this.elements[10] = var26 * var18 + var20;
         break;
      default:
         throw new IllegalStateException("not implemented");
      }

      this.elements[3] = 0.0D;
      this.elements[7] = 0.0D;
      this.elements[11] = 0.0D;
      this.elements[12] = 0.0D;
      this.elements[13] = 0.0D;
      this.elements[14] = 0.0D;
      this.elements[15] = 1.0D;
      return this;
   }

   public Matrix4 makeRotationMatrix(Quaternion quaternion) {
      this.compose(Vector3D.ZERO, var1, new Vector3D(1.0D, 1.0D, 1.0D));
      return this;
   }

   public Matrix4 compose(Vector3D position, Quaternion quaternion, Vector3D scale) {
      double var4 = var2.x;
      double var6 = var2.y;
      double var8 = var2.z;
      double var10 = var2.w;
      double var12 = var4 + var4;
      double var14 = var6 + var6;
      double var16 = var8 + var8;
      double var18 = var4 * var12;
      double var20 = var4 * var14;
      double var22 = var4 * var16;
      double var24 = var6 * var14;
      double var26 = var6 * var16;
      double var28 = var8 * var16;
      double var30 = var10 * var12;
      double var32 = var10 * var14;
      double var34 = var10 * var16;
      double var36 = var3.getX();
      double var38 = var3.getY();
      double var40 = var3.getZ();
      this.elements[0] = (1.0D - (var24 + var28)) * var36;
      this.elements[1] = (var20 + var34) * var36;
      this.elements[2] = (var22 - var32) * var36;
      this.elements[3] = 0.0D;
      this.elements[4] = (var20 - var34) * var38;
      this.elements[5] = (1.0D - (var18 + var28)) * var38;
      this.elements[6] = (var26 + var30) * var38;
      this.elements[7] = 0.0D;
      this.elements[8] = (var22 + var32) * var40;
      this.elements[9] = (var26 - var30) * var40;
      this.elements[10] = (1.0D - (var18 + var24)) * var40;
      this.elements[11] = 0.0D;
      this.elements[12] = var1.getX();
      this.elements[13] = var1.getY();
      this.elements[14] = var1.getZ();
      this.elements[15] = 1.0D;
      return this;
   }

   public Matrix4 copy() {
      return new Matrix4(this);
   }

   public boolean equals(Object o) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Matrix4)) {
         return false;
      } else {
         Matrix4 var2 = (Matrix4)var1;
         return (new EqualsBuilder()).append(this.elements, var2.elements).isEquals();
      }
   }

   public int hashCode() {
      return (new HashCodeBuilder(17, 37)).append(this.elements).toHashCode();
   }
}
