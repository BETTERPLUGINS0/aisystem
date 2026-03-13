package org.terraform.structure.caves;

import java.util.Objects;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.type.SeaPickle;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.small_items.PlantBuilder;
import org.terraform.tree.FractalTypes;
import org.terraform.tree.MushroomBuilder;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.StalactiteBuilder;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class MushroomCavePopulator extends GenericLargeCavePopulator {
   public MushroomCavePopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   protected void populateFloor(@NotNull SimpleBlock floor, int waterLevel) {
      TerraformWorld tw = floor.getPopData().getTerraformWorld();
      if (floor.getY() <= waterLevel) {
         int waterDepth = waterLevel - floor.getY();
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

            floor.getUp().RPillar(h, new Random(), Material.DIRT);
            floor = floor.getUp(h);
         }
      }

      if (floor.getY() >= waterLevel) {
         floor.setType(Material.MYCELIUM);
         if (GenUtils.chance(this.rand, 7, 100)) {
            SimpleBlock up = floor.getUp();
            if (!up.isSolid()) {
               PlantBuilder.build(up, PlantBuilder.RED_MUSHROOM, PlantBuilder.BROWN_MUSHROOM);
            }
         }

      } else {
         if (BlockUtils.isWet(floor.getUp()) && GenUtils.chance(this.rand, 7, 100)) {
            SeaPickle sp = (SeaPickle)Bukkit.createBlockData(Material.SEA_PICKLE);
            sp.setPickles(GenUtils.randInt(3, 4));
            floor.getUp().setBlockData(sp);
         }

      }
   }

   protected void populateCeilFloorPair(@NotNull SimpleBlock ceil, @NotNull SimpleBlock floor, int height) {
      TerraformWorld tw = ceil.getPopData().getTerraformWorld();

      int newHeight;
      for(newHeight = height; newHeight > 0 && floor.getUp().isSolid(); --newHeight) {
         floor = floor.getUp();
      }

      if (newHeight > 0) {
         if (GenUtils.chance(this.rand, 1, 150)) {
            int r = 2;
            int h = GenUtils.randInt(this.rand, (int)((float)height / 2.5F), (int)(1.5F * ((float)height / 2.5F)));
            (new StalactiteBuilder(new Material[]{BlockUtils.stoneOrSlateWall(ceil.getY())})).setSolidBlockType(BlockUtils.stoneOrSlate(ceil.getY())).makeSpike(ceil, (double)r, h, false);
         } else if (floor.getChunkX() == floor.getPopData().getChunkX() && floor.getChunkZ() == floor.getPopData().getChunkZ() && floor.getType() == Material.MYCELIUM && newHeight >= 15 && GenUtils.chance(this.rand, 1, 110)) {
            (new MushroomBuilder((FractalTypes.Mushroom)Objects.requireNonNull((FractalTypes.Mushroom)GenUtils.choice(this.rand, FractalTypes.Mushroom.values())))).build(tw, floor.getPopData(), floor.getX(), floor.getY(), floor.getZ());
         }

      }
   }
}
