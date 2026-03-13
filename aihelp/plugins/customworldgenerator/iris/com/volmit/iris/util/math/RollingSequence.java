package com.volmit.iris.util.math;

import com.volmit.iris.util.collection.KList;

public class RollingSequence extends Average {
   private double median = 0.0D;
   private double max = 0.0D;
   private double min = 0.0D;
   private boolean dirtyMedian;
   private int dirtyExtremes;
   private boolean precision;

   public RollingSequence(int size) {
      super(var1);
      this.setPrecision(false);
   }

   public double addLast(int amt) {
      double var2 = 0.0D;

      for(int var4 = 0; var4 < Math.min(this.values.length, var1); ++var4) {
         var2 += this.values[var4];
      }

      return var2;
   }

   public boolean isPrecision() {
      return this.precision;
   }

   public void setPrecision(boolean p) {
      this.precision = var1;
   }

   public double getMin() {
      if (this.dirtyExtremes > (this.isPrecision() ? 0 : this.values.length)) {
         this.resetExtremes();
      }

      return this.min;
   }

   public double getMax() {
      if (this.dirtyExtremes > (this.isPrecision() ? 0 : this.values.length)) {
         this.resetExtremes();
      }

      return this.max;
   }

   public double getMedian() {
      if (this.dirtyMedian) {
         this.recalculateMedian();
      }

      return this.median;
   }

   private void recalculateMedian() {
      this.median = (Double)(new KList()).forceAdd(this.values).sort().middleValue();
      this.dirtyMedian = false;
   }

   public void resetExtremes() {
      this.max = -2.147483648E9D;
      this.min = 2.147483647E9D;
      double[] var1 = this.values;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         double var4 = var1[var3];
         this.max = (Double)M.max(this.max, var4);
         this.min = (Double)M.min(this.min, var4);
      }

      this.dirtyExtremes = 0;
   }

   public void put(double i) {
      super.put(var1);
      this.dirtyMedian = true;
      ++this.dirtyExtremes;
      this.max = (Double)M.max(this.max, var1);
      this.min = (Double)M.min(this.min, var1);
   }
}
