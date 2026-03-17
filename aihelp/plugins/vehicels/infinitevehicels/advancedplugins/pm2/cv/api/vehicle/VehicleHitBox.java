package advancedplugins.pm2.cv.api.vehicle;

import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleHitBoxConfiguration;
import com.google.common.base.Preconditions;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import me.PM2.infinitevehicles.math.util.FastMath;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

public class VehicleHitBox {
   private final double width;
   private final double height;
   private final double depth;
   private double originX;
   private double originY;
   private double originZ;
   private double minX;
   private double minY;
   private double minZ;
   private double maxX;
   private double maxY;
   private double maxZ;

   public static VehicleHitBox of(@NotNull VehicleHitBoxConfiguration var0) {
      return new VehicleHitBox(Math.max(var0.getWidth(), 0.5D), Math.max(var0.getHeight(), 0.5D), Math.max(var0.getDepth(), 0.5D));
   }

   public VehicleHitBox(double var1, double var3, double var5) {
      Preconditions.checkArgument(var1 >= 0.5D, "width must be >= 0.5");
      Preconditions.checkArgument(var3 >= 0.5D, "height must be >= 0.5");
      Preconditions.checkArgument(var5 >= 0.5D, "depth must be >= 0.5");
      this.width = var1;
      this.height = var3;
      this.depth = var5;
      this.recalculate();
   }

   public int getBlockMinX() {
      return (int)FastMath.floor(this.minX);
   }

   public int getBlockMinY() {
      return (int)FastMath.floor(this.minY);
   }

   public int getBlockMinZ() {
      return (int)FastMath.floor(this.minZ);
   }

   public int getBlockMaxX() {
      return (int)FastMath.floor(this.maxX);
   }

   public int getBlockMaxY() {
      return (int)FastMath.floor(this.maxY);
   }

   public int getBlockMaxZ() {
      return (int)FastMath.floor(this.maxZ);
   }

   public void setOrigin(double var1, double var3, double var5) {
      if (Double.compare(var1, this.originX) != 0 || Double.compare(var3, this.originY) != 0 || Double.compare(var5, this.originZ) != 0) {
         this.originX = var1;
         this.originY = var3;
         this.originZ = var5;
         this.recalculate();
      }
   }

   public VehicleHitBox future(double var1, double var3, double var5) {
      VehicleHitBox var7 = this.copy();
      var7.setOrigin(var1, var3, var5);
      return var7;
   }

   public void recalculate() {
      double var1 = this.width / 2.0D;
      double var3 = this.depth / 2.0D;
      double var5 = this.originX - var1;
      double var7 = this.originZ - var3;
      double var9 = this.originX + var1;
      double var11 = this.originZ + var3;
      this.minX = Math.min(var5, var9);
      this.minZ = Math.min(var7, var11);
      this.maxX = Math.max(var5, var9);
      this.maxZ = Math.max(var7, var11);
      this.minY = this.originY;
      this.maxY = this.originY + this.height;
   }

   public Vector3D getCorner000() {
      return new Vector3D(this.minX, this.minY, this.minZ);
   }

   public Vector3D getCorner001() {
      return new Vector3D(this.minX, this.minY, this.maxZ);
   }

   public Vector3D getCorner010() {
      return new Vector3D(this.minX, this.maxY, this.minZ);
   }

   public Vector3D getCorner011() {
      return new Vector3D(this.minX, this.maxY, this.maxZ);
   }

   public Vector3D getCorner100() {
      return new Vector3D(this.maxX, this.minY, this.minZ);
   }

   public Vector3D getCorner101() {
      return new Vector3D(this.maxX, this.minY, this.maxZ);
   }

   public Vector3D getCorner110() {
      return new Vector3D(this.maxX, this.maxY, this.minZ);
   }

   public Vector3D getCorner111() {
      return new Vector3D(this.maxX, this.maxY, this.maxZ);
   }

   public Vector3D[] getCorners() {
      return new Vector3D[]{this.getCorner000(), this.getCorner001(), this.getCorner010(), this.getCorner011(), this.getCorner100(), this.getCorner101(), this.getCorner110(), this.getCorner111()};
   }

   public BoundingBox toBoundingBox() {
      return new BoundingBox(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
   }

   public VehicleHitBox copy() {
      VehicleHitBox var1 = new VehicleHitBox(this.width, this.height, this.depth);
      var1.originX = this.originX;
      var1.originY = this.originY;
      var1.originZ = this.originZ;
      var1.minX = this.minX;
      var1.minY = this.minY;
      var1.minZ = this.minZ;
      var1.maxX = this.maxX;
      var1.maxY = this.maxY;
      var1.maxZ = this.maxZ;
      return var1;
   }

   public double getWidth() {
      return this.width;
   }

   public double getHeight() {
      return this.height;
   }

   public double getDepth() {
      return this.depth;
   }

   public double getOriginX() {
      return this.originX;
   }

   public double getOriginY() {
      return this.originY;
   }

   public double getOriginZ() {
      return this.originZ;
   }

   public double getMinX() {
      return this.minX;
   }

   public double getMinY() {
      return this.minY;
   }

   public double getMinZ() {
      return this.minZ;
   }

   public double getMaxX() {
      return this.maxX;
   }

   public double getMaxY() {
      return this.maxY;
   }

   public double getMaxZ() {
      return this.maxZ;
   }
}
