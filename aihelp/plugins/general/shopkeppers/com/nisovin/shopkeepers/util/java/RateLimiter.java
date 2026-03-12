package com.nisovin.shopkeepers.util.java;

public class RateLimiter {
   private int threshold;
   private int remainingThreshold;

   public static RateLimiter withRandomInitialThreshold(int threshold) {
      return new RateLimiter(threshold, MathUtils.randomIntInRange(0, threshold) + 1);
   }

   public static RateLimiter withInitialOffset(int threshold, int offset) {
      return new RateLimiter(threshold, threshold + offset);
   }

   public static RateLimiter withRandomInitialOffset(int threshold) {
      return new RateLimiter(threshold, threshold + MathUtils.randomIntInRange(0, threshold));
   }

   public RateLimiter(int threshold) {
      this(threshold, 1);
   }

   public RateLimiter(int threshold, int initialThreshold) {
      Validate.isTrue(threshold >= 1, "threshold has to be positive");
      Validate.isTrue(initialThreshold >= 1, "initialThreshold has to be positive");
      this.threshold = threshold;
      this.remainingThreshold = initialThreshold;
   }

   public final int getThreshold() {
      return this.threshold;
   }

   public void setThreshold(int threshold) {
      Validate.isTrue(threshold >= 1, "threshold has to be positive");
      this.threshold = threshold;
   }

   public final int getRemainingThreshold() {
      return this.remainingThreshold;
   }

   public void setRemainingThreshold(int remainingThreshold) {
      Validate.isTrue(remainingThreshold >= 1, "remainingThreshold has to be positive");
      this.remainingThreshold = remainingThreshold;
   }

   public boolean request() {
      return this.handleRequest(1);
   }

   public boolean request(int requests) {
      Validate.isTrue(requests > 0, "requests has to be positive");
      return this.handleRequest(requests);
   }

   private boolean handleRequest(int requests) {
      assert requests > 0;

      this.remainingThreshold -= requests;
      if (this.remainingThreshold <= 0) {
         this.remainingThreshold = this.threshold;
         return true;
      } else {
         return false;
      }
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("RateLimiter [threshold=");
      builder.append(this.threshold);
      builder.append(", remainingThreshold=");
      builder.append(this.remainingThreshold);
      builder.append("]");
      return builder.toString();
   }
}
