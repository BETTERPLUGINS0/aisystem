package org.terraform.biome.flat;

import java.util.Locale;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.schematic.SchematicParser;
import org.terraform.schematic.TerraSchematic;
import org.terraform.small_items.PlantBuilder;
import org.terraform.tree.FractalTreeBuilder;
import org.terraform.tree.FractalTypes;
import org.terraform.tree.TreeDB;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class JungleHandler extends BiomeHandler {
   public static void createBush(@NotNull PopulatorDataAbstract data, float noiseIncrement, int oriX, int oriY, int oriZ) {
      if (TConfig.arePlantsEnabled()) {
         float rX = 2.5F + (float)((double)noiseIncrement * Math.random());
         float rY = 1.3F + (float)((double)noiseIncrement * Math.random());
         float rZ = 2.5F + (float)((double)noiseIncrement * Math.random());
         SimpleBlock base = new SimpleBlock(data, oriX, oriY, oriZ);

         for(int x = -Math.round(rX); (float)x <= rX; ++x) {
            for(int y = -Math.round(rY); (float)y <= rY; ++y) {
               for(int z = -Math.round(rZ); (float)z <= rZ; ++z) {
                  double equationResult = Math.pow((double)x, 2.0D) / Math.pow((double)rX, 2.0D) + Math.pow((double)y, 2.0D) / Math.pow((double)rY, 2.0D) + Math.pow((double)z, 2.0D) / Math.pow((double)rZ, 2.0D);
                  if (equationResult <= 1.0D) {
                     SimpleBlock block = base.getRelative(x, y + 1, z);
                     if (!(Math.random() < equationResult - 0.5D) && !block.isSolid() && !BlockUtils.isWet(block)) {
                        block.setType(Material.JUNGLE_LEAVES);
                     }
                  }
               }
            }
         }

         if (Math.random() > 0.3D) {
            base.setType(Material.JUNGLE_LOG);
         }

      }
   }

   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.JUNGLE;
   }

   @NotNull
   public BiomeBank getRiverType() {
      return BiomeBank.JUNGLE_RIVER;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{GenUtils.weightedRandomMaterial(rand, Material.GRASS_BLOCK, 35, Material.PODZOL, 5), Material.DIRT, Material.DIRT, (Material)GenUtils.randChoice(rand, Material.DIRT, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld tw, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (BlockUtils.isDirtLike(data.getType(rawX, surfaceY, rawZ)) && BlockUtils.isAir(data.getType(rawX, surfaceY + 1, rawZ)) && GenUtils.chance(2, 3)) {
         if (random.nextBoolean()) {
            GenUtils.weightedRandomSmallItem(random, PlantBuilder.GRASS, 5, BlockUtils.pickFlower(), 1).build(data, rawX, surfaceY + 1, rawZ);
         } else if (BlockUtils.isAir(data.getType(rawX, surfaceY + 2, rawZ))) {
            PlantBuilder.TALL_GRASS.build(data, rawX, surfaceY + 1, rawZ);
         }
      }

   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      FastNoise groundWoodNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_JUNGLE_GROUNDWOOD, (world) -> {
         FastNoise n = new FastNoise((int)(world.getSeed() * 12L));
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFractalOctaves(3);
         n.SetFrequency(0.07F);
         return n;
      });
      FastNoise groundLeavesNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_JUNGLE_GROUNDLEAVES, (world) -> {
         FastNoise n = new FastNoise((int)(world.getSeed() * 2L));
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFrequency(0.07F);
         return n;
      });
      SimpleLocation[] bigTrees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 20);
      SimpleLocation[] trees;
      int x;
      int z;
      int distanceToSeaOrMountain;
      if (TConfig.c.TREES_JUNGLE_BIG_ENABLED) {
         trees = bigTrees;
         x = bigTrees.length;

         for(z = 0; z < x; ++z) {
            SimpleLocation sLoc = trees[z];
            distanceToSeaOrMountain = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
            sLoc = sLoc.getAtY(distanceToSeaOrMountain);
            if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()))) {
               (new FractalTreeBuilder(FractalTypes.Tree.JUNGLE_BIG)).build(tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
            }
         }
      }

      trees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 9);
      SimpleLocation[] var14 = trees;
      z = trees.length;

      int y;
      for(y = 0; y < z; ++y) {
         SimpleLocation sLoc = var14[y];
         int treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
         sLoc = sLoc.getAtY(treeY);
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()))) {
            if (GenUtils.chance(random, 1000 - TConfig.c.BIOME_JUNGLE_STATUE_CHANCE, 1000)) {
               TreeDB.spawnSmallJungleTree(false, tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
            } else {
               this.spawnStatue(random, data, sLoc);
            }
         }
      }

      for(x = data.getChunkX() * 16; x < data.getChunkX() * 16 + 16; ++x) {
         for(z = data.getChunkZ() * 16; z < data.getChunkZ() * 16 + 16; ++z) {
            y = GenUtils.getHighestGround(data, x, z);
            distanceToSeaOrMountain = Math.min(y - TerraformGenerator.seaLevel, 80 - y);
            if (distanceToSeaOrMountain > 0) {
               float leavesNoiseValue = groundLeavesNoise.GetNoise((float)x, (float)z);
               float groundWoodNoiseValue = groundWoodNoise.GetNoise((float)x, (float)z);
               if (distanceToSeaOrMountain <= 4) {
                  leavesNoiseValue -= -0.25F * (float)distanceToSeaOrMountain + 1.0F;
                  groundWoodNoiseValue -= -0.25F * (float)distanceToSeaOrMountain + 1.0F;
               }

               if (data.getBiome(x, z) == this.getBiome() && (double)leavesNoiseValue > -0.12D && Math.random() > 0.85D) {
                  createBush(data, leavesNoiseValue, x, y, z);
               } else if (GenUtils.chance(1, 95)) {
                  createBush(data, 0.0F, x, y, z);
               }

               if ((double)groundWoodNoiseValue > 0.3D) {
                  data.lsetType(x, y + 1, z, Material.JUNGLE_WOOD);
               }
            }

            if (data.getBiome(x, z) == this.getBiome() && BlockUtils.isDirtLike(data.getType(x, y, z)) && data.getType(x, y + 1, z) == Material.JUNGLE_WOOD && BlockUtils.isAir(data.getType(x, y + 2, z)) && GenUtils.chance(2, 9)) {
               PlantBuilder.build(data, x, y + 2, z, PlantBuilder.RED_MUSHROOM, PlantBuilder.BROWN_MUSHROOM);
            }
         }
      }

   }

   protected void spawnStatue(@NotNull Random random, @NotNull PopulatorDataAbstract data, @NotNull SimpleLocation sLoc) {
      if (TConfig.areStructuresEnabled()) {
         try {
            TerraSchematic schema = TerraSchematic.load("jungle-statue1", new SimpleBlock(data, sLoc.getX(), sLoc.getY(), sLoc.getZ()));
            schema.parser = new JungleHandler.JungleStatueSchematicParser();
            schema.setFace(BlockUtils.getDirectBlockFace(random));
            schema.apply();
         } catch (Throwable var5) {
            TerraformGeneratorPlugin.logger.stackTrace(var5);
         }

      }
   }

   protected static class JungleStatueSchematicParser extends SchematicParser {
      public void applyData(@NotNull SimpleBlock block, @NotNull BlockData data) {
         if (data.getMaterial().toString().contains("COBBLESTONE")) {
            data = Bukkit.createBlockData(data.getAsString().replaceAll("cobblestone", ((Material)GenUtils.randChoice(new Random(), Material.COBBLESTONE, Material.ANDESITE, Material.STONE, Material.MOSSY_COBBLESTONE)).toString().toLowerCase(Locale.ENGLISH)));
            super.applyData(block, data);
         } else if (data.getMaterial() == Material.STONE_BRICK_STAIRS) {
            if ((new Random()).nextBoolean()) {
               data = Bukkit.createBlockData(data.getAsString().replaceAll("stone_brick", "mossy_stone_brick"));
            }

            super.applyData(block, data);
         } else {
            block.setBlockData(data);
            super.applyData(block, data);
         }

         if (data.getMaterial().isBlock() && GenUtils.chance(1, 10)) {
            BlockUtils.vineUp(block, 3);
         }

      }
   }
}
