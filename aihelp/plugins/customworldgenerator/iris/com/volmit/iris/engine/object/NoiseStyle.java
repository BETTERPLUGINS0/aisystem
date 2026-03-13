package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import com.volmit.iris.util.noise.CNGFactory;
import com.volmit.iris.util.noise.NoiseType;
import com.volmit.iris.util.stream.ProceduralStream;

@Desc("Styles of noise")
public enum NoiseStyle {
   @Desc("White Noise is like static. Useful for block scattering but not terrain.")
   STATIC((var0) -> {
      return new CNG(var0, NoiseType.WHITE, 1.0D, 1);
   }),
   @Desc("White Noise is like static. Useful for block scattering but not terrain.")
   STATIC_BILINEAR((var0) -> {
      return new CNG(var0, NoiseType.WHITE_BILINEAR, 1.0D, 1);
   }),
   @Desc("White Noise is like static. Useful for block scattering but not terrain.")
   STATIC_BICUBIC((var0) -> {
      return new CNG(var0, NoiseType.WHITE_BICUBIC, 1.0D, 1);
   }),
   @Desc("White Noise is like static. Useful for block scattering but not terrain.")
   STATIC_HERMITE((var0) -> {
      return new CNG(var0, NoiseType.WHITE_HERMITE, 1.0D, 1);
   }),
   @Desc("Wispy Perlin-looking simplex noise. The 'iris' style noise.")
   IRIS((var0) -> {
      return CNG.signature(var0).scale(1.0D);
   }),
   @Desc("Clover Noise")
   CLOVER((var0) -> {
      return (new CNG(var0, NoiseType.CLOVER, 1.0D, 1)).scale(0.06D).bake();
   }),
   @Desc("CLOVER noise creates the same noise level for cells, changes noise level on cell borders.")
   CLOVER_STARCAST_3((var0) -> {
      return new CNG(var0, NoiseType.CLOVER_STARCAST_3, 1.0D, 1);
   }),
   @Desc("CLOVER noise creates the same noise level for cells, changes noise level on cell borders.")
   CLOVER_STARCAST_6((var0) -> {
      return new CNG(var0, NoiseType.CLOVER_STARCAST_6, 1.0D, 1);
   }),
   @Desc("CLOVER noise creates the same noise level for cells, changes noise level on cell borders.")
   CLOVER_STARCAST_9((var0) -> {
      return new CNG(var0, NoiseType.CLOVER_STARCAST_9, 1.0D, 1);
   }),
   @Desc("CLOVER noise creates the same noise level for cells, changes noise level on cell borders.")
   CLOVER_STARCAST_12((var0) -> {
      return new CNG(var0, NoiseType.CLOVER_STARCAST_12, 1.0D, 1);
   }),
   @Desc("CLOVER noise creates the same noise level for cells, changes noise level on cell borders.")
   CLOVER_BILINEAR_STARCAST_3((var0) -> {
      return new CNG(var0, NoiseType.CLOVER_BILINEAR_STARCAST_3, 1.0D, 1);
   }),
   @Desc("CLOVER noise creates the same noise level for cells, changes noise level on cell borders.")
   CLOVER_BILINEAR_STARCAST_6((var0) -> {
      return new CNG(var0, NoiseType.CLOVER_BILINEAR_STARCAST_6, 1.0D, 1);
   }),
   @Desc("CLOVER noise creates the same noise level for cells, changes noise level on cell borders.")
   CLOVER_BILINEAR_STARCAST_9((var0) -> {
      return new CNG(var0, NoiseType.CLOVER_BILINEAR_STARCAST_9, 1.0D, 1);
   }),
   @Desc("CLOVER noise creates the same noise level for cells, changes noise level on cell borders.")
   CLOVER_BILINEAR_STARCAST_12((var0) -> {
      return new CNG(var0, NoiseType.CLOVER_BILINEAR_STARCAST_12, 1.0D, 1);
   }),
   @Desc("CLOVER noise creates the same noise level for cells, changes noise level on cell borders.")
   CLOVER_HERMITE_STARCAST_3((var0) -> {
      return new CNG(var0, NoiseType.CLOVER_HERMITE_STARCAST_3, 1.0D, 1);
   }),
   @Desc("CLOVER noise creates the same noise level for cells, changes noise level on cell borders.")
   CLOVER_HERMITE_STARCAST_6((var0) -> {
      return new CNG(var0, NoiseType.CLOVER_HERMITE_STARCAST_6, 1.0D, 1);
   }),
   @Desc("CLOVER noise creates the same noise level for cells, changes noise level on cell borders.")
   CLOVER_HERMITE_STARCAST_9((var0) -> {
      return new CNG(var0, NoiseType.CLOVER_HERMITE_STARCAST_9, 1.0D, 1);
   }),
   @Desc("CLOVER noise creates the same noise level for cells, changes noise level on cell borders.")
   CLOVER_HERMITE_STARCAST_12((var0) -> {
      return new CNG(var0, NoiseType.CLOVER_HERMITE_STARCAST_12, 1.0D, 1);
   }),
   @Desc("CLOVER noise creates the same noise level for cells, changes noise level on cell borders.")
   CLOVER_BILINEAR((var0) -> {
      return new CNG(var0, NoiseType.CLOVER_BILINEAR, 1.0D, 1);
   }),
   @Desc("CLOVER noise creates the same noise level for cells, changes noise level on cell borders.")
   CLOVER_BICUBIC((var0) -> {
      return new CNG(var0, NoiseType.CLOVER_BICUBIC, 1.0D, 1);
   }),
   @Desc("CLOVER noise creates the same noise level for cells, changes noise level on cell borders.")
   CLOVER_HERMITE((var0) -> {
      return new CNG(var0, NoiseType.CLOVER_HERMITE, 1.0D, 1);
   }),
   @Desc("Vascular noise gets higher as the position nears a cell border.")
   VASCULAR((var0) -> {
      return new CNG(var0, NoiseType.VASCULAR, 1.0D, 1);
   }),
   @Desc("It always returns 0.5")
   FLAT((var0) -> {
      return new CNG(var0, NoiseType.FLAT, 1.0D, 1);
   }),
   @Desc("Cellular noise creates the same noise level for cells, changes noise level on cell borders.")
   CELLULAR((var0) -> {
      return new CNG(var0, NoiseType.CELLULAR, 1.0D, 1);
   }),
   @Desc("Cellular noise creates the same noise level for cells, changes noise level on cell borders.")
   CELLULAR_STARCAST_3((var0) -> {
      return new CNG(var0, NoiseType.CELLULAR_STARCAST_3, 1.0D, 1);
   }),
   @Desc("Cellular noise creates the same noise level for cells, changes noise level on cell borders.")
   CELLULAR_STARCAST_6((var0) -> {
      return new CNG(var0, NoiseType.CELLULAR_STARCAST_6, 1.0D, 1);
   }),
   @Desc("Cellular noise creates the same noise level for cells, changes noise level on cell borders.")
   CELLULAR_STARCAST_9((var0) -> {
      return new CNG(var0, NoiseType.CELLULAR_STARCAST_9, 1.0D, 1);
   }),
   @Desc("Cellular noise creates the same noise level for cells, changes noise level on cell borders.")
   CELLULAR_STARCAST_12((var0) -> {
      return new CNG(var0, NoiseType.CELLULAR_STARCAST_12, 1.0D, 1);
   }),
   @Desc("Cellular noise creates the same noise level for cells, changes noise level on cell borders.")
   CELLULAR_BILINEAR_STARCAST_3((var0) -> {
      return new CNG(var0, NoiseType.CELLULAR_BILINEAR_STARCAST_3, 1.0D, 1);
   }),
   @Desc("Cellular noise creates the same noise level for cells, changes noise level on cell borders.")
   CELLULAR_BILINEAR_STARCAST_6((var0) -> {
      return new CNG(var0, NoiseType.CELLULAR_BILINEAR_STARCAST_6, 1.0D, 1);
   }),
   @Desc("Cellular noise creates the same noise level for cells, changes noise level on cell borders.")
   CELLULAR_BILINEAR_STARCAST_9((var0) -> {
      return new CNG(var0, NoiseType.CELLULAR_BILINEAR_STARCAST_9, 1.0D, 1);
   }),
   @Desc("Cellular noise creates the same noise level for cells, changes noise level on cell borders.")
   CELLULAR_BILINEAR_STARCAST_12((var0) -> {
      return new CNG(var0, NoiseType.CELLULAR_BILINEAR_STARCAST_12, 1.0D, 1);
   }),
   @Desc("Cellular noise creates the same noise level for cells, changes noise level on cell borders.")
   CELLULAR_HERMITE_STARCAST_3((var0) -> {
      return new CNG(var0, NoiseType.CELLULAR_HERMITE_STARCAST_3, 1.0D, 1);
   }),
   @Desc("Cellular noise creates the same noise level for cells, changes noise level on cell borders.")
   CELLULAR_HERMITE_STARCAST_6((var0) -> {
      return new CNG(var0, NoiseType.CELLULAR_HERMITE_STARCAST_6, 1.0D, 1);
   }),
   @Desc("Cellular noise creates the same noise level for cells, changes noise level on cell borders.")
   CELLULAR_HERMITE_STARCAST_9((var0) -> {
      return new CNG(var0, NoiseType.CELLULAR_HERMITE_STARCAST_9, 1.0D, 1);
   }),
   @Desc("Cellular noise creates the same noise level for cells, changes noise level on cell borders.")
   CELLULAR_HERMITE_STARCAST_12((var0) -> {
      return new CNG(var0, NoiseType.CELLULAR_HERMITE_STARCAST_12, 1.0D, 1);
   }),
   @Desc("Cellular noise creates the same noise level for cells, changes noise level on cell borders.")
   CELLULAR_BILINEAR((var0) -> {
      return new CNG(var0, NoiseType.CELLULAR_BILINEAR, 1.0D, 1);
   }),
   @Desc("Cellular noise creates the same noise level for cells, changes noise level on cell borders.")
   CELLULAR_BICUBIC((var0) -> {
      return new CNG(var0, NoiseType.CELLULAR_BICUBIC, 1.0D, 1);
   }),
   @Desc("Cellular noise creates the same noise level for cells, changes noise level on cell borders.")
   CELLULAR_HERMITE((var0) -> {
      return new CNG(var0, NoiseType.CELLULAR_HERMITE, 1.0D, 1);
   }),
   @Desc("Classic German Engineering")
   NOWHERE((var0) -> {
      return CNG.signaturePerlin(var0).scale(0.776D).bake();
   }),
   @Desc("Classic German Engineering")
   NOWHERE_CELLULAR((var0) -> {
      return CNG.signaturePerlin(var0, NoiseType.CELLULAR).scale(1.0D).bake();
   }),
   @Desc("Classic German Engineering")
   NOWHERE_CLOVER((var0) -> {
      return CNG.signaturePerlin(var0, NoiseType.CLOVER).scale(1.0D).bake();
   }),
   @Desc("Classic German Engineering")
   NOWHERE_SIMPLEX((var0) -> {
      return CNG.signaturePerlin(var0, NoiseType.SIMPLEX).scale(1.0D).bake();
   }),
   @Desc("Classic German Engineering")
   NOWHERE_GLOB((var0) -> {
      return CNG.signaturePerlin(var0, NoiseType.GLOB).scale(1.0D).bake();
   }),
   @Desc("Classic German Engineering")
   NOWHERE_VASCULAR((var0) -> {
      return CNG.signaturePerlin(var0, NoiseType.VASCULAR).scale(1.0D).bake();
   }),
   @Desc("Classic German Engineering")
   NOWHERE_CUBIC((var0) -> {
      return CNG.signaturePerlin(var0, NoiseType.CUBIC).scale(1.0D).bake();
   }),
   @Desc("Classic German Engineering")
   NOWHERE_SUPERFRACTAL((var0) -> {
      return CNG.signaturePerlin(var0, NoiseType.FRACTAL_RIGID_MULTI_SIMPLEX).scale(1.0D).bake();
   }),
   @Desc("Classic German Engineering")
   NOWHERE_FRACTAL((var0) -> {
      return CNG.signaturePerlin(var0, NoiseType.FRACTAL_BILLOW_PERLIN).scale(1.0D).bake();
   }),
   @Desc("Wispy Perlin-looking simplex noise. The 'iris' style noise.")
   IRIS_DOUBLE((var0) -> {
      return CNG.signatureDouble(var0).scale(1.0D);
   }),
   @Desc("Wispy Perlin-looking simplex noise. The 'iris' style noise.")
   IRIS_THICK((var0) -> {
      return CNG.signatureThick(var0).scale(1.0D);
   }),
   @Desc("Wispy Perlin-looking simplex noise. The 'iris' style noise.")
   IRIS_HALF((var0) -> {
      return CNG.signatureHalf(var0).scale(1.0D);
   }),
   @Desc("Basic, Smooth & Fast Simplex noise.")
   SIMPLEX((var0) -> {
      return (new CNG(var0, 1.0D, 1)).scale(1.0D);
   }),
   @Desc("Very Detailed smoke using simplex fractured with fractal billow simplex at high octaves.")
   FRACTAL_SMOKE((var0) -> {
      return (new CNG(var0, 1.0D, 1)).fractureWith((new CNG(var0.nextParallelRNG(1), NoiseType.FRACTAL_BILLOW_SIMPLEX, 1.0D, 8)).scale(0.2D), 1000.0D).scale(0.34D);
   }),
   @Desc("Thinner Veins.")
   VASCULAR_THIN((var0) -> {
      return (new CNG(var0.nextParallelRNG(1), NoiseType.VASCULAR, 1.0D, 1)).scale(1.0D).pow(0.65D);
   }),
   @Desc("Cells of simplex noise")
   SIMPLEX_CELLS((var0) -> {
      return (new CNG(var0.nextParallelRNG(1), NoiseType.SIMPLEX, 1.0D, 1)).scale(1.0D).fractureWith((new CNG(var0.nextParallelRNG(8), NoiseType.CELLULAR, 1.0D, 1)).scale(1.0D), 200.0D);
   }),
   @Desc("Veins of simplex noise")
   SIMPLEX_VASCULAR((var0) -> {
      return (new CNG(var0.nextParallelRNG(1), NoiseType.SIMPLEX, 1.0D, 1)).scale(1.0D).fractureWith((new CNG(var0.nextParallelRNG(8), NoiseType.VASCULAR, 1.0D, 1)).scale(1.0D), 200.0D);
   }),
   @Desc("Very Detailed fluid using simplex fractured with fractal billow simplex at high octaves.")
   FRACTAL_WATER((var0) -> {
      return (new CNG(var0, 1.0D, 1)).fractureWith((new CNG(var0.nextParallelRNG(1), NoiseType.FRACTAL_FBM_SIMPLEX, 1.0D, 9)).scale(0.03D), 9900.0D).scale(1.14D);
   }),
   @Desc("Perlin. Like simplex but more natural")
   PERLIN((var0) -> {
      return (new CNG(var0, NoiseType.PERLIN, 1.0D, 1)).scale(1.15D);
   }),
   @Desc("Perlin. Like simplex but more natural")
   PERLIN_IRIS((var0) -> {
      return CNG.signature(var0, NoiseType.PERLIN).scale(1.47D);
   }),
   @Desc("Perlin. Like simplex but more natural")
   PERLIN_IRIS_HALF((var0) -> {
      return CNG.signatureHalf(var0, NoiseType.PERLIN).scale(1.47D);
   }),
   @Desc("Perlin. Like simplex but more natural")
   PERLIN_IRIS_DOUBLE((var0) -> {
      return CNG.signatureDouble(var0, NoiseType.PERLIN).scale(1.47D);
   }),
   @Desc("Perlin. Like simplex but more natural")
   PERLIN_IRIS_THICK((var0) -> {
      return CNG.signatureThick(var0, NoiseType.PERLIN).scale(1.47D);
   }),
   @Desc("Billow Fractal Perlin Noise.")
   FRACTAL_BILLOW_PERLIN((var0) -> {
      return (new CNG(var0, NoiseType.FRACTAL_BILLOW_PERLIN, 1.0D, 1)).scale(1.47D);
   }),
   @Desc("Billow Fractal Perlin Noise. 2 Octaves")
   BIOCTAVE_FRACTAL_BILLOW_PERLIN((var0) -> {
      return (new CNG(var0, NoiseType.FRACTAL_BILLOW_PERLIN, 1.0D, 2)).scale(1.17D);
   }),
   @Desc("Billow Fractal Simplex Noise. Single octave.")
   FRACTAL_BILLOW_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_BILLOW_SIMPLEX, 1.0D, 1);
   }),
   @Desc("FBM Fractal Simplex Noise. Single octave.")
   FRACTAL_FBM_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_FBM_SIMPLEX, 1.0D, 1);
   }),
   @Desc("Billow Fractal Iris Noise. Single octave.")
   FRACTAL_BILLOW_IRIS((var0) -> {
      return CNG.signature(var0, NoiseType.FRACTAL_BILLOW_SIMPLEX);
   }),
   @Desc("FBM Fractal Iris Noise. Single octave.")
   FRACTAL_FBM_IRIS((var0) -> {
      return CNG.signature(var0, NoiseType.FRACTAL_FBM_SIMPLEX);
   }),
   @Desc("Billow Fractal Iris Noise. Single octave.")
   FRACTAL_BILLOW_IRIS_HALF((var0) -> {
      return CNG.signatureHalf(var0, NoiseType.FRACTAL_BILLOW_SIMPLEX);
   }),
   @Desc("FBM Fractal Iris Noise. Single octave.")
   FRACTAL_FBM_IRIS_HALF((var0) -> {
      return CNG.signatureHalf(var0, NoiseType.FRACTAL_FBM_SIMPLEX);
   }),
   @Desc("Billow Fractal Iris Noise. Single octave.")
   FRACTAL_BILLOW_IRIS_THICK((var0) -> {
      return CNG.signatureThick(var0, NoiseType.FRACTAL_BILLOW_SIMPLEX);
   }),
   @Desc("FBM Fractal Iris Noise. Single octave.")
   FRACTAL_FBM_IRIS_THICK((var0) -> {
      return CNG.signatureThick(var0, NoiseType.FRACTAL_FBM_SIMPLEX);
   }),
   @Desc("Rigid Multi Fractal Simplex Noise. Single octave.")
   FRACTAL_RM_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_RIGID_MULTI_SIMPLEX, 1.0D, 1);
   }),
   @Desc("Billow Fractal Simplex Noise. 2 octaves.")
   BIOCTAVE_FRACTAL_BILLOW_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_BILLOW_SIMPLEX, 1.0D, 2);
   }),
   @Desc("FBM Fractal Simplex Noise. 2 octaves.")
   BIOCTAVE_FRACTAL_FBM_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_FBM_SIMPLEX, 1.0D, 2);
   }),
   @Desc("Rigid Multi Fractal Simplex Noise. 2 octaves.")
   BIOCTAVE_FRACTAL_RM_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_RIGID_MULTI_SIMPLEX, 1.0D, 2);
   }),
   @Desc("Rigid Multi Fractal Simplex Noise. 3 octaves.")
   TRIOCTAVE_FRACTAL_RM_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_RIGID_MULTI_SIMPLEX, 1.0D, 3);
   }),
   @Desc("Billow Fractal Simplex Noise. 3 octaves.")
   TRIOCTAVE_FRACTAL_BILLOW_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_BILLOW_SIMPLEX, 1.0D, 3);
   }),
   @Desc("FBM Fractal Simplex Noise. 3 octaves.")
   TRIOCTAVE_FRACTAL_FBM_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_FBM_SIMPLEX, 1.0D, 3);
   }),
   @Desc("Rigid Multi Fractal Simplex Noise. 4 octaves.")
   QUADOCTAVE_FRACTAL_RM_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_RIGID_MULTI_SIMPLEX, 1.0D, 4);
   }),
   @Desc("Billow Fractal Simplex Noise. 4 octaves.")
   QUADOCTAVE_FRACTAL_BILLOW_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_BILLOW_SIMPLEX, 1.0D, 4);
   }),
   @Desc("FBM Fractal Simplex Noise. 4 octaves.")
   QUADOCTAVE_FRACTAL_FBM_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_FBM_SIMPLEX, 1.0D, 4);
   }),
   @Desc("Rigid Multi Fractal Simplex Noise. 5 octaves.")
   QUINTOCTAVE_FRACTAL_RM_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_RIGID_MULTI_SIMPLEX, 1.0D, 5);
   }),
   @Desc("Billow Fractal Simplex Noise. 5 octaves.")
   QUINTOCTAVE_FRACTAL_BILLOW_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_BILLOW_SIMPLEX, 1.0D, 5);
   }),
   @Desc("FBM Fractal Simplex Noise. 5 octaves.")
   QUINTOCTAVE_FRACTAL_FBM_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_FBM_SIMPLEX, 1.0D, 5);
   }),
   @Desc("Rigid Multi Fractal Simplex Noise. 6 octaves.")
   SEXOCTAVE_FRACTAL_RM_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_RIGID_MULTI_SIMPLEX, 1.0D, 6);
   }),
   @Desc("Billow Fractal Simplex Noise. 6 octaves.")
   SEXOCTAVE_FRACTAL_BILLOW_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_BILLOW_SIMPLEX, 1.0D, 6);
   }),
   @Desc("FBM Fractal Simplex Noise. 6 octaves.")
   SEXOCTAVE_FRACTAL_FBM_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_FBM_SIMPLEX, 1.0D, 6);
   }),
   @Desc("Rigid Multi Fractal Simplex Noise. 7 octaves.")
   SEPTOCTAVE_FRACTAL_RM_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_RIGID_MULTI_SIMPLEX, 1.0D, 7);
   }),
   @Desc("Billow Fractal Simplex Noise. 7 octaves.")
   SEPTOCTAVE_FRACTAL_BILLOW_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_BILLOW_SIMPLEX, 1.0D, 7);
   }),
   @Desc("FBM Fractal Simplex Noise. 7 octaves.")
   SEPTOCTAVE_FRACTAL_FBM_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_FBM_SIMPLEX, 1.0D, 7);
   }),
   @Desc("Rigid Multi Fractal Simplex Noise. 8 octaves.")
   OCTOCTAVE_FRACTAL_RM_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_RIGID_MULTI_SIMPLEX, 1.0D, 8);
   }),
   @Desc("Billow Fractal Simplex Noise. 8 octaves.")
   OCTOCTAVE_FRACTAL_BILLOW_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_BILLOW_SIMPLEX, 1.0D, 8);
   }),
   @Desc("FBM Fractal Simplex Noise. 8 octaves.")
   OCTOCTAVE_FRACTAL_FBM_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_FBM_SIMPLEX, 1.0D, 8);
   }),
   @Desc("Rigid Multi Fractal Simplex Noise. 9 octaves.")
   NONOCTAVE_FRACTAL_RM_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_RIGID_MULTI_SIMPLEX, 1.0D, 9);
   }),
   @Desc("Billow Fractal Simplex Noise. 9 octaves.")
   NONOCTAVE_FRACTAL_BILLOW_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_BILLOW_SIMPLEX, 1.0D, 9);
   }),
   @Desc("FBM Fractal Simplex Noise. 9 octaves.")
   NONOCTAVE_FRACTAL_FBM_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_FBM_SIMPLEX, 1.0D, 9);
   }),
   @Desc("Rigid Multi Fractal Simplex Noise. 10 octaves.")
   VIGOCTAVE_FRACTAL_RM_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_RIGID_MULTI_SIMPLEX, 1.0D, 10);
   }),
   @Desc("Billow Fractal Simplex Noise. 10 octaves.")
   VIGOCTAVE_FRACTAL_BILLOW_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_BILLOW_SIMPLEX, 1.0D, 10);
   }),
   @Desc("FBM Fractal Simplex Noise. 10 octaves.")
   VIGOCTAVE_FRACTAL_FBM_SIMPLEX((var0) -> {
      return new CNG(var0, NoiseType.FRACTAL_FBM_SIMPLEX, 1.0D, 10);
   }),
   @Desc("Basic, Smooth & Fast Simplex noise. Uses 2 octaves")
   BIOCTAVE_SIMPLEX((var0) -> {
      return (new CNG(var0, 1.0D, 2)).scale(0.5D);
   }),
   @Desc("Basic, Smooth & Fast Simplex noise. Uses 3 octaves")
   TRIOCTAVE_SIMPLEX((var0) -> {
      return (new CNG(var0, 1.0D, 3)).scale(0.3333333333333333D);
   }),
   @Desc("Basic, Smooth & Fast Simplex noise. Uses 4 octaves")
   QUADOCTAVE_SIMPLEX((var0) -> {
      return (new CNG(var0, 1.0D, 4)).scale(0.25D);
   }),
   @Desc("Basic, Smooth & Fast Simplex noise. Uses 5 octaves")
   QUINTOCTAVE_SIMPLEX((var0) -> {
      return (new CNG(var0, 1.0D, 5)).scale(0.2D);
   }),
   @Desc("Basic, Smooth & Fast Simplex noise. Uses 6 octaves")
   SEXOCTAVE_SIMPLEX((var0) -> {
      return (new CNG(var0, 1.0D, 6)).scale(0.16666666666666666D);
   }),
   @Desc("Basic, Smooth & Fast Simplex noise. Uses 7 octaves")
   SEPTOCTAVE_SIMPLEX((var0) -> {
      return (new CNG(var0, 1.0D, 7)).scale(0.08333333333333333D);
   }),
   @Desc("Basic, Smooth & Fast Simplex noise. Uses 8 octaves")
   OCTOCTAVE_SIMPLEX((var0) -> {
      return (new CNG(var0, 1.0D, 8)).scale(0.04D);
   }),
   @Desc("Basic, Smooth & Fast Simplex noise. Uses 9 octaves")
   NONOCTAVE_SIMPLEX((var0) -> {
      return (new CNG(var0, 1.0D, 9)).scale(0.02D);
   }),
   @Desc("Basic, Smooth & Fast Simplex noise. Uses 10 octaves")
   VIGOCTAVE_SIMPLEX((var0) -> {
      return (new CNG(var0, 1.0D, 10)).scale(0.01D);
   }),
   @Desc("Glob noise is like cellular, but with globs...")
   GLOB((var0) -> {
      return new CNG(var0, NoiseType.GLOB, 1.0D, 1);
   }),
   @Desc("Glob noise is like cellular, but with globs...")
   GLOB_IRIS((var0) -> {
      return CNG.signature(var0, NoiseType.GLOB);
   }),
   @Desc("Glob noise is like cellular, but with globs...")
   GLOB_IRIS_HALF((var0) -> {
      return CNG.signatureHalf(var0, NoiseType.GLOB);
   }),
   @Desc("Glob noise is like cellular, but with globs...")
   GLOB_IRIS_DOUBLE((var0) -> {
      return CNG.signatureDouble(var0, NoiseType.GLOB);
   }),
   @Desc("Glob noise is like cellular, but with globs...")
   GLOB_IRIS_THICK((var0) -> {
      return CNG.signatureThick(var0, NoiseType.GLOB);
   }),
   @Desc("Cubic Noise")
   CUBIC((var0) -> {
      return (new CNG(var0, NoiseType.CUBIC, 1.0D, 1)).scale(256.0D);
   }),
   @Desc("Fractal Cubic Noise")
   FRACTAL_CUBIC((var0) -> {
      return (new CNG(var0, NoiseType.FRACTAL_CUBIC, 1.0D, 1)).scale(2.0D);
   }),
   @Desc("Fractal Cubic Noise With Iris Swirls")
   FRACTAL_CUBIC_IRIS((var0) -> {
      return CNG.signature(var0, NoiseType.FRACTAL_CUBIC).scale(2.0D);
   }),
   @Desc("Fractal Cubic Noise With Iris Swirls")
   FRACTAL_CUBIC_IRIS_THICK((var0) -> {
      return CNG.signatureThick(var0, NoiseType.FRACTAL_CUBIC).scale(2.0D);
   }),
   @Desc("Fractal Cubic Noise With Iris Swirls")
   FRACTAL_CUBIC_IRIS_HALF((var0) -> {
      return CNG.signatureHalf(var0, NoiseType.FRACTAL_CUBIC).scale(2.0D);
   }),
   @Desc("Fractal Cubic Noise With Iris Swirls")
   FRACTAL_CUBIC_IRIS_DOUBLE((var0) -> {
      return CNG.signatureDouble(var0, NoiseType.FRACTAL_CUBIC).scale(2.0D);
   }),
   @Desc("Fractal Cubic Noise, 2 Octaves")
   BIOCTAVE_FRACTAL_CUBIC((var0) -> {
      return (new CNG(var0, NoiseType.FRACTAL_CUBIC, 1.0D, 2)).scale(2.0D);
   }),
   @Desc("Fractal Cubic Noise, 3 Octaves")
   TRIOCTAVE_FRACTAL_CUBIC((var0) -> {
      return (new CNG(var0, NoiseType.FRACTAL_CUBIC, 1.0D, 3)).scale(1.5D);
   }),
   @Desc("Fractal Cubic Noise, 4 Octaves")
   QUADOCTAVE_FRACTAL_CUBIC((var0) -> {
      return (new CNG(var0, NoiseType.FRACTAL_CUBIC, 1.0D, 4)).scale(1.0D);
   }),
   @Desc("Cubic Noise")
   CUBIC_IRIS((var0) -> {
      return CNG.signature(var0, NoiseType.CUBIC).scale(256.0D);
   }),
   @Desc("Cubic Noise")
   CUBIC_IRIS_HALF((var0) -> {
      return CNG.signatureHalf(var0, NoiseType.CUBIC).scale(256.0D);
   }),
   @Desc("Cubic Noise")
   CUBIC_IRIS_DOUBLE((var0) -> {
      return CNG.signatureDouble(var0, NoiseType.CUBIC).scale(256.0D);
   }),
   @Desc("Cubic Noise")
   CUBIC_IRIS_THICK((var0) -> {
      return CNG.signatureThick(var0, NoiseType.CUBIC).scale(256.0D);
   }),
   @Desc("Cellular noise creates the same noise level for cells, changes noise level on cell borders. Cells are distorted using Iris styled wispy noise.")
   CELLULAR_IRIS((var0) -> {
      return CNG.signature(var0, NoiseType.CELLULAR);
   }),
   @Desc("Cellular noise creates the same noise level for cells, changes noise level on cell borders. Cells are distorted using Iris styled wispy noise.")
   CELLULAR_IRIS_THICK((var0) -> {
      return CNG.signatureThick(var0, NoiseType.CELLULAR);
   }),
   @Desc("Cellular noise creates the same noise level for cells, changes noise level on cell borders. Cells are distorted using Iris styled wispy noise.")
   CELLULAR_IRIS_DOUBLE((var0) -> {
      return CNG.signatureDouble(var0, NoiseType.CELLULAR);
   }),
   @Desc("Cellular noise creates the same noise level for cells, changes noise level on cell borders. Cells are distorted using Iris styled wispy noise.")
   CELLULAR_IRIS_HALF((var0) -> {
      return CNG.signatureHalf(var0, NoiseType.CELLULAR);
   }),
   @Desc("Inverse of vascular, height gets to 1.0 as it approaches the center of a cell")
   CELLULAR_HEIGHT((var0) -> {
      return new CNG(var0, NoiseType.CELLULAR_HEIGHT, 1.0D, 1);
   }),
   @Desc("Inverse of vascular, height gets to 1.0 as it approaches the center of a cell, using the iris style.")
   CELLULAR_HEIGHT_IRIS((var0) -> {
      return CNG.signature(var0, NoiseType.CELLULAR_HEIGHT);
   }),
   @Desc("Inverse of vascular, height gets to 1.0 as it approaches the center of a cell, using the iris style.")
   CELLULAR_HEIGHT_IRIS_DOUBLE((var0) -> {
      return CNG.signatureDouble(var0, NoiseType.CELLULAR_HEIGHT);
   }),
   @Desc("Inverse of vascular, height gets to 1.0 as it approaches the center of a cell, using the iris style.")
   CELLULAR_HEIGHT_IRIS_THICK((var0) -> {
      return CNG.signatureThick(var0, NoiseType.CELLULAR_HEIGHT);
   }),
   @Desc("Inverse of vascular, height gets to 1.0 as it approaches the center of a cell, using the iris style.")
   CELLULAR_HEIGHT_IRIS_HALF((var0) -> {
      return CNG.signatureHalf(var0, NoiseType.CELLULAR_HEIGHT);
   }),
   @Desc("Vascular noise gets higher as the position nears a cell border. Cells are distorted using Iris styled wispy noise.")
   VASCULAR_IRIS((var0) -> {
      return CNG.signature(var0, NoiseType.VASCULAR);
   }),
   @Desc("Vascular noise gets higher as the position nears a cell border. Cells are distorted using Iris styled wispy noise.")
   VASCULAR_IRIS_DOUBLE((var0) -> {
      return CNG.signatureDouble(var0, NoiseType.VASCULAR);
   }),
   @Desc("Vascular noise gets higher as the position nears a cell border. Cells are distorted using Iris styled wispy noise.")
   VASCULAR_IRIS_THICK((var0) -> {
      return CNG.signatureThick(var0, NoiseType.VASCULAR);
   }),
   @Desc("Vascular noise gets higher as the position nears a cell border. Cells are distorted using Iris styled wispy noise.")
   VASCULAR_IRIS_HALF((var0) -> {
      return CNG.signatureHalf(var0, NoiseType.VASCULAR);
   });

   private final CNGFactory f;

   private NoiseStyle(CNGFactory f) {
      this.f = var3;
   }

   public ProceduralStream<Double> stream(RNG seed) {
      return this.create(var1).stream();
   }

   public ProceduralStream<Double> stream(long seed) {
      return this.create(new RNG(var1)).stream();
   }

   public CNG create(RNG seed) {
      CNG var2 = this.f.create(var1).bake();
      var2.setLeakStyle(this);
      return var2;
   }

   public IrisGeneratorStyle style() {
      return new IrisGeneratorStyle(this);
   }

   // $FF: synthetic method
   private static NoiseStyle[] $values() {
      return new NoiseStyle[]{STATIC, STATIC_BILINEAR, STATIC_BICUBIC, STATIC_HERMITE, IRIS, CLOVER, CLOVER_STARCAST_3, CLOVER_STARCAST_6, CLOVER_STARCAST_9, CLOVER_STARCAST_12, CLOVER_BILINEAR_STARCAST_3, CLOVER_BILINEAR_STARCAST_6, CLOVER_BILINEAR_STARCAST_9, CLOVER_BILINEAR_STARCAST_12, CLOVER_HERMITE_STARCAST_3, CLOVER_HERMITE_STARCAST_6, CLOVER_HERMITE_STARCAST_9, CLOVER_HERMITE_STARCAST_12, CLOVER_BILINEAR, CLOVER_BICUBIC, CLOVER_HERMITE, VASCULAR, FLAT, CELLULAR, CELLULAR_STARCAST_3, CELLULAR_STARCAST_6, CELLULAR_STARCAST_9, CELLULAR_STARCAST_12, CELLULAR_BILINEAR_STARCAST_3, CELLULAR_BILINEAR_STARCAST_6, CELLULAR_BILINEAR_STARCAST_9, CELLULAR_BILINEAR_STARCAST_12, CELLULAR_HERMITE_STARCAST_3, CELLULAR_HERMITE_STARCAST_6, CELLULAR_HERMITE_STARCAST_9, CELLULAR_HERMITE_STARCAST_12, CELLULAR_BILINEAR, CELLULAR_BICUBIC, CELLULAR_HERMITE, NOWHERE, NOWHERE_CELLULAR, NOWHERE_CLOVER, NOWHERE_SIMPLEX, NOWHERE_GLOB, NOWHERE_VASCULAR, NOWHERE_CUBIC, NOWHERE_SUPERFRACTAL, NOWHERE_FRACTAL, IRIS_DOUBLE, IRIS_THICK, IRIS_HALF, SIMPLEX, FRACTAL_SMOKE, VASCULAR_THIN, SIMPLEX_CELLS, SIMPLEX_VASCULAR, FRACTAL_WATER, PERLIN, PERLIN_IRIS, PERLIN_IRIS_HALF, PERLIN_IRIS_DOUBLE, PERLIN_IRIS_THICK, FRACTAL_BILLOW_PERLIN, BIOCTAVE_FRACTAL_BILLOW_PERLIN, FRACTAL_BILLOW_SIMPLEX, FRACTAL_FBM_SIMPLEX, FRACTAL_BILLOW_IRIS, FRACTAL_FBM_IRIS, FRACTAL_BILLOW_IRIS_HALF, FRACTAL_FBM_IRIS_HALF, FRACTAL_BILLOW_IRIS_THICK, FRACTAL_FBM_IRIS_THICK, FRACTAL_RM_SIMPLEX, BIOCTAVE_FRACTAL_BILLOW_SIMPLEX, BIOCTAVE_FRACTAL_FBM_SIMPLEX, BIOCTAVE_FRACTAL_RM_SIMPLEX, TRIOCTAVE_FRACTAL_RM_SIMPLEX, TRIOCTAVE_FRACTAL_BILLOW_SIMPLEX, TRIOCTAVE_FRACTAL_FBM_SIMPLEX, QUADOCTAVE_FRACTAL_RM_SIMPLEX, QUADOCTAVE_FRACTAL_BILLOW_SIMPLEX, QUADOCTAVE_FRACTAL_FBM_SIMPLEX, QUINTOCTAVE_FRACTAL_RM_SIMPLEX, QUINTOCTAVE_FRACTAL_BILLOW_SIMPLEX, QUINTOCTAVE_FRACTAL_FBM_SIMPLEX, SEXOCTAVE_FRACTAL_RM_SIMPLEX, SEXOCTAVE_FRACTAL_BILLOW_SIMPLEX, SEXOCTAVE_FRACTAL_FBM_SIMPLEX, SEPTOCTAVE_FRACTAL_RM_SIMPLEX, SEPTOCTAVE_FRACTAL_BILLOW_SIMPLEX, SEPTOCTAVE_FRACTAL_FBM_SIMPLEX, OCTOCTAVE_FRACTAL_RM_SIMPLEX, OCTOCTAVE_FRACTAL_BILLOW_SIMPLEX, OCTOCTAVE_FRACTAL_FBM_SIMPLEX, NONOCTAVE_FRACTAL_RM_SIMPLEX, NONOCTAVE_FRACTAL_BILLOW_SIMPLEX, NONOCTAVE_FRACTAL_FBM_SIMPLEX, VIGOCTAVE_FRACTAL_RM_SIMPLEX, VIGOCTAVE_FRACTAL_BILLOW_SIMPLEX, VIGOCTAVE_FRACTAL_FBM_SIMPLEX, BIOCTAVE_SIMPLEX, TRIOCTAVE_SIMPLEX, QUADOCTAVE_SIMPLEX, QUINTOCTAVE_SIMPLEX, SEXOCTAVE_SIMPLEX, SEPTOCTAVE_SIMPLEX, OCTOCTAVE_SIMPLEX, NONOCTAVE_SIMPLEX, VIGOCTAVE_SIMPLEX, GLOB, GLOB_IRIS, GLOB_IRIS_HALF, GLOB_IRIS_DOUBLE, GLOB_IRIS_THICK, CUBIC, FRACTAL_CUBIC, FRACTAL_CUBIC_IRIS, FRACTAL_CUBIC_IRIS_THICK, FRACTAL_CUBIC_IRIS_HALF, FRACTAL_CUBIC_IRIS_DOUBLE, BIOCTAVE_FRACTAL_CUBIC, TRIOCTAVE_FRACTAL_CUBIC, QUADOCTAVE_FRACTAL_CUBIC, CUBIC_IRIS, CUBIC_IRIS_HALF, CUBIC_IRIS_DOUBLE, CUBIC_IRIS_THICK, CELLULAR_IRIS, CELLULAR_IRIS_THICK, CELLULAR_IRIS_DOUBLE, CELLULAR_IRIS_HALF, CELLULAR_HEIGHT, CELLULAR_HEIGHT_IRIS, CELLULAR_HEIGHT_IRIS_DOUBLE, CELLULAR_HEIGHT_IRIS_THICK, CELLULAR_HEIGHT_IRIS_HALF, VASCULAR_IRIS, VASCULAR_IRIS_DOUBLE, VASCULAR_IRIS_THICK, VASCULAR_IRIS_HALF};
   }
}
