package com.volmit.iris.util.math;

import com.volmit.iris.util.data.DoubleArrayUtils;

public class Average {
   protected final double[] values;
   protected int cursor;
   private double average;
   private double lastSum;
   private boolean dirty;
   private boolean brandNew;

   public Average(int size) {
      this.values = new double[var1];
      DoubleArrayUtils.fill(this.values, 0.0D);
      this.brandNew = true;
      this.average = 0.0D;
      this.cursor = 0;
      this.lastSum = 0.0D;
      this.dirty = false;
   }

   public void put(double i) {
      this.dirty = true;
      if (this.brandNew) {
         DoubleArrayUtils.fill(this.values, var1);
         this.lastSum = (double)this.size() * var1;
         this.brandNew = false;
      } else {
         double var3 = this.values[this.cursor];
         this.lastSum = this.lastSum - var3 + var1;
         this.values[this.cursor] = var1;
         this.cursor = this.cursor + 1 < this.size() ? this.cursor + 1 : 0;
      }
   }

   public double getAverage() {
      if (this.dirty) {
         this.calculateAverage();
         return this.getAverage();
      } else {
         return this.average;
      }
   }

   private void calculateAverage() {
      this.average = this.lastSum / (double)this.size();
      this.dirty = false;
   }

   public int size() {
      return this.values.length;
   }

   public boolean isDirty() {
      return this.dirty;
   }
}
