package ac.grim.grimac.utils.worldborder;

import ac.grim.grimac.utils.math.GrimMath;

public class TickBasedMovingBorderExtent implements BorderExtent {
   private final double from;
   private final double to;
   private final long lerpDuration;
   private long lerpProgress;
   private double size;
   private double previousSize;

   public TickBasedMovingBorderExtent(double from, double to, long durationTicks) {
      this.from = from;
      this.to = to;
      this.lerpDuration = durationTicks;
      this.lerpProgress = durationTicks;
      this.size = this.calculateSize();
      this.previousSize = this.size;
   }

   private double calculateSize() {
      if (this.lerpDuration <= 0L) {
         return this.to;
      } else {
         double progress = (double)(this.lerpDuration - this.lerpProgress) / (double)this.lerpDuration;
         return progress < 1.0D ? GrimMath.lerp(progress, this.from, this.to) : this.to;
      }
   }

   public double size() {
      return this.size;
   }

   public double getMinX(double centerX, double absoluteMaxSize) {
      return GrimMath.clamp(centerX - this.previousSize / 2.0D, -absoluteMaxSize, absoluteMaxSize);
   }

   public double getMaxX(double centerX, double absoluteMaxSize) {
      return GrimMath.clamp(centerX + this.previousSize / 2.0D, -absoluteMaxSize, absoluteMaxSize);
   }

   public double getMinZ(double centerZ, double absoluteMaxSize) {
      return GrimMath.clamp(centerZ - this.previousSize / 2.0D, -absoluteMaxSize, absoluteMaxSize);
   }

   public double getMaxZ(double centerZ, double absoluteMaxSize) {
      return GrimMath.clamp(centerZ + this.previousSize / 2.0D, -absoluteMaxSize, absoluteMaxSize);
   }

   public BorderExtent tick() {
      if (this.lerpProgress > 0L) {
         --this.lerpProgress;
         this.previousSize = this.size;
         this.size = this.calculateSize();
      }

      return this.update();
   }

   public BorderExtent update() {
      return (BorderExtent)(this.lerpProgress <= 0L ? new StaticBorderExtent(this.to) : this);
   }
}
