package org.terraform.biome.flat;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Snowable;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.tree.FractalTreeBuilder;
import org.terraform.tree.FractalTypes;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class SnowyWastelandHandler extends BiomeHandler {
   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.SNOWY_PLAINS;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.SNOW_BLOCK, Material.SNOW_BLOCK, (Material)GenUtils.randChoice(rand, Material.SNOW_BLOCK, Material.SNOW_BLOCK, Material.DIRT, Material.DIRT), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (GenUtils.chance(random, 1, 500)) {
         BlockUtils.replaceCircularPatch(random.nextInt(9999), 3.0F, new SimpleBlock(data, rawX, 0, rawZ), Material.POWDER_SNOW);
      }

      if (data.getType(rawX, surfaceY + 1, rawZ) == Material.AIR && data.getType(rawX, surfaceY, rawZ) != Material.POWDER_SNOW && GenUtils.isGroundLike(data.getType(rawX, surfaceY, rawZ))) {
         data.setType(rawX, surfaceY + 1, rawZ, Material.SNOW);
         BlockData var8 = data.getBlockData(rawX, surfaceY, rawZ);
         if (var8 instanceof Snowable) {
            Snowable snowable = (Snowable)var8;
            snowable.setSnowy(true);
            data.setBlockData(rawX, surfaceY, rawZ, snowable);
         }
      }

   }

   public void populateLargeItems(@NotNull TerraformWorld world, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      SimpleLocation[] items = GenUtils.randomObjectPositions(world, data.getChunkX(), data.getChunkZ(), 44);
      SimpleLocation[] var5 = items;
      int var6 = items.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         SimpleLocation sLoc = var5[var7];
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome()) {
            sLoc = sLoc.getAtY(GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ()));
            if (data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()) == Material.SNOW_BLOCK) {
               if (!TConfig.areTreesEnabled()) {
                  BlockUtils.spawnPillar(random, data, sLoc.getX(), sLoc.getY(), sLoc.getZ(), Material.SPRUCE_LOG, 6, 8);
               }

               if (GenUtils.chance(1, 3)) {
                  (new FractalTreeBuilder(FractalTypes.Tree.FROZEN_TREE_SMALL)).setSnowyLeaves(true).build(world, data, sLoc.getX(), sLoc.getY() + 1, sLoc.getZ());
               }

               if (GenUtils.chance(1, 30)) {
                  (new FractalTreeBuilder(FractalTypes.Tree.FROZEN_TREE_BIG)).build(world, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
               }
            }
         }
      }

   }

   @NotNull
   public BiomeBank getBeachType() {
      return BiomeBank.ICY_BEACH;
   }

   @NotNull
   public BiomeBank getRiverType() {
      return BiomeBank.FROZEN_RIVER;
   }
}
