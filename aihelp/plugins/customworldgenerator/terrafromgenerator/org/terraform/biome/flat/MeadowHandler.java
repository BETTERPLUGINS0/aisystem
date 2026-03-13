package org.terraform.biome.flat;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.version.V_1_21_5;

public class MeadowHandler extends BiomeHandler {
   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.MEADOW;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.GRASS_BLOCK, Material.DIRT, Material.DIRT, (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld tw, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (data.getType(rawX, surfaceY, rawZ) == Material.GRASS_BLOCK && !BlockUtils.isWet(new SimpleBlock(data, rawX, surfaceY, rawZ)) && GenUtils.chance(random, 1, 10)) {
         switch(random.nextInt(4)) {
         case 0:
         case 1:
            PlantBuilder.GRASS.build(data, rawX, surfaceY + 1, rawZ);
            break;
         case 2:
            PlantBuilder.TALL_GRASS.build(data, rawX, surfaceY + 1, rawZ);
            break;
         case 3:
            V_1_21_5.wildflowers(random, data, rawX, surfaceY + 1, rawZ);
         }
      }

   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      SimpleLocation[] flowerCenters = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 23);
      SimpleLocation[] var5 = flowerCenters;
      int i = flowerCenters.length;

      for(int var7 = 0; var7 < i; ++var7) {
         SimpleLocation center = var5[var7];
         Random hashRand = tw.getHashedRand(78019432L, center.getX(), center.getZ());
         PlantBuilder flowerer = BlockUtils.pickFlower(hashRand);
         BlockUtils.lambdaCircularPatch(hashRand.nextInt(211312), (float)GenUtils.randInt(5, 7), (new SimpleBlock(data, center.getX(), 0, center.getZ())).getGround(), (b) -> {
            if (data.getBiome(b.getX(), b.getZ()) == this.getBiome() && hashRand.nextInt(4) == 0) {
               flowerer.build(b.getUp());
            }

         });
      }

      boolean isPumpkin = random.nextBoolean();
      if (GenUtils.chance(1, 70)) {
         for(i = 0; i < GenUtils.randInt(5, 10); ++i) {
            int[] loc = GenUtils.randomSurfaceCoordinates(random, data);
            if (data.getBiome(loc[0], loc[2]) == this.getBiome()) {
               SimpleBlock target = new SimpleBlock(data, loc[0], GenUtils.getHighestGround(data, loc[0], loc[2]) + 1, loc[2]);
               if (!target.isSolid()) {
                  if (isPumpkin) {
                     PlantBuilder.PUMPKIN.build(target);
                  } else {
                     PlantBuilder.MELON.build(target);
                  }
               }
            }
         }
      }

      SimpleLocation[] poffLocs = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 16);
      SimpleLocation[] var15 = poffLocs;
      int var17 = poffLocs.length;

      for(int var18 = 0; var18 < var17; ++var18) {
         SimpleLocation sLoc = var15[var18];
         int highestY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
         if (!BlockUtils.isWet(new SimpleBlock(data, sLoc.getX(), highestY + 1, sLoc.getZ()))) {
            sLoc = sLoc.getAtY(highestY);
            if (TConfig.arePlantsEnabled() && data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()))) {
               BlockUtils.replaceSphere(random.nextInt(424444), 2.0F, 2.0F, 2.0F, new SimpleBlock(data, sLoc.getX(), sLoc.getY() + 1, sLoc.getZ()), false, Material.OAK_LEAVES);
            }
         }
      }

   }
}
