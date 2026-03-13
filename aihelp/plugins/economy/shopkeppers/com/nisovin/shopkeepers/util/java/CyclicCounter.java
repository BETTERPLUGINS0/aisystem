package com.nisovin.shopkeepers.util.java;

public class CyclicCounter {
   private final int lowerBound;
   private final int upperBound;
   private int value;

   public CyclicCounter(int upperBound) {
      this(0, upperBound);
   }

   public CyclicCounter(int lowerBound, int upperBound) {
      Validate.isTrue(upperBound > lowerBound, "lowerBound <= upperBound");
      this.lowerBound = lowerBound;
      this.upperBound = upperBound;
      this.value = lowerBound;
   }

   public int getLowerBound() {
      return this.lowerBound;
   }

   public int getUpperBound() {
      return this.upperBound;
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int value) {
      Validate.isTrue(value >= this.lowerBound && value < this.upperBound, "value is out of bounds");
      this.value = value;
   }

   public void reset() {
      this.value = this.lowerBound;
   }

   public int getAndIncrement() {
      int currentValue = this.value;
      int nextValue = currentValue + 1;

      assert nextValue <= this.upperBound;

      if (nextValue == this.upperBound) {
         this.value = this.lowerBound;
      } else {
         this.value = nextValue;
      }

      return currentValue;
   }

   public int incrementAndGet() {
      this.getAndIncrement();
      return this.getValue();
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("CyclicCounter [lowerBound=");
      builder.append(this.lowerBound);
      builder.append(", upperBound=");
      builder.append(this.upperBound);
      builder.append(", value=");
      builder.append(this.value);
      builder.append("]");
      return builder.toString();
   }
}
