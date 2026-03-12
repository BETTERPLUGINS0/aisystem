package ac.grim.grimac.utils.worldborder;

import ac.grim.grimac.utils.math.GrimMath;

public class RealTimeMovingBorderExtent implements BorderExtent {
   private final double from;
   private final double to;
   private final long startTime;
   private final long endTime;

   public RealTimeMovingBorderExtent(double from, double to, long durationMs) {
      this.from = from;
      this.to = to;
      this.startTime = System.currentTimeMillis();
      this.endTime = this.startTime + durationMs;
   }

   public double size() {
      long now = System.currentTimeMillis();
      if (now >= this.endTime) {
         return this.to;
      } else {
         double progress = (double)(now - this.startTime) / (double)(this.endTime - this.startTime);
         return progress < 1.0D ? GrimMath.lerp(progress, this.from, this.to) : this.to;
      }
   }

   public double getMinX(double centerX, double absoluteMaxSize) {
      return GrimMath.clamp(centerX - this.size() / 2.0D, -absoluteMaxSize, absoluteMaxSize);
   }

   public double getMaxX(double centerX, double absoluteMaxSize) {
      return GrimMath.clamp(centerX + this.size() / 2.0D, -absoluteMaxSize, absoluteMaxSize);
   }

   public double getMinZ(double centerZ, double absoluteMaxSize) {
      return GrimMath.clamp(centerZ - this.size() / 2.0D, -absoluteMaxSize, absoluteMaxSize);
   }

   public double getMaxZ(double centerZ, double absoluteMaxSize) {
      return GrimMath.clamp(centerZ + this.size() / 2.0D, -absoluteMaxSize, absoluteMaxSize);
   }

   public BorderExtent tick() {
      return this.update();
   }

   public BorderExtent update() {
      return (BorderExtent)(System.currentTimeMillis() >= this.endTime ? new StaticBorderExtent(this.to) : this);
   }
}
