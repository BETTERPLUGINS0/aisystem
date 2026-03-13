package org.terraform.biome.flat;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.tree.FractalTypes;
import org.terraform.tree.TreeDB;
import org.terraform.utils.GenUtils;

public class SwampHandler extends BiomeHandler {
   @NotNull
   public BiomeBank getRiverType() {
      return BiomeBank.SWAMP;
   }

   public boolean isOcean() {
      return true;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.SWAMP;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{(Material)GenUtils.randChoice(rand, Material.GRASS_BLOCK, Material.PODZOL, Material.PODZOL), (Material)GenUtils.randChoice(rand, Material.DIRT), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE)};
   }

   public BiomeHandler getTransformHandler() {
      return BiomeBank.MANGROVE.getHandler();
   }

   public void populateSmallItems(TerraformWorld tw, Random random, int rawX, int surfaceY, int rawZ, PopulatorDataAbstract data) {
      BiomeBank.MANGROVE.getHandler().populateSmallItems(tw, random, rawX, surfaceY, rawZ, data);
   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      if (GenUtils.chance(random, 8, 10)) {
         int treeX = GenUtils.randInt(random, 2, 12) + data.getChunkX() * 16;
         int treeZ = GenUtils.randInt(random, 2, 12) + data.getChunkZ() * 16;
         if (data.getBiome(treeX, treeZ) == this.getBiome()) {
            int treeY = GenUtils.getHighestGround(data, treeX, treeZ);
            if (treeY > TerraformGenerator.seaLevel - 6) {
               TreeDB.spawnBreathingRoots(tw, new SimpleBlock(data, treeX, treeY, treeZ), Material.OAK_LOG);
               FractalTypes.Tree.SWAMP_TOP.build(tw, new SimpleBlock(data, treeX, treeY, treeZ), (t) -> {
                  t.setCheckGradient(false);
                  t.setRootMaterial(Material.OAK_WOOD);
                  t.setBranchMaterial(Material.OAK_LOG);
                  t.getFractalLeaves().setMaterial(Material.OAK_LEAVES);
                  t.getFractalLeaves().setMangrovePropagules(false);
               });
            }
         }
      }

   }

   @NotNull
   public BiomeBank getBeachType() {
      return BiomeBank.MUDFLATS;
   }

   public double calculateHeight(TerraformWorld tw, int x, int z) {
      double height = HeightMap.CORE.getHeight(tw, x, z) - 10.0D;
      if (height <= 0.0D) {
         height = 3.0D;
      }

      return height;
   }
}
