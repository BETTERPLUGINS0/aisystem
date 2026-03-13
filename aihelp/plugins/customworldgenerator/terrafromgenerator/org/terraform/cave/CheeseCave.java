package org.terraform.cave;

import org.jetbrains.annotations.NotNull;
import org.terraform.data.TerraformWorld;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class CheeseCave extends NoiseCaveAbstract {
   public boolean canCarve(@NotNull TerraformWorld tw, int rawX, int y, int rawZ, double height, float surfaceFilter) {
      FastNoise cheeseNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.CAVE_CHEESE_NOISE, (world) -> {
         FastNoise n = new FastNoise((int)(tw.getSeed() + 723891L));
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFrequency(0.03F);
         n.SetFractalOctaves(2);
         return n;
      });
      float cheese = cheeseNoise.GetNoise((float)rawX * 0.5F, (float)y, (float)rawZ * 0.5F);
      return surfaceFilter * cheese <= -0.3F;
   }
}
