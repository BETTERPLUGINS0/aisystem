package org.terraform.biome.beach;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.HeightMap;
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

public class MushroomBeachHandler extends BiomeHandler {
   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.MUSHROOM_FIELDS;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.MYCELIUM, Material.DIRT, (Material)GenUtils.randChoice(rand, Material.STONE, Material.DIRT, Material.DIRT), (Material)GenUtils.randChoice(rand, Material.STONE, Material.DIRT)};
   }

   public void populateSmallItems(TerraformWorld tw, Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (surfaceY >= TerraformGenerator.seaLevel) {
         if (BlockUtils.isDirtLike(data.getType(rawX, surfaceY, rawZ)) && BlockUtils.isAir(data.getType(rawX, surfaceY + 1, rawZ)) && GenUtils.chance(1, 60)) {
            PlantBuilder.build(data, rawX, surfaceY + 1, rawZ, PlantBuilder.RED_MUSHROOM, PlantBuilder.BROWN_MUSHROOM);
         }

      }
   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      SimpleLocation[] bigTrees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 33, 0.15F);
      SimpleLocation[] smallDecorations = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 15, 0.3F);
      SimpleLocation[] var6 = bigTrees;
      int z = bigTrees.length;

      int y;
      SimpleLocation sLoc;
      int treeY;
      int choice;
      for(y = 0; y < z; ++y) {
         sLoc = var6[y];
         treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
         sLoc = sLoc.getAtY(treeY);
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()))) {
            choice = random.nextInt(3);
            FractalTypes.Mushroom var10000;
            switch(choice) {
            case 0:
               var10000 = FractalTypes.Mushroom.GIANT_RED_MUSHROOM;
               break;
            case 1:
               var10000 = FractalTypes.Mushroom.GIANT_BROWN_MUSHROOM;
               break;
            default:
               var10000 = FractalTypes.Mushroom.GIANT_BROWN_FUNNEL_MUSHROOM;
            }

            FractalTypes.Mushroom type = var10000;
            if (HeightMap.getTrueHeightGradient(data, sLoc.getX(), sLoc.getZ(), 3) <= TConfig.c.MISC_TREES_GRADIENT_LIMIT) {
               (new MushroomBuilder(type)).build(tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
            }
         }
      }

      var6 = smallDecorations;
      z = smallDecorations.length;

      for(y = 0; y < z; ++y) {
         sLoc = var6[y];
         treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
         sLoc = sLoc.getAtY(treeY);
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()))) {
            choice = random.nextInt(4);
            switch(choice) {
            case 0:
               (new MushroomBuilder(FractalTypes.Mushroom.SMALL_POINTY_RED_MUSHROOM)).build(tw, data, sLoc.getX(), sLoc.getY() + 1, sLoc.getZ());
               break;
            case 1:
               (new MushroomBuilder(FractalTypes.Mushroom.SMALL_BROWN_MUSHROOM)).build(tw, data, sLoc.getX(), sLoc.getY() + 1, sLoc.getZ());
               break;
            case 2:
               (new MushroomBuilder(FractalTypes.Mushroom.SMALL_RED_MUSHROOM)).build(tw, data, sLoc.getX(), sLoc.getY() + 1, sLoc.getZ());
               break;
            default:
               (new MushroomBuilder(FractalTypes.Mushroom.TINY_RED_MUSHROOM)).build(tw, data, sLoc.getX(), sLoc.getY() + 1, sLoc.getZ());
            }
         }
      }

      for(int x = data.getChunkX() * 16; x < data.getChunkX() * 16 + 16; x += 2) {
         for(z = data.getChunkZ() * 16; z < data.getChunkZ() * 16 + 16; z += 2) {
            y = GenUtils.getHighestGround(data, x, z);
            if (data.getBiome(x, z) == this.getBiome() && y >= TerraformGenerator.seaLevel + 4 && HeightMap.getTrueHeightGradient(data, x, z, 3) > 2.0D && GenUtils.chance(random, 1, 20)) {
               BlockUtils.replaceCircle(random.nextInt(919292), 3.0F, new SimpleBlock(data, x, y - 2, z), (Material)GenUtils.randChoice(random, Material.BROWN_MUSHROOM_BLOCK, Material.RED_MUSHROOM_BLOCK));
            }
         }
      }

   }
}
