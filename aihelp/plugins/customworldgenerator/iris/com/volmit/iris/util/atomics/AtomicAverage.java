package com.volmit.iris.util.atomics;

import com.google.common.util.concurrent.AtomicDoubleArray;
import com.volmit.iris.Iris;
import com.volmit.iris.util.data.DoubleArrayUtils;

public class AtomicAverage {
   protected final AtomicDoubleArray values;
   protected transient int cursor;
   private transient double average;
   private transient double lastSum;
   private transient boolean dirty;
   private transient boolean brandNew;

   public AtomicAverage(int size) {
      this.values = new AtomicDoubleArray(var1);
      DoubleArrayUtils.fill(this.values, 0.0D);
      this.brandNew = true;
      this.average = 0.0D;
      this.cursor = 0;
      this.lastSum = 0.0D;
      this.dirty = false;
   }

   public synchronized void put(double i) {
      try {
         this.dirty = true;
         if (this.brandNew) {
            DoubleArrayUtils.fill(this.values, var1);
            this.lastSum = (double)this.size() * var1;
            this.brandNew = false;
            return;
         }

         double var3 = this.values.get(this.cursor);
         this.lastSum = this.lastSum - var3 + var1;
         this.values.set(this.cursor, var1);
         this.cursor = this.cursor + 1 < this.size() ? this.cursor + 1 : 0;
      } catch (Throwable var5) {
         Iris.reportError(var5);
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
      return this.values.length();
   }

   public boolean isDirty() {
      return this.dirty;
   }
}
