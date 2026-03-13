package com.volmit.iris.util.atomics;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.math.M;

public class AtomicRollingSequence extends AtomicAverage {
   private double median = 0.0D;
   private double max = 0.0D;
   private double min = 0.0D;
   private boolean dirtyMedian;
   private int dirtyExtremes;
   private boolean precision;

   public AtomicRollingSequence(int size) {
      super(var1);
      this.setPrecision(false);
   }

   public double addLast(int amt) {
      double var2 = 0.0D;

      for(int var4 = 0; var4 < Math.min(this.values.length(), var1); ++var4) {
         var2 += this.values.get(var4);
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
      if (this.dirtyExtremes > (this.isPrecision() ? 0 : this.values.length())) {
         this.resetExtremes();
      }

      return this.min;
   }

   public double getMax() {
      if (this.dirtyExtremes > (this.isPrecision() ? 0 : this.values.length())) {
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

      for(int var1 = 0; var1 < this.values.length(); ++var1) {
         double var2 = this.values.get(var1);
         this.max = (Double)M.max(this.max, var2);
         this.min = (Double)M.min(this.min, var2);
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
