package org.terraform.biome.flat;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Snowable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class IceSpikesHandler extends BiomeHandler {
   public static void genSpike(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data, int x, int y, int z, int baseRadius, int height) {
      y -= height / 5;
      Vector base = new Vector(x, y, z);
      Vector base2 = new Vector(x + GenUtils.randInt(random, (int)(-1.5D * (double)baseRadius), (int)(1.5D * (double)baseRadius)), y + height, z + GenUtils.randInt(random, (int)(-1.5D * (double)baseRadius), (int)(1.5D * (double)baseRadius)));
      Vector v = base2.subtract(base);
      SimpleBlock one = new SimpleBlock(data, x, y, z);
      double radius = (double)baseRadius;

      for(int i = 0; i <= height; ++i) {
         Vector seg = v.clone().multiply((float)i / (float)height);
         SimpleBlock segment = one.getRelative(seg);
         BlockUtils.replaceSphere((int)(tw.getSeed() * 12L), (float)radius, 2.0F, (float)radius, segment, false, false, Material.PACKED_ICE);
         radius = (double)baseRadius * (1.0D - (double)i / (double)height);
      }

   }

   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.ICE_SPIKES;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{GenUtils.weightedRandomMaterial(rand, Material.SNOW_BLOCK, 5, Material.SNOW_BLOCK, 25), Material.SNOW_BLOCK, (Material)GenUtils.randChoice(rand, Material.SNOW_BLOCK, Material.DIRT), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld world, Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (data.getType(rawX, surfaceY + 1, rawZ) == Material.AIR && !Tag.ICE.isTagged(data.getType(rawX, surfaceY, rawZ))) {
         data.setType(rawX, surfaceY + 1, rawZ, Material.SNOW);
         BlockData var8 = data.getBlockData(rawX, surfaceY, rawZ);
         if (var8 instanceof Snowable) {
            Snowable snowable = (Snowable)var8;
            snowable.setSnowy(true);
            data.setBlockData(rawX, surfaceY, rawZ, snowable);
         }
      }

   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      SimpleLocation[] spikes = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 16, 0.5F);
      SimpleLocation[] var5 = spikes;
      int var6 = spikes.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         SimpleLocation sLoc = var5[var7];
         int spikeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
         sLoc = sLoc.getAtY(spikeY);
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()) == Material.SNOW_BLOCK) {
            if (GenUtils.chance(random, 1, 10)) {
               genSpike(tw, random, data, sLoc.getX(), sLoc.getY(), sLoc.getZ(), GenUtils.randInt(3, 7), GenUtils.randInt(40, 55));
            } else {
               genSpike(tw, random, data, sLoc.getX(), sLoc.getY(), sLoc.getZ(), GenUtils.randInt(3, 5), GenUtils.randInt(13, 24));
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
