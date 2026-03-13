package org.terraform.cave;

import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.data.TerraformWorld;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class NoiseRavine extends NoiseCaveAbstract {
   private static final int RAVINE_DEPTH = 50;

   public boolean canCarve(@NotNull TerraformWorld tw, int rawX, int y, int rawZ, double height, float filter) {
      if (height < (double)TerraformGenerator.seaLevel) {
         return false;
      } else if ((double)y < height - 50.0D) {
         return false;
      } else {
         FastNoise ravineNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.CAVE_XRAVINE_NOISE, (world) -> {
            FastNoise n = new FastNoise(tw.getHashedRand(458930L, 16328, 54981).nextInt());
            n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
            n.SetFrequency(0.007F);
            n.SetFractalOctaves(3);
            return n;
         });
         float ravine = ravineNoise.GetNoise((float)(3 * rawX), (float)y * 0.4F, (float)(3 * rawZ));
         ravine *= (float)((double)filter * 0.5885D * Math.log(51.0D - (height - (double)y)));
         return ravine <= -1.3F;
      }
   }
}
