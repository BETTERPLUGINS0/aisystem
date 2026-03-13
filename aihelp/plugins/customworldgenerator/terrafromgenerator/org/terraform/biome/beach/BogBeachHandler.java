package org.terraform.biome.beach;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeHandler;
import org.terraform.biome.custombiomes.CustomBiomeType;
import org.terraform.biome.flat.MuddyBogHandler;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.small_items.PlantBuilder;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class BogBeachHandler extends BiomeHandler {
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
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.GRASS_BLOCK, (Material)GenUtils.randChoice(rand, Material.DIRT), (Material)GenUtils.randChoice(rand, Material.DIRT), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld tw, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      SimpleBlock block = new SimpleBlock(data, rawX, surfaceY, rawZ);
      if (!BlockUtils.isWet(block.getUp())) {
         if (GenUtils.chance(random, 1, 85)) {
            PlantBuilder.DEAD_BUSH.build(block.getUp());
         } else if (GenUtils.chance(random, 1, 85)) {
            PlantBuilder.BROWN_MUSHROOM.build(block.getUp());
         } else if (GenUtils.chance(random, 1, 85)) {
            PlantBuilder.GRASS.build(block.getUp());
         } else if (GenUtils.chance(random, 1, 85)) {
            PlantBuilder.TALL_GRASS.build(block.getUp());
         } else {
            BlockFace[] var8 = BlockUtils.directBlockFaces;
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               BlockFace face = var8[var10];
               if (GenUtils.chance(random, 1, 75) && BlockUtils.isWet(block.getRelative(face))) {
                  PlantBuilder.SUGAR_CANE.build(block.getUp(), random, 2, 5);
               }
            }
         }
      }

   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      (new MuddyBogHandler()).populateLargeItems(tw, random, data);
   }
}
