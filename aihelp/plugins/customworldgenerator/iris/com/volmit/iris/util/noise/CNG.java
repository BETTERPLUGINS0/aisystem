package com.volmit.iris.util.noise;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.object.IRare;
import com.volmit.iris.engine.object.NoiseStyle;
import com.volmit.iris.util.cache.FloatCache;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.function.NoiseInjector;
import com.volmit.iris.util.interpolation.IrisInterpolation;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import com.volmit.iris.util.stream.ProceduralStream;
import com.volmit.iris.util.stream.arithmetic.FittedStream;
import com.volmit.iris.util.stream.sources.CNGStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;

public class CNG {
   public static final NoiseInjector ADD = (var0, var2) -> {
      return new double[]{var0 + var2, 1.0D};
   };
   public static final NoiseInjector SRC_SUBTRACT = (var0, var2) -> {
      return new double[]{var0 - var2 < 0.0D ? 0.0D : var0 - var2, -1.0D};
   };
   public static final NoiseInjector DST_SUBTRACT = (var0, var2) -> {
      return new double[]{var2 - var0 < 0.0D ? 0.0D : var0 - var2, -1.0D};
   };
   public static final NoiseInjector MULTIPLY = (var0, var2) -> {
      return new double[]{var0 * var2, 0.0D};
   };
   public static final NoiseInjector MAX = (var0, var2) -> {
      return new double[]{Math.max(var0, var2), 0.0D};
   };
   public static final NoiseInjector MIN = (var0, var2) -> {
      return new double[]{Math.min(var0, var2), 0.0D};
   };
   public static final NoiseInjector SRC_MOD = (var0, var2) -> {
      return new double[]{var0 % var2, 0.0D};
   };
   public static final NoiseInjector SRC_POW = (var0, var2) -> {
      return new double[]{Math.pow(var0, var2), 0.0D};
   };
   public static final NoiseInjector DST_MOD = (var0, var2) -> {
      return new double[]{var2 % var0, 0.0D};
   };
   public static final NoiseInjector DST_POW = (var0, var2) -> {
      return new double[]{Math.pow(var2, var0), 0.0D};
   };
   public static long hits = 0L;
   public static long creates = 0L;
   private final double opacity;
   private double scale;
   private double bakedScale;
   private double fscale;
   private boolean trueFracturing;
   private KList<CNG> children;
   private CNG fracture;
   private FloatCache cache;
   private NoiseGenerator generator;
   private NoiseInjector injector;
   private RNG rng;
   private boolean noscale;
   private int oct;
   private double patch;
   private double up;
   private double down;
   private double power;
   private NoiseStyle leakStyle;
   private ProceduralStream<Double> customGenerator;

   public CNG(RNG random) {
      this(var1, 1);
   }

   public CNG(RNG random, int octaves) {
      this(var1, 1.0D, var2);
   }

   public CNG(RNG random, double opacity, int octaves) {
      this(var1, NoiseType.SIMPLEX, var2, var4);
   }

   public CNG(RNG random, NoiseType type, double opacity, int octaves) {
      this(var1, var2.create(var1.nextParallelRNG((long)(1.113334944E9D * var3 + 12922.0D + (double)var5)).lmax()), var3, var5);
   }

   public CNG(RNG random, NoiseGenerator generator, double opacity, int octaves) {
      this.trueFracturing = false;
      this.customGenerator = null;
      ++creates;
      this.noscale = var2.isNoScale();
      this.oct = var5;
      this.rng = var1;
      this.power = 1.0D;
      this.scale = 1.0D;
      this.patch = 1.0D;
      this.bakedScale = 1.0D;
      this.fscale = 1.0D;
      this.down = 0.0D;
      this.up = 0.0D;
      this.fracture = null;
      this.generator = var2;
      this.opacity = var3;
      this.injector = ADD;
      if (var2 instanceof OctaveNoise) {
         ((OctaveNoise)var2).setOctaves(var5);
      }

   }

   public static CNG signature(RNG rng) {
      return signature(var0, NoiseType.SIMPLEX);
   }

   public static CNG signatureHalf(RNG rng) {
      return signatureHalf(var0, NoiseType.SIMPLEX);
   }

   public static CNG signatureThick(RNG rng) {
      return signatureThick(var0, NoiseType.SIMPLEX);
   }

   public static CNG signatureDouble(RNG rng) {
      return signatureDouble(var0, NoiseType.SIMPLEX);
   }

   public static CNG signatureDouble(RNG rng, NoiseType t) {
      return signatureThick(var0, var1).fractureWith(signature(var0.nextParallelRNG(4956)), 93.0D);
   }

   public static CNG signatureDoubleFast(RNG rng, NoiseType t, NoiseType f) {
      return signatureThickFast(var0, var1, var2).fractureWith(signatureFast(var0.nextParallelRNG(4956), var1, var2), 93.0D);
   }

   public static CNG signature(RNG rng, NoiseType t) {
      return (new CNG(var0.nextParallelRNG(17), var1, 1.0D, 1)).fractureWith((new CNG(var0.nextParallelRNG(18), 1.0D, 1)).scale(0.9D).fractureWith((new CNG(var0.nextParallelRNG(20), 1.0D, 1)).scale(0.21D).fractureWith((new CNG(var0.nextParallelRNG(20), 1.0D, 1)).scale(0.9D), 620.0D), 145.0D), 44.0D).bake();
   }

   public static CNG signaturePerlin(RNG rng) {
      return signaturePerlin(var0, NoiseType.PERLIN);
   }

   public static CNG signaturePerlin(RNG rng, NoiseType t) {
      return (new CNG(var0.nextParallelRNG(124996), var1, 1.0D, 1)).fractureWith((new CNG(var0.nextParallelRNG(18), NoiseType.PERLIN, 1.0D, 1)).scale(1.25D), 250.0D).bake();
   }

   public static CNG signatureFast(RNG rng, NoiseType t, NoiseType f) {
      return (new CNG(var0.nextParallelRNG(17), var1, 1.0D, 1)).fractureWith((new CNG(var0.nextParallelRNG(18), var2, 1.0D, 1)).scale(0.9D).fractureWith((new CNG(var0.nextParallelRNG(20), var2, 1.0D, 1)).scale(0.21D).fractureWith((new CNG(var0.nextParallelRNG(20), var2, 1.0D, 1)).scale(0.9D), 620.0D), 145.0D), 44.0D).bake();
   }

   public static CNG signatureThick(RNG rng, NoiseType t) {
      return (new CNG(var0.nextParallelRNG(133), var1, 1.0D, 1)).fractureWith((new CNG(var0.nextParallelRNG(18), 1.0D, 1)).scale(0.5D).fractureWith((new CNG(var0.nextParallelRNG(20), 1.0D, 1)).scale(0.11D).fractureWith((new CNG(var0.nextParallelRNG(20), 1.0D, 1)).scale(0.4D), 620.0D), 145.0D), 44.0D).bake();
   }

   public static CNG signatureThickFast(RNG rng, NoiseType t, NoiseType f) {
      return (new CNG(var0.nextParallelRNG(133), var1, 1.0D, 1)).fractureWith((new CNG(var0.nextParallelRNG(18), var2, 1.0D, 1)).scale(0.5D).fractureWith((new CNG(var0.nextParallelRNG(20), var2, 1.0D, 1)).scale(0.11D).fractureWith((new CNG(var0.nextParallelRNG(20), var2, 1.0D, 1)).scale(0.4D), 620.0D), 145.0D), 44.0D).bake();
   }

   public static CNG signatureHalf(RNG rng, NoiseType t) {
      return (new CNG(var0.nextParallelRNG(127), var1, 1.0D, 1)).fractureWith((new CNG(var0.nextParallelRNG(18), 1.0D, 1)).scale(0.9D).fractureWith((new CNG(var0.nextParallelRNG(20), 1.0D, 1)).scale(0.21D).fractureWith((new CNG(var0.nextParallelRNG(20), 1.0D, 1)).scale(0.9D), 420.0D), 99.0D), 22.0D).bake();
   }

   public static CNG signatureHalfFast(RNG rng, NoiseType t, NoiseType f) {
      return (new CNG(var0.nextParallelRNG(127), var1, 1.0D, 1)).fractureWith((new CNG(var0.nextParallelRNG(18), var2, 1.0D, 1)).scale(0.9D).fractureWith((new CNG(var0.nextParallelRNG(20), var2, 1.0D, 1)).scale(0.21D).fractureWith((new CNG(var0.nextParallelRNG(20), var2, 1.0D, 1)).scale(0.9D), 420.0D), 99.0D), 22.0D).bake();
   }

   public static void main(String[] a) {
      CNG var1 = NoiseStyle.SIMPLEX.create(new RNG(1234L));
      PrecisionStopwatch var2 = PrecisionStopwatch.start();
      double var3 = 0.0D;

      for(int var5 = 0; var5 < 300000000; ++var5) {
         var3 += (double)var1.fit(-1000, 1000, (double)var5, (double)var5);
      }

      PrintStream var10000 = System.out;
      String var10001 = Form.duration(var2.getMilliseconds(), 10);
      var10000.println(var10001 + " merged = " + var3);
   }

   public CNG cellularize(RNG seed, double freq) {
      final FastNoise var4 = new FastNoise(var1.imax());
      var4.SetNoiseType(FastNoise.NoiseType.Cellular);
      var4.SetCellularReturnType(FastNoise.CellularReturnType.CellValue);
      var4.SetCellularDistanceFunction(FastNoise.CellularDistanceFunction.Manhattan);
      var4.SetFrequency((float)var2 * 0.01F);
      final ProceduralStream var5 = this.stream();
      return new CNG(var1, new NoiseGenerator(this) {
         public double noise(double x) {
            return this.noise(var1, 0.0D);
         }

         public double noise(double x, double z) {
            return (double)var4.GetCellular((float)var1, (float)var3, var5, 1.0D) * 0.5D + 0.5D;
         }

         public double noise(double x, double y, double z) {
            return this.noise(var1, var3 + var5x);
         }
      }, 1.0D, 1);
   }

   public CNG cached(int size, String key, File cacheFolder) {
      if (var1 <= 0) {
         return this;
      } else {
         this.cache = null;
         File var4 = new File(new File(var3, ".cache"), var2 + ".cnm");
         boolean var6 = false;
         FloatCache var5;
         if (var4.exists()) {
            try {
               var5 = new FloatCache(var4);
               var6 = true;
            } catch (IOException var10) {
               var5 = new FloatCache(var1, var1);
            }
         } else {
            var5 = new FloatCache(var1, var1);
         }

         if (!var6) {
            for(int var7 = 0; var7 < var1; ++var7) {
               for(int var8 = 0; var8 < var1; ++var8) {
                  var5.set(var7, var8, (float)this.noise((double)var7, (double)var8));
               }
            }

            try {
               var4.getParentFile().mkdirs();
               FileOutputStream var11 = new FileOutputStream(var4);
               DataOutputStream var12 = new DataOutputStream(var11);
               var5.writeCache(var12);
               var12.close();
               Iris.info("Saved Noise Cache " + var4.getName());
            } catch (IOException var9) {
               throw new RuntimeException(var9);
            }
         }

         this.cache = var5;
         return this;
      }
   }

   public NoiseGenerator getGen() {
      return this.generator;
   }

   public ProceduralStream<Double> stream() {
      return new CNGStream(this);
   }

   public ProceduralStream<Double> stream(double min, double max) {
      return new FittedStream(this.stream(), var1, var3);
   }

   public CNG bake() {
      this.bakedScale *= this.scale;
      this.scale = 1.0D;
      return this;
   }

   public CNG child(CNG c) {
      if (this.children == null) {
         this.children = new KList();
      }

      this.children.add((Object)var1);
      return this;
   }

   public RNG getRNG() {
      return this.rng;
   }

   public CNG fractureWith(CNG c, double scale) {
      this.fracture = var1;
      this.fscale = var2;
      return this;
   }

   public CNG scale(double c) {
      this.scale = var1;
      return this;
   }

   public CNG patch(double c) {
      this.patch = var1;
      return this;
   }

   public CNG up(double c) {
      this.up = var1;
      return this;
   }

   public CNG down(double c) {
      this.down = var1;
      return this;
   }

   public CNG injectWith(NoiseInjector i) {
      this.injector = var1;
      return this;
   }

   public <T extends IRare> T fitRarity(KList<T> b, double... dim) {
      if (var1.size() == 0) {
         return null;
      } else if (var1.size() == 1) {
         return (IRare)var1.get(0);
      } else {
         KList var3 = new KList();
         boolean var4 = false;
         int var5 = 1;
         Iterator var6 = var1.iterator();

         IRare var7;
         while(var6.hasNext()) {
            var7 = (IRare)var6.next();
            if (var7.getRarity() > var5) {
               var5 = var7.getRarity();
            }
         }

         ++var5;
         var6 = var1.iterator();

         while(var6.hasNext()) {
            var7 = (IRare)var6.next();

            for(int var8 = 0; var8 < var5 - var7.getRarity(); ++var8) {
               if (var4 = !var4) {
                  var3.add((Object)var7);
               } else {
                  var3.add(0, var7);
               }
            }
         }

         if (var3.size() == 1) {
            return (IRare)var3.get(0);
         } else if (var3.isEmpty()) {
            throw new RuntimeException("BAD RARITY MAP! RELATED TO: " + var1.toString(", or possibly "));
         } else {
            return (IRare)this.fit((List)var3, var2);
         }
      }
   }

   public <T> T fit(T[] v, double... dim) {
      if (var1.length == 0) {
         return null;
      } else {
         return var1.length == 1 ? var1[0] : var1[this.fit(0, var1.length - 1, var2)];
      }
   }

   public <T> T fit(List<T> v, double... dim) {
      if (var1.size() == 0) {
         return null;
      } else if (var1.size() == 1) {
         return var1.get(0);
      } else {
         try {
            return var1.get(this.fit(0, var1.size() - 1, var2));
         } catch (Throwable var4) {
            Iris.reportError(var4);
            return var1.get(0);
         }
      }
   }

   public int fit(int min, int max, double... dim) {
      if (var1 == var2) {
         return var1;
      } else {
         double var4 = this.noise(var3);
         return (int)Math.round(IrisInterpolation.lerp((double)var1, (double)var2, var4));
      }
   }

   public int fit(double min, double max, double... dim) {
      if (var1 == var3) {
         return (int)Math.round(var1);
      } else {
         double var6 = this.noise(var5);
         return (int)Math.round(IrisInterpolation.lerp(var1, var3, var6));
      }
   }

   public double fitDouble(double min, double max, double... dim) {
      if (var1 == var3) {
         return var1;
      } else {
         double var6 = this.noise(var5);
         return IrisInterpolation.lerp(var1, var3, var6);
      }
   }

   private double getNoise(double... dim) {
      double var2 = this.noscale ? 1.0D : this.bakedScale * this.scale;
      if (this.fracture != null && !this.noscale) {
         double var4;
         double var6;
         double var8;
         if (this.fracture.isTrueFracturing()) {
            var4 = var1.length > 0 ? var1[0] + (this.fracture.noise(var1) - 0.5D) * this.fscale : 0.0D;
            var6 = var1.length > 1 ? var1[1] + (this.fracture.noise(var1[1], var1[0]) - 0.5D) * this.fscale : 0.0D;
            var8 = var1.length > 2 ? var1[2] + (this.fracture.noise(var1[2], var1[0], var1[1]) - 0.5D) * this.fscale : 0.0D;
            return this.generator.noise(var4 * var2, var6 * var2, var8 * var2) * this.opacity;
         } else {
            var4 = this.fracture.noise(var1) * this.fscale;
            var6 = var1.length > 0 ? var1[0] + var4 : 0.0D;
            var8 = var1.length > 1 ? var1[1] - var4 : 0.0D;
            double var10 = var1.length > 2 ? var1[2] - var4 : 0.0D;
            return this.generator.noise(var6 * var2, var8 * var2, var10 * var2) * this.opacity;
         }
      } else {
         return this.generator.noise((var1.length > 0 ? var1[0] : 0.0D) * var2, (var1.length > 1 ? var1[1] : 0.0D) * var2, (var1.length > 2 ? var1[2] : 0.0D) * var2) * this.opacity;
      }
   }

   public double invertNoise(double... dim) {
      if (var1.length == 1) {
         return this.noise(-var1[0]);
      } else if (var1.length == 2) {
         return this.noise(var1[1], var1[0]);
      } else {
         return var1.length == 3 ? this.noise(var1[1], var1[2], var1[0]) : this.noise(var1);
      }
   }

   public double noiseSym(double... dim) {
      return this.noise(var1) * 2.0D - 1.0D;
   }

   public double noise(double... dim) {
      if (this.cache != null && var1.length == 2) {
         return (double)(Float)this.cache.get((int)var1[0], (int)var1[1]);
      } else {
         double var2 = this.getNoise(var1);
         var2 = this.power != 1.0D ? (var2 < 0.0D ? -Math.pow(Math.abs(var2), this.power) : Math.pow(var2, this.power)) : var2;
         double var4 = 1.0D;
         hits += (long)this.oct;
         if (this.children == null) {
            return (var2 - this.down + this.up) * this.patch;
         } else {
            double[] var8;
            for(Iterator var6 = this.children.iterator(); var6.hasNext(); var4 += var8[1]) {
               CNG var7 = (CNG)var6.next();
               var8 = this.injector.combine(var2, var7.noise(var1));
               var2 = var8[0];
            }

            return (var2 / var4 - this.down + this.up) * this.patch;
         }
      }
   }

   public CNG pow(double power) {
      this.power = var1;
      return this;
   }

   public CNG oct(int octaves) {
      this.oct = var1;
      return this;
   }

   public double getScale() {
      return this.scale;
   }

   public boolean isStatic() {
      return this.generator != null && this.generator.isStatic();
   }

   @Generated
   public double getOpacity() {
      return this.opacity;
   }

   @Generated
   public double getBakedScale() {
      return this.bakedScale;
   }

   @Generated
   public double getFscale() {
      return this.fscale;
   }

   @Generated
   public boolean isTrueFracturing() {
      return this.trueFracturing;
   }

   @Generated
   public KList<CNG> getChildren() {
      return this.children;
   }

   @Generated
   public CNG getFracture() {
      return this.fracture;
   }

   @Generated
   public FloatCache getCache() {
      return this.cache;
   }

   @Generated
   public NoiseGenerator getGenerator() {
      return this.generator;
   }

   @Generated
   public NoiseInjector getInjector() {
      return this.injector;
   }

   @Generated
   public boolean isNoscale() {
      return this.noscale;
   }

   @Generated
   public int getOct() {
      return this.oct;
   }

   @Generated
   public double getPatch() {
      return this.patch;
   }

   @Generated
   public double getUp() {
      return this.up;
   }

   @Generated
   public double getDown() {
      return this.down;
   }

   @Generated
   public double getPower() {
      return this.power;
   }

   @Generated
   public NoiseStyle getLeakStyle() {
      return this.leakStyle;
   }

   @Generated
   public ProceduralStream<Double> getCustomGenerator() {
      return this.customGenerator;
   }

   @Generated
   public void setScale(final double scale) {
      this.scale = var1;
   }

   @Generated
   public void setBakedScale(final double bakedScale) {
      this.bakedScale = var1;
   }

   @Generated
   public void setFscale(final double fscale) {
      this.fscale = var1;
   }

   @Generated
   public void setTrueFracturing(final boolean trueFracturing) {
      this.trueFracturing = var1;
   }

   @Generated
   public void setChildren(final KList<CNG> children) {
      this.children = var1;
   }

   @Generated
   public void setFracture(final CNG fracture) {
      this.fracture = var1;
   }

   @Generated
   public void setCache(final FloatCache cache) {
      this.cache = var1;
   }

   @Generated
   public void setGenerator(final NoiseGenerator generator) {
      this.generator = var1;
   }

   @Generated
   public void setInjector(final NoiseInjector injector) {
      this.injector = var1;
   }

   @Generated
   public void setRng(final RNG rng) {
      this.rng = var1;
   }

   @Generated
   public void setNoscale(final boolean noscale) {
      this.noscale = var1;
   }

   @Generated
   public void setOct(final int oct) {
      this.oct = var1;
   }

   @Generated
   public void setPatch(final double patch) {
      this.patch = var1;
   }

   @Generated
   public void setUp(final double up) {
      this.up = var1;
   }

   @Generated
   public void setDown(final double down) {
      this.down = var1;
   }

   @Generated
   public void setPower(final double power) {
      this.power = var1;
   }

   @Generated
   public void setLeakStyle(final NoiseStyle leakStyle) {
      this.leakStyle = var1;
   }

   @Generated
   public void setCustomGenerator(final ProceduralStream<Double> customGenerator) {
      this.customGenerator = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof CNG)) {
         return false;
      } else {
         CNG var2 = (CNG)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getOpacity(), var2.getOpacity()) != 0) {
            return false;
         } else if (Double.compare(this.getScale(), var2.getScale()) != 0) {
            return false;
         } else if (Double.compare(this.getBakedScale(), var2.getBakedScale()) != 0) {
            return false;
         } else if (Double.compare(this.getFscale(), var2.getFscale()) != 0) {
            return false;
         } else if (this.isTrueFracturing() != var2.isTrueFracturing()) {
            return false;
         } else if (this.isNoscale() != var2.isNoscale()) {
            return false;
         } else if (this.getOct() != var2.getOct()) {
            return false;
         } else if (Double.compare(this.getPatch(), var2.getPatch()) != 0) {
            return false;
         } else if (Double.compare(this.getUp(), var2.getUp()) != 0) {
            return false;
         } else if (Double.compare(this.getDown(), var2.getDown()) != 0) {
            return false;
         } else if (Double.compare(this.getPower(), var2.getPower()) != 0) {
            return false;
         } else {
            label134: {
               KList var3 = this.getChildren();
               KList var4 = var2.getChildren();
               if (var3 == null) {
                  if (var4 == null) {
                     break label134;
                  }
               } else if (var3.equals(var4)) {
                  break label134;
               }

               return false;
            }

            CNG var5 = this.getFracture();
            CNG var6 = var2.getFracture();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            label120: {
               FloatCache var7 = this.getCache();
               FloatCache var8 = var2.getCache();
               if (var7 == null) {
                  if (var8 == null) {
                     break label120;
                  }
               } else if (var7.equals(var8)) {
                  break label120;
               }

               return false;
            }

            NoiseGenerator var9 = this.getGenerator();
            NoiseGenerator var10 = var2.getGenerator();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            label106: {
               NoiseInjector var11 = this.getInjector();
               NoiseInjector var12 = var2.getInjector();
               if (var11 == null) {
                  if (var12 == null) {
                     break label106;
                  }
               } else if (var11.equals(var12)) {
                  break label106;
               }

               return false;
            }

            RNG var13 = this.getRNG();
            RNG var14 = var2.getRNG();
            if (var13 == null) {
               if (var14 != null) {
                  return false;
               }
            } else if (!var13.equals(var14)) {
               return false;
            }

            label92: {
               NoiseStyle var15 = this.getLeakStyle();
               NoiseStyle var16 = var2.getLeakStyle();
               if (var15 == null) {
                  if (var16 == null) {
                     break label92;
                  }
               } else if (var15.equals(var16)) {
                  break label92;
               }

               return false;
            }

            ProceduralStream var17 = this.getCustomGenerator();
            ProceduralStream var18 = var2.getCustomGenerator();
            if (var17 == null) {
               if (var18 != null) {
                  return false;
               }
            } else if (!var17.equals(var18)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof CNG;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.getOpacity());
      int var27 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getScale());
      var27 = var27 * 59 + (int)(var5 >>> 32 ^ var5);
      long var7 = Double.doubleToLongBits(this.getBakedScale());
      var27 = var27 * 59 + (int)(var7 >>> 32 ^ var7);
      long var9 = Double.doubleToLongBits(this.getFscale());
      var27 = var27 * 59 + (int)(var9 >>> 32 ^ var9);
      var27 = var27 * 59 + (this.isTrueFracturing() ? 79 : 97);
      var27 = var27 * 59 + (this.isNoscale() ? 79 : 97);
      var27 = var27 * 59 + this.getOct();
      long var11 = Double.doubleToLongBits(this.getPatch());
      var27 = var27 * 59 + (int)(var11 >>> 32 ^ var11);
      long var13 = Double.doubleToLongBits(this.getUp());
      var27 = var27 * 59 + (int)(var13 >>> 32 ^ var13);
      long var15 = Double.doubleToLongBits(this.getDown());
      var27 = var27 * 59 + (int)(var15 >>> 32 ^ var15);
      long var17 = Double.doubleToLongBits(this.getPower());
      var27 = var27 * 59 + (int)(var17 >>> 32 ^ var17);
      KList var19 = this.getChildren();
      var27 = var27 * 59 + (var19 == null ? 43 : var19.hashCode());
      CNG var20 = this.getFracture();
      var27 = var27 * 59 + (var20 == null ? 43 : var20.hashCode());
      FloatCache var21 = this.getCache();
      var27 = var27 * 59 + (var21 == null ? 43 : var21.hashCode());
      NoiseGenerator var22 = this.getGenerator();
      var27 = var27 * 59 + (var22 == null ? 43 : var22.hashCode());
      NoiseInjector var23 = this.getInjector();
      var27 = var27 * 59 + (var23 == null ? 43 : var23.hashCode());
      RNG var24 = this.getRNG();
      var27 = var27 * 59 + (var24 == null ? 43 : var24.hashCode());
      NoiseStyle var25 = this.getLeakStyle();
      var27 = var27 * 59 + (var25 == null ? 43 : var25.hashCode());
      ProceduralStream var26 = this.getCustomGenerator();
      var27 = var27 * 59 + (var26 == null ? 43 : var26.hashCode());
      return var27;
   }

   @Generated
   public String toString() {
      double var10000 = this.getOpacity();
      return "CNG(opacity=" + var10000 + ", scale=" + this.getScale() + ", bakedScale=" + this.getBakedScale() + ", fscale=" + this.getFscale() + ", trueFracturing=" + this.isTrueFracturing() + ", children=" + String.valueOf(this.getChildren()) + ", fracture=" + String.valueOf(this.getFracture()) + ", cache=" + String.valueOf(this.getCache()) + ", generator=" + String.valueOf(this.getGenerator()) + ", injector=" + String.valueOf(this.getInjector()) + ", rng=" + String.valueOf(this.getRNG()) + ", noscale=" + this.isNoscale() + ", oct=" + this.getOct() + ", patch=" + this.getPatch() + ", up=" + this.getUp() + ", down=" + this.getDown() + ", power=" + this.getPower() + ", leakStyle=" + String.valueOf(this.getLeakStyle()) + ", customGenerator=" + String.valueOf(this.getCustomGenerator()) + ")";
   }
}
