package ac.grim.grimac.utils.worldborder;

import ac.grim.grimac.utils.math.GrimMath;

public record StaticBorderExtent(double size) implements BorderExtent {
   public StaticBorderExtent(double size) {
      this.size = size;
   }

   public double getMinX(double centerX, double absoluteMaxSize) {
      return GrimMath.clamp(centerX - this.size / 2.0D, -absoluteMaxSize, absoluteMaxSize);
   }

   public double getMaxX(double centerX, double absoluteMaxSize) {
      return GrimMath.clamp(centerX + this.size / 2.0D, -absoluteMaxSize, absoluteMaxSize);
   }

   public double getMinZ(double centerZ, double absoluteMaxSize) {
      return GrimMath.clamp(centerZ - this.size / 2.0D, -absoluteMaxSize, absoluteMaxSize);
   }

   public double getMaxZ(double centerZ, double absoluteMaxSize) {
      return GrimMath.clamp(centerZ + this.size / 2.0D, -absoluteMaxSize, absoluteMaxSize);
   }

   public BorderExtent tick() {
      return this;
   }

   public BorderExtent update() {
      return this;
   }

   public double size() {
      return this.size;
   }
}
