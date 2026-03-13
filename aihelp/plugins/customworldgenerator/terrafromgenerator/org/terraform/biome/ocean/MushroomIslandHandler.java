package org.terraform.biome.ocean;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeSection;
import org.terraform.biome.BiomeType;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.CoralGenerator;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class MushroomIslandHandler extends AbstractOceanHandler {
   public MushroomIslandHandler() {
      super(BiomeType.DEEP_OCEANIC);
   }

   public double calculateHeight(@NotNull TerraformWorld tw, int x, int z) {
      double height = super.calculateHeight(tw, x, z);
      BiomeSection currentSection = BiomeBank.getBiomeSectionFromBlockCoords(tw, x, z);
      if (currentSection.getBiomeBank() != BiomeBank.MUSHROOM_ISLANDS) {
         currentSection = BiomeSection.getMostDominantSection(tw, x, z);
      }

      float islandRadius = (float)BiomeSection.sectionWidth / 2.5F;
      BlockFace[] var8 = BlockUtils.directBlockFaces;
      int var9 = var8.length;

      int relX;
      for(relX = 0; relX < var9; ++relX) {
         BlockFace face = var8[relX];
         if (currentSection.getRelative(face.getModX(), face.getModZ()).getBiomeBank().isDry()) {
            islandRadius *= 0.65F;
            break;
         }
      }

      FastNoise circleNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_MUSHROOMISLAND_CIRCLE, (world) -> {
         FastNoise n = new FastNoise((int)world.getSeed());
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFractalOctaves(3);
         n.SetFrequency(0.04F);
         return n;
      });
      SimpleLocation center = currentSection.getCenter();
      relX = x - center.getX();
      int relZ = z - center.getZ();
      double equationResult = Math.pow((double)relX, 2.0D) / Math.pow((double)islandRadius, 2.0D) + Math.pow((double)relZ, 2.0D) / Math.pow((double)islandRadius, 2.0D);
      double noise = 1.0D + 0.7D * (double)circleNoise.GetNoise((float)relX, (float)relZ);
      if (equationResult <= noise) {
         double supplement = (double)TerraformGenerator.seaLevel - height;
         if (equationResult >= noise * 0.9D) {
            return height + supplement * 0.6D;
         } else if (equationResult >= noise * 0.7D) {
            return height + supplement + 10.0D;
         } else if (equationResult >= noise * 0.6D) {
            return height + supplement + 9.0D;
         } else if (equationResult >= noise * 0.5D) {
            return height + supplement + 8.0D;
         } else {
            return equationResult >= noise * 0.4D ? height + supplement + 7.5D : height + supplement + 7.0D;
         }
      } else {
         return height;
      }
   }

   public boolean isOcean() {
      return true;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.MUSHROOM_FIELDS;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.GRAVEL, Material.GRAVEL, (Material)GenUtils.randChoice(rand, Material.STONE, Material.GRAVEL, Material.STONE), (Material)GenUtils.randChoice(rand, Material.STONE), (Material)GenUtils.randChoice(rand, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (rawX < TerraformGenerator.seaLevel) {
         if (BlockUtils.isStoneLike(data.getType(rawX, rawX, rawZ))) {
            if (GenUtils.chance(random, 1, 150)) {
               CoralGenerator.generateKelpGrowth(data, rawX, rawX + 1, rawZ);
            }

         }
      }
   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      SimpleLocation[] rocks = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 25, 0.4F);
      SimpleLocation[] var5 = rocks;
      int var6 = rocks.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         SimpleLocation sLoc = var5[var7];
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome()) {
            int rockY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
            sLoc = sLoc.getAtY(rockY);
            if (data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()) == Material.GRAVEL && rockY < TerraformGenerator.seaLevel) {
               BlockUtils.replaceSphere(random.nextInt(9987), (float)GenUtils.randDouble(random, 3.0D, 7.0D), (float)GenUtils.randDouble(random, 2.0D, 4.0D), (float)GenUtils.randDouble(random, 3.0D, 7.0D), new SimpleBlock(data, sLoc), true, (Material)GenUtils.randChoice((Object[])(Material.STONE, Material.GRANITE, Material.ANDESITE, Material.DIORITE)));
            }
         }
      }

   }

   @NotNull
   public BiomeBank getBeachType() {
      return BiomeBank.MUSHROOM_BEACH;
   }

   public boolean forceDefaultToBeach() {
      return true;
   }
}
