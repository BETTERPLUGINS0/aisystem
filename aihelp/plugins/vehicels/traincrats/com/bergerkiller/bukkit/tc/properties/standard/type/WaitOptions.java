package com.bergerkiller.bukkit.tc.properties.standard.type;

public final class WaitOptions {
   public static final WaitOptions DEFAULT = new WaitOptions(0.0D, 0.0D, 0.0D, 0.0D, true);
   private final double distance;
   private final double delay;
   private final double deceleration;
   private final double acceleration;
   private final boolean predict;

   private WaitOptions(double distance, double delay, double acceleration, double deceleration, boolean predict) {
      this.distance = distance;
      this.delay = delay;
      this.acceleration = acceleration;
      this.deceleration = deceleration;
      this.predict = predict;
   }

   public double distance() {
      return this.distance;
   }

   public double delay() {
      return this.delay;
   }

   public double deceleration() {
      return this.deceleration;
   }

   public double acceleration() {
      return this.acceleration;
   }

   public boolean predict() {
      return this.predict;
   }

   public int hashCode() {
      return Double.hashCode(this.distance);
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof WaitOptions)) {
         return false;
      } else {
         WaitOptions other = (WaitOptions)o;
         return this.distance == other.distance && this.delay == other.delay && this.acceleration == other.acceleration && this.deceleration == other.deceleration;
      }
   }

   public static WaitOptions create(double distance) {
      return create(distance, 0.0D, 0.0D, 0.0D, true);
   }

   public static WaitOptions create(double distance, double delay, double acceleration, double deceleration) {
      return new WaitOptions(Math.max(0.0D, distance), Math.max(0.0D, delay), Math.max(0.0D, acceleration), Math.max(0.0D, deceleration), true);
   }

   public static WaitOptions create(double distance, double delay, double acceleration, double deceleration, boolean predict) {
      return new WaitOptions(Math.max(0.0D, distance), Math.max(0.0D, delay), Math.max(0.0D, acceleration), Math.max(0.0D, deceleration), predict);
   }
}
