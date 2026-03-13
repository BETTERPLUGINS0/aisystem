package org.terraform.structure.caves;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.type.SeaPickle;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.cavepopulators.LushClusterCavePopulator;
import org.terraform.coregen.populatordata.PopulatorDataICABiomeWriterAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.StalactiteBuilder;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class LargeLushCavePopulator extends GenericLargeCavePopulator {
   public LargeLushCavePopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   protected void populateFloor(@NotNull SimpleBlock floor, int waterLevel) {
      if (floor.getY() <= waterLevel) {
         int waterDepth = waterLevel - floor.getY();
         TerraformWorld tw = floor.getPopData().getTerraformWorld();
         FastNoise raisedGroundNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.STRUCTURE_LARGECAVE_RAISEDGROUNDNOISE, (world) -> {
            FastNoise n = new FastNoise((int)(world.getSeed() * 5L));
            n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
            n.SetFractalOctaves(3);
            n.SetFrequency(0.06F);
            return n;
         });
         double noise = (double)raisedGroundNoise.GetNoise((float)floor.getX(), (float)floor.getZ());
         if (noise > 0.0D) {
            int h = (int)Math.round((double)(4.3F * (float)waterDepth) * noise);
            if (h > waterDepth) {
               h = (int)Math.round((double)waterDepth + Math.sqrt((double)(h - waterDepth)));
            }

            floor.getUp().RPillar(h, new Random(), Material.CLAY);
            floor = floor.getUp(h - 1);
         }

         if (floor.getY() <= waterLevel) {
            if (TConfig.arePlantsEnabled() && BlockUtils.isWet(floor.getUp()) && GenUtils.chance(this.rand, 7, 100)) {
               SeaPickle sp = (SeaPickle)Bukkit.createBlockData(Material.SEA_PICKLE);
               sp.setPickles(GenUtils.randInt(3, 4));
               floor.getUp().setBlockData(sp);
            }

            if (GenUtils.chance(this.rand, 1, 200) && BlockUtils.isWet(floor.getAtY(waterLevel)) && floor.getAtY(waterLevel + 1).isAir()) {
               PlantBuilder.LILY_PAD.build(floor.getAtY(waterLevel + 1));
            }

            if (GenUtils.chance(this.rand, 1, 130)) {
               int r = 2;
               int h = GenUtils.randInt(this.rand, 6 * waterDepth, (int)(1.5F * (float)(6 * waterDepth)));
               (new StalactiteBuilder(new Material[]{BlockUtils.stoneOrSlateWall(floor.getY())})).setSolidBlockType(BlockUtils.stoneOrSlate(floor.getY())).makeSpike(floor, (double)r, h, true);
            }

         }
      }
   }

   protected void populateCeilFloorPair(@NotNull SimpleBlock ceil, @NotNull SimpleBlock floor, int height) {
      TerraformWorld tw = ceil.getPopData().getTerraformWorld();

      int cutoff;
      for(cutoff = height; cutoff > 0 && floor.getUp().isSolid(); --cutoff) {
         floor = floor.getUp();
      }

      if (cutoff > 0) {
         (new LushClusterCavePopulator(10.0F, true)).oneUnit(tw, new Random(), ceil, floor, false);
         int ny;
         if (GenUtils.chance(this.rand, 1, 150)) {
            int r = 2;
            ny = GenUtils.randInt(this.rand, (int)((float)height / 2.5F), (int)(1.5F * ((float)height / 2.5F)));
            (new StalactiteBuilder(new Material[]{BlockUtils.stoneOrSlateWall(ceil.getY())})).setSolidBlockType(BlockUtils.stoneOrSlate(ceil.getY())).makeSpike(ceil, (double)r, ny, false);
         }

         PopulatorDataICABiomeWriterAbstract biomeWriter = (PopulatorDataICABiomeWriterAbstract)TerraformGeneratorPlugin.injector.getICAData(ceil.getPopData());

         for(ny = floor.getY(); ny <= ceil.getY(); ++ny) {
            biomeWriter.setBiome(floor.getX(), ny, floor.getZ(), Biome.LUSH_CAVES);
         }

      }
   }
}
