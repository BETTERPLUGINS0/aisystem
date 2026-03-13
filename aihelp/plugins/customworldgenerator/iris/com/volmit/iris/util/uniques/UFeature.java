package com.volmit.iris.util.uniques;

import com.volmit.iris.engine.object.NoiseStyle;
import com.volmit.iris.util.function.NoiseInjector;
import com.volmit.iris.util.interpolation.InterpolationMethod;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import java.awt.Color;
import java.util.List;
import java.util.function.Consumer;

public interface UFeature {
   List<NoiseInjector> injectors = List.of(CNG.ADD, CNG.DST_MOD, CNG.DST_POW, CNG.DST_SUBTRACT, CNG.MAX, CNG.MIN, CNG.SRC_MOD, CNG.SRC_POW, CNG.SRC_SUBTRACT, CNG.MULTIPLY);

   void render(UImage image, RNG rng, double time, Consumer<Double> progressor, UFeatureMeta meta);

   default Color color(CNG hue, CNG saturation, CNG brightness, double x, double y, double t) {
      return Color.getHSBColor((float)hue.fitDouble(0.0D, 1.0D, x + t, y + t), (float)saturation.fitDouble(0.0D, 1.0D, x + t, y + t), (float)brightness.fitDouble(0.0D, 1.0D, x + t, y + t));
   }

   default InterpolationMethod interpolator(RNG rng) {
      return (InterpolationMethod)rng.pick(UniqueRenderer.renderer.getInterpolators());
   }

   default CNG generator(String key, RNG rng, double scaleMod, long salt, UFeatureMeta meta) {
      return this.generator(key, rng, scaleMod, rng.i(1, 3), rng.i(1, 5), salt, meta);
   }

   default CNG generator(String key, RNG rng, double scaleMod, int fractures, int composites, long salt, UFeatureMeta meta) {
      RNG rngg = rng.nextParallelRNG(salt);
      CNG cng = ((NoiseStyle)rng.pick(UniqueRenderer.renderer.getStyles())).create(rngg).oct(rng.i(1, 5));
      RNG rngf = rngg.nextParallelRNG(-salt);
      cng.scale(rngf.d(0.33D * scaleMod, 1.66D * scaleMod));
      if (fractures > 0) {
         cng.fractureWith(this.generator((String)null, rngf.nextParallelRNG(salt + (long)fractures), scaleMod / rng.d(4.0D, 17.0D), fractures - 1, composites, salt + (long)fractures + 55L, (UFeatureMeta)null), scaleMod * rngf.nextDouble(16.0D, 256.0D));
      }

      for(int i = 0; i < composites; ++i) {
         CNG sub = this.generator((String)null, rngf.nextParallelRNG(salt + (long)fractures), scaleMod * rngf.d(0.4D, 3.3D), fractures / 3, 0, salt + (long)fractures + (long)composites + 78L, (UFeatureMeta)null);
         sub.setInjector((NoiseInjector)rng.pick(injectors));
         cng.child(sub);
      }

      if (key != null && meta != null) {
         meta.registerGenerator(key, cng);
      }

      return cng;
   }
}
