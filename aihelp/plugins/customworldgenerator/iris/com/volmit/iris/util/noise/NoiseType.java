package com.volmit.iris.util.noise;

import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.util.interpolation.InterpolationMethod;

public enum NoiseType {
   WHITE(WhiteNoise::new),
   WHITE_BILINEAR((var0) -> {
      return new InterpolatedNoise(var0, WHITE, InterpolationMethod.BILINEAR);
   }),
   WHITE_BICUBIC((var0) -> {
      return new InterpolatedNoise(var0, WHITE, InterpolationMethod.BICUBIC);
   }),
   WHITE_HERMITE((var0) -> {
      return new InterpolatedNoise(var0, WHITE, InterpolationMethod.HERMITE);
   }),
   SIMPLEX(SimplexNoise::new),
   PERLIN((var0) -> {
      return (new PerlinNoise(var0)).hermite();
   }),
   FRACTAL_BILLOW_SIMPLEX(FractalBillowSimplexNoise::new),
   FRACTAL_BILLOW_PERLIN(FractalBillowPerlinNoise::new),
   FRACTAL_FBM_SIMPLEX(FractalFBMSimplexNoise::new),
   FRACTAL_RIGID_MULTI_SIMPLEX(FractalRigidMultiSimplexNoise::new),
   FLAT(FlatNoise::new),
   CELLULAR(CellularNoise::new),
   CELLULAR_BILINEAR((var0) -> {
      return new InterpolatedNoise(var0, CELLULAR, InterpolationMethod.BILINEAR);
   }),
   CELLULAR_BILINEAR_STARCAST_3((var0) -> {
      return new InterpolatedNoise(var0, CELLULAR, InterpolationMethod.BILINEAR_STARCAST_3);
   }),
   CELLULAR_BILINEAR_STARCAST_6((var0) -> {
      return new InterpolatedNoise(var0, CELLULAR, InterpolationMethod.BILINEAR_STARCAST_6);
   }),
   CELLULAR_BILINEAR_STARCAST_9((var0) -> {
      return new InterpolatedNoise(var0, CELLULAR, InterpolationMethod.BILINEAR_STARCAST_9);
   }),
   CELLULAR_BILINEAR_STARCAST_12((var0) -> {
      return new InterpolatedNoise(var0, CELLULAR, InterpolationMethod.BILINEAR_STARCAST_12);
   }),
   CELLULAR_BICUBIC((var0) -> {
      return new InterpolatedNoise(var0, CELLULAR, InterpolationMethod.BICUBIC);
   }),
   CELLULAR_HERMITE((var0) -> {
      return new InterpolatedNoise(var0, CELLULAR, InterpolationMethod.HERMITE);
   }),
   CELLULAR_STARCAST_3((var0) -> {
      return new InterpolatedNoise(var0, CELLULAR, InterpolationMethod.STARCAST_3);
   }),
   CELLULAR_STARCAST_6((var0) -> {
      return new InterpolatedNoise(var0, CELLULAR, InterpolationMethod.STARCAST_6);
   }),
   CELLULAR_STARCAST_9((var0) -> {
      return new InterpolatedNoise(var0, CELLULAR, InterpolationMethod.STARCAST_9);
   }),
   CELLULAR_STARCAST_12((var0) -> {
      return new InterpolatedNoise(var0, CELLULAR, InterpolationMethod.STARCAST_12);
   }),
   CELLULAR_HERMITE_STARCAST_3((var0) -> {
      return new InterpolatedNoise(var0, CELLULAR, InterpolationMethod.HERMITE_STARCAST_3);
   }),
   CELLULAR_HERMITE_STARCAST_6((var0) -> {
      return new InterpolatedNoise(var0, CELLULAR, InterpolationMethod.HERMITE_STARCAST_6);
   }),
   CELLULAR_HERMITE_STARCAST_9((var0) -> {
      return new InterpolatedNoise(var0, CELLULAR, InterpolationMethod.HERMITE_STARCAST_9);
   }),
   CELLULAR_HERMITE_STARCAST_12((var0) -> {
      return new InterpolatedNoise(var0, CELLULAR, InterpolationMethod.HERMITE_STARCAST_12);
   }),
   GLOB(GlobNoise::new),
   CUBIC(CubicNoise::new),
   FRACTAL_CUBIC(FractalCubicNoise::new),
   CELLULAR_HEIGHT(CellHeightNoise::new),
   CLOVER(CloverNoise::new),
   CLOVER_BILINEAR((var0) -> {
      return new InterpolatedNoise(var0, CLOVER, InterpolationMethod.BILINEAR);
   }),
   CLOVER_BILINEAR_STARCAST_3((var0) -> {
      return new InterpolatedNoise(var0, CLOVER, InterpolationMethod.BILINEAR_STARCAST_3);
   }),
   CLOVER_BILINEAR_STARCAST_6((var0) -> {
      return new InterpolatedNoise(var0, CLOVER, InterpolationMethod.BILINEAR_STARCAST_6);
   }),
   CLOVER_BILINEAR_STARCAST_9((var0) -> {
      return new InterpolatedNoise(var0, CLOVER, InterpolationMethod.BILINEAR_STARCAST_9);
   }),
   CLOVER_BILINEAR_STARCAST_12((var0) -> {
      return new InterpolatedNoise(var0, CLOVER, InterpolationMethod.BILINEAR_STARCAST_12);
   }),
   CLOVER_BICUBIC((var0) -> {
      return new InterpolatedNoise(var0, CLOVER, InterpolationMethod.BICUBIC);
   }),
   CLOVER_HERMITE((var0) -> {
      return new InterpolatedNoise(var0, CLOVER, InterpolationMethod.HERMITE);
   }),
   CLOVER_STARCAST_3((var0) -> {
      return new InterpolatedNoise(var0, CLOVER, InterpolationMethod.STARCAST_3);
   }),
   CLOVER_STARCAST_6((var0) -> {
      return new InterpolatedNoise(var0, CLOVER, InterpolationMethod.STARCAST_6);
   }),
   CLOVER_STARCAST_9((var0) -> {
      return new InterpolatedNoise(var0, CLOVER, InterpolationMethod.STARCAST_9);
   }),
   CLOVER_STARCAST_12((var0) -> {
      return new InterpolatedNoise(var0, CLOVER, InterpolationMethod.STARCAST_12);
   }),
   CLOVER_HERMITE_STARCAST_3((var0) -> {
      return new InterpolatedNoise(var0, CLOVER, InterpolationMethod.HERMITE_STARCAST_3);
   }),
   CLOVER_HERMITE_STARCAST_6((var0) -> {
      return new InterpolatedNoise(var0, CLOVER, InterpolationMethod.HERMITE_STARCAST_6);
   }),
   CLOVER_HERMITE_STARCAST_9((var0) -> {
      return new InterpolatedNoise(var0, CLOVER, InterpolationMethod.HERMITE_STARCAST_9);
   }),
   CLOVER_HERMITE_STARCAST_12((var0) -> {
      return new InterpolatedNoise(var0, CLOVER, InterpolationMethod.HERMITE_STARCAST_12);
   }),
   VASCULAR(VascularNoise::new);

   private final NoiseFactory f;

   private NoiseType(NoiseFactory f) {
      this.f = var3;
   }

   public NoiseGenerator create(long seed) {
      return (NoiseGenerator)(IrisSettings.get().getGenerator().offsetNoiseTypes ? this.f.create(var1).offset(var1) : this.f.create(var1));
   }

   // $FF: synthetic method
   private static NoiseType[] $values() {
      return new NoiseType[]{WHITE, WHITE_BILINEAR, WHITE_BICUBIC, WHITE_HERMITE, SIMPLEX, PERLIN, FRACTAL_BILLOW_SIMPLEX, FRACTAL_BILLOW_PERLIN, FRACTAL_FBM_SIMPLEX, FRACTAL_RIGID_MULTI_SIMPLEX, FLAT, CELLULAR, CELLULAR_BILINEAR, CELLULAR_BILINEAR_STARCAST_3, CELLULAR_BILINEAR_STARCAST_6, CELLULAR_BILINEAR_STARCAST_9, CELLULAR_BILINEAR_STARCAST_12, CELLULAR_BICUBIC, CELLULAR_HERMITE, CELLULAR_STARCAST_3, CELLULAR_STARCAST_6, CELLULAR_STARCAST_9, CELLULAR_STARCAST_12, CELLULAR_HERMITE_STARCAST_3, CELLULAR_HERMITE_STARCAST_6, CELLULAR_HERMITE_STARCAST_9, CELLULAR_HERMITE_STARCAST_12, GLOB, CUBIC, FRACTAL_CUBIC, CELLULAR_HEIGHT, CLOVER, CLOVER_BILINEAR, CLOVER_BILINEAR_STARCAST_3, CLOVER_BILINEAR_STARCAST_6, CLOVER_BILINEAR_STARCAST_9, CLOVER_BILINEAR_STARCAST_12, CLOVER_BICUBIC, CLOVER_HERMITE, CLOVER_STARCAST_3, CLOVER_STARCAST_6, CLOVER_STARCAST_9, CLOVER_STARCAST_12, CLOVER_HERMITE_STARCAST_3, CLOVER_HERMITE_STARCAST_6, CLOVER_HERMITE_STARCAST_9, CLOVER_HERMITE_STARCAST_12, VASCULAR};
   }
}
