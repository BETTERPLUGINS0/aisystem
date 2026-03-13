package org.terraform.biome.ocean;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeType;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.CoralGenerator;
import org.terraform.utils.GenUtils;

public class BlackOceansHandler extends AbstractOceanHandler {
   public BlackOceansHandler(BiomeType oceanType) {
      super(oceanType);
   }

   public static void genSpike(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data, int x, int y, int z, int baseRadius, int height) {
      y -= height / 5;
      Vector base = new Vector(x, y, z);
      Vector base2 = new Vector(x + GenUtils.randInt(random, -2 * baseRadius, 2 * baseRadius), y + height, z + GenUtils.randInt(random, -2 * baseRadius, 2 * baseRadius));
      Vector v = base2.subtract(base);
      SimpleBlock one = new SimpleBlock(data, x, y, z);
      double radius = (double)baseRadius;

      for(int i = 0; i <= height; ++i) {
         Vector seg = v.clone().multiply((float)i / (float)height);
         SimpleBlock segment = one.getRelative(seg);
         BlockUtils.replaceSphere((int)(tw.getSeed() * 12L), (float)radius, 2.0F, (float)radius, segment, false, false, Material.STONE);
         radius = (double)baseRadius * (1.0D - (double)i / (double)height);
      }

   }

   public boolean isOcean() {
      return true;
   }

   @NotNull
   public Biome getBiome() {
      return this.oceanType == BiomeType.DEEP_OCEANIC ? Biome.DEEP_COLD_OCEAN : Biome.COLD_OCEAN;
   }

   @NotNull
   public Material[] getSurfaceCrust(Random rand) {
      return new Material[]{Material.STONE};
   }

   public void populateSmallItems(TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (surfaceY >= TerraformGenerator.seaLevel - 2) {
         data.setType(rawX, surfaceY, rawZ, Material.GRAVEL);
      } else if (surfaceY >= TerraformGenerator.seaLevel - 4 && random.nextBoolean()) {
         data.setType(rawX, surfaceY, rawZ, Material.GRAVEL);
      }

      if (surfaceY <= TerraformGenerator.seaLevel) {
         if (BlockUtils.isStoneLike(data.getType(rawX, surfaceY, rawZ))) {
            if (GenUtils.chance(random, 1, 80)) {
               CoralGenerator.generateKelpGrowth(data, rawX, surfaceY + 1, rawZ);
            }

         }
      }
   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      for(int x = data.getChunkX() * 16 + 3; x < data.getChunkX() * 16 + 16 - 3; ++x) {
         for(int z = data.getChunkZ() * 16 + 3; z < data.getChunkZ() * 16 + 16 - 3; ++z) {
            if (data.getBiome(x, z) == this.getBiome() && GenUtils.chance(random, 1, 200)) {
               int y = HeightMap.getBlockHeight(tw, x, z);
               genSpike(tw, random, data, x, y, z, GenUtils.randInt(5, 15), GenUtils.randInt(50, 100));
            }
         }
      }

   }

   public boolean forceDefaultToBeach() {
      return true;
   }

   @NotNull
   public BiomeBank getBeachType() {
      return BiomeBank.BLACK_OCEAN_BEACH;
   }
}
