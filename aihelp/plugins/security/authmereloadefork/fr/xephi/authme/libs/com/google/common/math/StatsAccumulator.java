package fr.xephi.authme.libs.com.google.common.math;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.primitives.Doubles;
import java.util.Iterator;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public final class StatsAccumulator {
   private long count = 0L;
   private double mean = 0.0D;
   private double sumOfSquaresOfDeltas = 0.0D;
   private double min = Double.NaN;
   private double max = Double.NaN;

   public void add(double value) {
      if (this.count == 0L) {
         this.count = 1L;
         this.mean = value;
         this.min = value;
         this.max = value;
         if (!Doubles.isFinite(value)) {
            this.sumOfSquaresOfDeltas = Double.NaN;
         }
      } else {
         ++this.count;
         if (Doubles.isFinite(value) && Doubles.isFinite(this.mean)) {
            double delta = value - this.mean;
            this.mean += delta / (double)this.count;
            this.sumOfSquaresOfDeltas += delta * (value - this.mean);
         } else {
            this.mean = calculateNewMeanNonFinite(this.mean, value);
            this.sumOfSquaresOfDeltas = Double.NaN;
         }

         this.min = Math.min(this.min, value);
         this.max = Math.max(this.max, value);
      }

   }

   public void addAll(Iterable<? extends Number> values) {
      Iterator var2 = values.iterator();

      while(var2.hasNext()) {
         Number value = (Number)var2.next();
         this.add(value.doubleValue());
      }

   }

   public void addAll(Iterator<? extends Number> values) {
      while(values.hasNext()) {
         this.add(((Number)values.next()).doubleValue());
      }

   }

   public void addAll(double... values) {
      double[] var2 = values;
      int var3 = values.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         double value = var2[var4];
         this.add(value);
      }

   }

   public void addAll(int... values) {
      int[] var2 = values;
      int var3 = values.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int value = var2[var4];
         this.add((double)value);
      }

   }

   public void addAll(long... values) {
      long[] var2 = values;
      int var3 = values.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         long value = var2[var4];
         this.add((double)value);
      }

   }

   public void addAll(DoubleStream values) {
      this.addAll((StatsAccumulator)values.collect(StatsAccumulator::new, StatsAccumulator::add, StatsAccumulator::addAll));
   }

   public void addAll(IntStream values) {
      this.addAll((StatsAccumulator)values.collect(StatsAccumulator::new, StatsAccumulator::add, StatsAccumulator::addAll));
   }

   public void addAll(LongStream values) {
      this.addAll((StatsAccumulator)values.collect(StatsAccumulator::new, StatsAccumulator::add, StatsAccumulator::addAll));
   }

   public void addAll(Stats values) {
      if (values.count() != 0L) {
         this.merge(values.count(), values.mean(), values.sumOfSquaresOfDeltas(), values.min(), values.max());
      }
   }

   public void addAll(StatsAccumulator values) {
      if (values.count() != 0L) {
         this.merge(values.count(), values.mean(), values.sumOfSquaresOfDeltas(), values.min(), values.max());
      }
   }

   private void merge(long otherCount, double otherMean, double otherSumOfSquaresOfDeltas, double otherMin, double otherMax) {
      if (this.count == 0L) {
         this.count = otherCount;
         this.mean = otherMean;
         this.sumOfSquaresOfDeltas = otherSumOfSquaresOfDeltas;
         this.min = otherMin;
         this.max = otherMax;
      } else {
         this.count += otherCount;
         if (Doubles.isFinite(this.mean) && Doubles.isFinite(otherMean)) {
            double delta = otherMean - this.mean;
            this.mean += delta * (double)otherCount / (double)this.count;
            this.sumOfSquaresOfDeltas += otherSumOfSquaresOfDeltas + delta * (otherMean - this.mean) * (double)otherCount;
         } else {
            this.mean = calculateNewMeanNonFinite(this.mean, otherMean);
            this.sumOfSquaresOfDeltas = Double.NaN;
         }

         this.min = Math.min(this.min, otherMin);
         this.max = Math.max(this.max, otherMax);
      }

   }

   public Stats snapshot() {
      return new Stats(this.count, this.mean, this.sumOfSquaresOfDeltas, this.min, this.max);
   }

   public long count() {
      return this.count;
   }

   public double mean() {
      Preconditions.checkState(this.count != 0L);
      return this.mean;
   }

   public final double sum() {
      return this.mean * (double)this.count;
   }

   public final double populationVariance() {
      Preconditions.checkState(this.count != 0L);
      if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
         return Double.NaN;
      } else {
         return this.count == 1L ? 0.0D : DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / (double)this.count;
      }
   }

   public final double populationStandardDeviation() {
      return Math.sqrt(this.populationVariance());
   }

   public final double sampleVariance() {
      Preconditions.checkState(this.count > 1L);
      return Double.isNaN(this.sumOfSquaresOfDeltas) ? Double.NaN : DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / (double)(this.count - 1L);
   }

   public final double sampleStandardDeviation() {
      return Math.sqrt(this.sampleVariance());
   }

   public double min() {
      Preconditions.checkState(this.count != 0L);
      return this.min;
   }

   public double max() {
      Preconditions.checkState(this.count != 0L);
      return this.max;
   }

   double sumOfSquaresOfDeltas() {
      return this.sumOfSquaresOfDeltas;
   }

   static double calculateNewMeanNonFinite(double previousMean, double value) {
      if (Doubles.isFinite(previousMean)) {
         return value;
      } else {
         return !Doubles.isFinite(value) && previousMean != value ? Double.NaN : previousMean;
      }
   }
}
