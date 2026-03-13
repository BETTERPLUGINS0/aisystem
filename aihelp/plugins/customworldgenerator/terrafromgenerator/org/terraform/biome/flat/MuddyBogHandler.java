package org.terraform.biome.flat;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeHandler;
import org.terraform.biome.custombiomes.CustomBiomeType;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.tree.FractalTypes;
import org.terraform.tree.MushroomBuilder;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class MuddyBogHandler extends BiomeHandler {
   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.SWAMP;
   }

   @NotNull
   public CustomBiomeType getCustomBiome() {
      return CustomBiomeType.MUDDY_BOG;
   }

   @NotNull
   public BiomeBank getBeachType() {
      return BiomeBank.BOG_BEACH;
   }

   @NotNull
   public BiomeBank getRiverType() {
      return BiomeBank.BOG_RIVER;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.GRASS_BLOCK, Material.DIRT, Material.DIRT, (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      SimpleBlock block = new SimpleBlock(data, rawX, surfaceY, rawZ);
      if (block.getUp().getType() == Material.AIR && block.getType() == Material.GRASS_BLOCK) {
         if (GenUtils.chance(random, 1, 85)) {
            PlantBuilder.DEAD_BUSH.build(block.getUp());
         } else if (GenUtils.chance(random, 1, 85)) {
            PlantBuilder.BROWN_MUSHROOM.build(block.getUp());
         } else if (GenUtils.chance(random, 1, 85)) {
            PlantBuilder.GRASS.build(block.getUp());
         } else if (GenUtils.chance(random, 1, 85)) {
            PlantBuilder.TALL_GRASS.build(data, rawX, surfaceY + 1, rawZ);
         } else if (TConfig.areDecorationsEnabled() && GenUtils.chance(random, 1, 300)) {
            BlockUtils.replaceCircularPatch(random.nextInt(9999), 2.5F, block, Material.DRIPSTONE_BLOCK);
            if (GenUtils.chance(random, 1, 7)) {
               BlockUtils.upLPointedDripstone(GenUtils.randInt(random, 2, 4), block.getUp());
            }

            BlockFace[] var8 = BlockUtils.xzPlaneBlockFaces;
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               BlockFace face = var8[var10];
               if (GenUtils.chance(random, 1, 7)) {
                  BlockUtils.upLPointedDripstone(GenUtils.randInt(random, 2, 4), block.getRelative(face).getGround().getUp());
               }
            }
         }
      }

   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      SimpleLocation[] shrooms = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 16);
      SimpleLocation[] var5 = shrooms;
      int var6 = shrooms.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         SimpleLocation sLoc = var5[var7];
         int treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
         sLoc = sLoc.getAtY(treeY);
         if (this.isRightBiome(tw.getBiomeBank(sLoc.getX(), sLoc.getZ())) && !BlockUtils.isWet(new SimpleBlock(data, sLoc.getX(), sLoc.getY() + 1, sLoc.getZ())) && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ())) && data.getType(sLoc.getX(), sLoc.getY() + 1, sLoc.getZ()) == Material.AIR) {
            if (random.nextBoolean()) {
               (new MushroomBuilder(FractalTypes.Mushroom.SMALL_BROWN_MUSHROOM)).build(tw, data, sLoc.getX(), sLoc.getY() + 1, sLoc.getZ());
            } else {
               (new MushroomBuilder(FractalTypes.Mushroom.TINY_BROWN_MUSHROOM)).build(tw, data, sLoc.getX(), sLoc.getY() + 1, sLoc.getZ());
            }
         }
      }

   }

   private boolean isRightBiome(BiomeBank bank) {
      return bank == BiomeBank.MUDDY_BOG || bank == BiomeBank.BOG_BEACH;
   }

   public double calculateHeight(TerraformWorld tw, int x, int z) {
      double height = super.calculateHeight(tw, x, z) - 5.0D;
      FastNoise sinkin = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_MUDDYBOG_HEIGHTMAP, (world) -> {
         FastNoise n = new FastNoise((int)world.getSeed());
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFractalOctaves(4);
         n.SetFrequency(0.08F);
         return n;
      });
      if ((double)sinkin.GetNoise((float)x, (float)z) < -0.15D && height > (double)TerraformGenerator.seaLevel) {
         height -= height - (double)TerraformGenerator.seaLevel + 2.0D;
      }

      return height;
   }
}
