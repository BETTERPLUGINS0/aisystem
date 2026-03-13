package com.volmit.iris.util.noise;

import com.volmit.iris.util.math.RNG;
import lombok.Generated;

public class CellGenerator {
   private final FastNoiseDouble fn;
   private final FastNoiseDouble fd;
   private final CNG cng;
   private double cellScale = 0.73D;
   private double shuffle = 128.0D;

   public CellGenerator(RNG rng) {
      this.cng = CNG.signature(var1.nextParallelRNG(3204));
      RNG var2 = var1.nextParallelRNG(8735652);
      long var3 = var2.lmax();
      this.fn = new FastNoiseDouble(var3);
      this.fn.setNoiseType(FastNoiseDouble.NoiseType.Cellular);
      this.fn.setCellularReturnType(FastNoiseDouble.CellularReturnType.CellValue);
      this.fn.setCellularDistanceFunction(FastNoiseDouble.CellularDistanceFunction.Natural);
      this.fd = new FastNoiseDouble(var3);
      this.fd.setNoiseType(FastNoiseDouble.NoiseType.Cellular);
      this.fd.setCellularReturnType(FastNoiseDouble.CellularReturnType.Distance2Sub);
      this.fd.setCellularDistanceFunction(FastNoiseDouble.CellularDistanceFunction.Natural);
   }

   public double getDistance(double x, double z) {
      return (this.fd.GetCellular(var1 * this.cellScale + this.cng.noise(var1, var3) * this.shuffle, var3 * this.cellScale + this.cng.noise(var3, var1) * this.shuffle) + 1.0D) / 2.0D;
   }

   public double getDistance(double x, double y, double z) {
      return (this.fd.GetCellular(var1 * this.cellScale + this.cng.noise(var1, var3, var5) * this.shuffle, var3 * this.cellScale + this.cng.noise(var1, var3, var5) * this.shuffle, var5 * this.cellScale + this.cng.noise(var5, var3, var1) * this.shuffle) + 1.0D) / 2.0D;
   }

   public double getValue(double x, double z, int possibilities) {
      return var5 == 1 ? 0.0D : (this.fn.GetCellular(var1 * this.cellScale + this.cng.noise(var1, var3) * this.shuffle, var3 * this.cellScale + this.cng.noise(var3, var1) * this.shuffle) + 1.0D) / 2.0D * (double)(var5 - 1);
   }

   public double getValue(double x, double y, double z, int possibilities) {
      return var7 == 1 ? 0.0D : (this.fn.GetCellular(var1 * this.cellScale + this.cng.noise(var1, var5) * this.shuffle, var3 * 8.0D * this.cellScale + this.cng.noise(var1, var3 * 8.0D) * this.shuffle, var5 * this.cellScale + this.cng.noise(var5, var1) * this.shuffle) + 1.0D) / 2.0D * (double)(var7 - 1);
   }

   public int getIndex(double x, double z, int possibilities) {
      return var5 == 1 ? 0 : (int)Math.round(this.getValue(var1, var3, var5));
   }

   public int getIndex(double x, double y, double z, int possibilities) {
      return var7 == 1 ? 0 : (int)Math.round(this.getValue(var1, var3, var5, var7));
   }

   @Generated
   public double getCellScale() {
      return this.cellScale;
   }

   @Generated
   public void setCellScale(final double cellScale) {
      this.cellScale = var1;
   }

   @Generated
   public double getShuffle() {
      return this.shuffle;
   }

   @Generated
   public void setShuffle(final double shuffle) {
      this.shuffle = var1;
   }
}
