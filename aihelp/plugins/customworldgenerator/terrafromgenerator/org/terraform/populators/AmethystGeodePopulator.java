package org.terraform.populators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.DirectionalBuilder;
import org.terraform.utils.noise.FastNoise;

public class AmethystGeodePopulator {
   private final int geodeRadius;
   private final double frequency;
   private final int minDepth;
   private final int minDepthBelowSurface;

   public AmethystGeodePopulator(int geodeRadius, double frequency, int minDepth, int minDepthBelowSurface) {
      this.geodeRadius = geodeRadius;
      this.frequency = TConfig.c.FEATURE_ORES_ENABLED ? frequency : 0.0D;
      this.minDepth = minDepth;
      this.minDepthBelowSurface = minDepthBelowSurface;
   }

   public static void placeGeode(int seed, float r, @NotNull SimpleBlock block) {
      if (!(r <= 1.0F)) {
         ArrayList<SimpleBlock> amethystBlocks = new ArrayList();
         FastNoise noise = new FastNoise(seed);
         noise.SetNoiseType(FastNoise.NoiseType.Simplex);
         noise.SetFrequency(0.09F);

         for(float x = -r; x <= r; ++x) {
            for(float y = -r; y <= r; ++y) {
               for(float z = -r; z <= r; ++z) {
                  SimpleBlock rel = block.getRelative(Math.round(x), Math.round(y), Math.round(z));
                  double outerCrust = (Math.pow((double)x, 2.0D) + Math.pow((double)y, 2.0D) + Math.pow((double)z, 2.0D)) / Math.pow((double)r, 2.0D);
                  double innerCrust = (Math.pow((double)x, 2.0D) + Math.pow((double)y, 2.0D) + Math.pow((double)z, 2.0D)) / Math.pow((double)r - 1.0D, 2.0D);
                  double amethystCrust = (Math.pow((double)x, 2.0D) + Math.pow((double)y, 2.0D) + Math.pow((double)z, 2.0D)) / Math.pow((double)r - 2.2D, 2.0D);
                  double airHollower = (Math.pow((double)x, 2.0D) + Math.pow((double)y, 2.0D) + Math.pow((double)z, 2.0D)) / Math.pow((double)r - 3.3D, 2.0D);
                  double noiseVal = 1.0D + 0.4D * (double)noise.GetNoise((float)rel.getX(), (float)rel.getY(), (float)rel.getZ());
                  if (airHollower <= noiseVal) {
                     if (BlockUtils.isWet(rel)) {
                        rel.setType(Material.WATER);
                     } else {
                        rel.setType(Material.CAVE_AIR);
                     }
                  } else if (rel.isSolid()) {
                     if (amethystCrust <= noiseVal) {
                        rel.setType(Material.AMETHYST_BLOCK, Material.BUDDING_AMETHYST);
                        amethystBlocks.add(rel);
                     } else if (innerCrust <= noiseVal) {
                        rel.setType(Material.CALCITE);
                     } else if (outerCrust <= noiseVal) {
                        rel.setType(Material.SMOOTH_BASALT);
                     }
                  }
               }
            }
         }

         Iterator var19 = amethystBlocks.iterator();

         while(var19.hasNext()) {
            SimpleBlock rel = (SimpleBlock)var19.next();
            BlockFace[] var21 = BlockUtils.sixBlockFaces;
            int var22 = var21.length;

            for(int var23 = 0; var23 < var22; ++var23) {
               BlockFace face = var21[var23];
               if (!GenUtils.chance(1, 6)) {
                  SimpleBlock target = rel.getRelative(face);
                  if (BlockUtils.isAir(target.getType()) && GenUtils.chance(1, 6)) {
                     (new DirectionalBuilder(Material.AMETHYST_CLUSTER)).setFacing(face).apply(target);
                  }
               }
            }
         }

      }
   }

   public void populate(@NotNull TerraformWorld world, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      if (GenUtils.chance(random, (int)(this.frequency * 10000.0D), 10000)) {
         int x = GenUtils.randInt(random, 0, 15) + data.getChunkX() * 16;
         int z = GenUtils.randInt(random, 0, 15) + data.getChunkZ() * 16;
         int upperHeightRange = GenUtils.getHighestGround(data, x, z) - this.minDepthBelowSurface;
         if (upperHeightRange > this.minDepth) {
            upperHeightRange = this.minDepth;
         }

         upperHeightRange = Math.min(world.getBiomeBank(x, z).getHandler().getMaxHeightForCaves(world, x, z), upperHeightRange);
         if (upperHeightRange < 14) {
            return;
         }

         int y = GenUtils.randInt(random, 14, upperHeightRange);
         placeGeode(random.nextInt(9999), (float)this.geodeRadius, new SimpleBlock(data, x, y, z));
      }

   }
}
