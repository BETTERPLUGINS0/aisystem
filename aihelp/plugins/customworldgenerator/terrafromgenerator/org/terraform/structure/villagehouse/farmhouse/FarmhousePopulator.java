package org.terraform.structure.villagehouse.farmhouse;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.Slab.Type;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.MegaChunk;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.schematic.TerraSchematic;
import org.terraform.structure.villagehouse.VillageHousePopulator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.WoodUtils;
import org.terraform.utils.noise.FastNoise;

public class FarmhousePopulator extends VillageHousePopulator {
   public void populate(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data) {
      if (TConfig.areStructuresEnabled()) {
         MegaChunk mc = new MegaChunk(data.getChunkX(), data.getChunkZ());
         int[] coords = mc.getCenterBiomeSectionBlockCoords();
         int x = coords[0];
         int z = coords[1];
         int height = GenUtils.getHighestGround(data, x, z);
         this.spawnFarmHouse(tw, this.getHashedRandom(tw, data.getChunkX(), data.getChunkZ()), data, x, height + 1, z);
      }
   }

   public void spawnFarmHouse(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      if (TConfig.areStructuresEnabled()) {
         try {
            BiomeBank biome = tw.getBiomeBank(x, z);
            y += GenUtils.randInt(random, 1, 3);
            TerraSchematic farmHouse = TerraSchematic.load("farmhouse", new SimpleBlock(data, x, y, z));
            farmHouse.parser = new FarmhouseSchematicParser(biome, random, data);
            farmHouse.setFace(BlockUtils.getDirectBlockFace(random));
            farmHouse.apply();
            TerraformGeneratorPlugin.logger.info("Spawning farmhouse at " + x + "," + y + "," + z + " with rotation of " + String.valueOf(farmHouse.getFace()));
            data.addEntity(x, y + 1, z, EntityType.VILLAGER);
            data.addEntity(x, y + 1, z, EntityType.VILLAGER);
            data.addEntity(x, y + 1, z, EntityType.CAT);

            int nz;
            for(int nx = -9; nx <= 9; ++nx) {
               for(nz = -9; nz <= 9; ++nz) {
                  if (!data.getType(x + nx, y - 1, z + nz).toString().contains("PLANKS") && !data.getType(x + nx, y - 1, z + nz).toString().contains("STONE_BRICKS")) {
                     if (data.getType(x + nx, y - 1, z + nz).toString().contains("LOG")) {
                        BlockUtils.setDownUntilSolid(x + nx, y - 2, z + nz, data, data.getType(x + nx, y - 1, z + nz));
                     }
                  } else {
                     BlockUtils.setDownUntilSolid(x + nx, y - 2, z + nz, data, Material.COBBLESTONE, Material.COBBLESTONE, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE);
                  }
               }
            }

            Wall w = (new Wall(new SimpleBlock(data, x, y - 1, z), farmHouse.getFace())).getRight();

            for(nz = 0; nz < 7; ++nz) {
               w = w.getFront();
            }

            while(!w.isSolid() || w.getType().toString().contains("PLANKS")) {
               Stairs stairs = (Stairs)Bukkit.createBlockData((Material)GenUtils.randChoice(random, Material.COBBLESTONE_STAIRS, Material.COBBLESTONE_STAIRS, Material.COBBLESTONE_STAIRS, Material.MOSSY_COBBLESTONE_STAIRS));
               stairs.setFacing(w.getDirection().getOppositeFace());
               w.getRight().setBlockData(stairs);
               w.setBlockData(stairs);
               w.getLeft().setBlockData(stairs);
               w.getLeft().getLeft().getUp().downUntilSolid(random, new Material[]{WoodUtils.getWoodForBiome(biome, WoodUtils.WoodType.LOG)});
               w.getLeft().getLeft().getUp(2).setType((Material)GenUtils.randChoice(random, Material.COBBLESTONE_WALL, Material.COBBLESTONE_WALL, Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL));
               w.getRight().getRight().getUp().downUntilSolid(random, new Material[]{WoodUtils.getWoodForBiome(biome, WoodUtils.WoodType.LOG)});
               w.getRight().getRight().getUp(2).setType((Material)GenUtils.randChoice(random, Material.COBBLESTONE_WALL, Material.COBBLESTONE_WALL, Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL));
               w = w.getFront().getDown();
            }

            this.createFields(tw, biome, random, data, x, y, z);
         } catch (Throwable var11) {
            TerraformGeneratorPlugin.logger.error("Something went wrong trying to place farmhouse at " + x + "," + y + "," + z + "!");
            TerraformGeneratorPlugin.logger.stackTrace(var11);
         }

      }
   }

   private void placeLamp(TerraformWorld tw, BiomeBank biome, @NotNull Random rand, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      if (TConfig.areDecorationsEnabled()) {
         SimpleBlock b = new SimpleBlock(data, x, y, z);
         b.setType((Material)GenUtils.randChoice(rand, Material.STONE_BRICKS, Material.MOSSY_STONE_BRICKS));
         b.getUp().setType((Material)GenUtils.randChoice(rand, Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL));
         b.getUp(2).setType((Material)GenUtils.randChoice(rand, Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL));
         b.getUp(3).setType((Material)GenUtils.randChoice(rand, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE));
         b.getUp(4).setType(Material.CAMPFIRE);
         b.getUp(5).setType((Material)GenUtils.randChoice(rand, Material.STONE_BRICKS, Material.MOSSY_STONE_BRICKS));
         BlockFace[] var9 = BlockUtils.directBlockFaces;
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            BlockFace face = var9[var11];
            Slab tSlab = (Slab)Bukkit.createBlockData((Material)GenUtils.randChoice(rand, Material.STONE_BRICK_SLAB, Material.MOSSY_STONE_BRICK_SLAB));
            tSlab.setType(Type.TOP);
            b.getRelative(face).getUp(3).setBlockData(tSlab);
            b.getRelative(face).getUp(4).setType((Material)GenUtils.randChoice(rand, Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL));
            b.getRelative(face).getUp(5).setType((Material)GenUtils.randChoice(rand, Material.STONE_BRICK_SLAB, Material.MOSSY_STONE_BRICK_SLAB));
         }

      }
   }

   private void createFields(@NotNull TerraformWorld tw, @NotNull BiomeBank biome, @NotNull Random random, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      FastNoise fieldNoise = new FastNoise(tw.getHashedRand(x, y, z, 23L).nextInt(225));
      fieldNoise.SetNoiseType(FastNoise.NoiseType.Simplex);
      fieldNoise.SetFrequency(0.05F);
      FastNoise radiusNoise = new FastNoise(tw.getHashedRand(x, y, z, 23L).nextInt(225));
      radiusNoise.SetNoiseType(FastNoise.NoiseType.Cubic);
      radiusNoise.SetFrequency(0.09F);
      Material cropOne = Material.WHEAT;
      Material cropTwo = Material.CARROTS;
      if (BiomeBank.getBiomeSectionFromBlockCoords(tw, x, z).getTemperature() <= -2.0F) {
         cropOne = Material.POTATOES;
         cropTwo = Material.BEETROOTS;
      }

      for(int nx = -50; nx <= 50; ++nx) {
         for(int nz = -50; nz <= 50; ++nz) {
            int height = GenUtils.getTrueHighestBlock(data, x + nx, z + nz);
            if (BlockUtils.isDirtLike(data.getType(x + nx, height, z + nz)) && data.getType(x + nx, height + 1, z + nz) == Material.AIR) {
               double noise = (double)fieldNoise.GetNoise((float)(nx + x), (float)(nz + z));
               double dist = Math.pow((double)nx, 2.0D) + Math.pow((double)nz, 2.0D);
               double multiplier = Math.pow(1.0D / (dist - 2500.0D) + 1.0D, 255.0D);
               if (multiplier < 0.0D || dist > 2500.0D + (double)radiusNoise.GetNoise((float)nx, (float)nz) * 500.0D) {
                  multiplier = 0.0D;
               }

               noise *= multiplier;
               if (dist < 2500.0D && GenUtils.chance(random, 1, 300)) {
                  data.setType(x + nx, height + 1, z + nz, Material.COMPOSTER);
               } else {
                  Farmland fl;
                  Ageable crop;
                  int var23;
                  BlockFace face;
                  BlockFace[] var25;
                  int var26;
                  if (noise < -0.2D) {
                     if (GenUtils.chance(random, 1, 15)) {
                        data.setType(nx + x, height, nz + z, Material.WATER);
                        var25 = BlockUtils.directBlockFaces;
                        var26 = var25.length;

                        for(var23 = 0; var23 < var26; ++var23) {
                           face = var25[var23];
                           BlockUtils.setDownUntilSolid(nx + x + face.getModX(), height, nz + z + face.getModZ(), data, Material.FARMLAND);
                        }
                     } else {
                        fl = (Farmland)Bukkit.createBlockData(Material.FARMLAND);
                        fl.setMoisture(fl.getMaximumMoisture());
                        data.setBlockData(nx + x, height, nz + z, fl);
                        crop = (Ageable)Bukkit.createBlockData(cropOne);
                        crop.setAge(GenUtils.randInt(random, 0, crop.getMaximumAge()));
                        if (!data.getType(nx + x, height + 1, nz + z).isSolid()) {
                           data.setBlockData(nx + x, height + 1, nz + z, crop);
                        }
                     }
                  } else if (noise > 0.2D) {
                     if (GenUtils.chance(random, 1, 15)) {
                        data.setType(nx + x, height, nz + z, Material.WATER);
                        var25 = BlockUtils.directBlockFaces;
                        var26 = var25.length;

                        for(var23 = 0; var23 < var26; ++var23) {
                           face = var25[var23];
                           BlockUtils.setDownUntilSolid(nx + x + face.getModX(), height, nz + z + face.getModZ(), data, Material.FARMLAND);
                        }
                     } else {
                        fl = (Farmland)Bukkit.createBlockData(Material.FARMLAND);
                        fl.setMoisture(fl.getMaximumMoisture());
                        data.setBlockData(nx + x, height, nz + z, fl);
                        crop = (Ageable)Bukkit.createBlockData(cropTwo);
                        crop.setAge(GenUtils.randInt(random, 0, crop.getMaximumAge()));
                        data.setBlockData(nx + x, height + 1, nz + z, crop);
                     }
                  } else if (Math.abs(noise) < 0.2D && Math.abs(noise) > 0.1D) {
                     BlockUtils.setPersistentLeaves(data, nx + x, height + 1, nz + z, WoodUtils.getWoodForBiome(biome, WoodUtils.WoodType.LEAVES));
                     if (GenUtils.chance(random, 1, 100)) {
                        this.placeLamp(tw, biome, random, data, nx + x, height + 1, z + nz);
                     }
                  } else if (GenUtils.chance(random, (int)(100.0D * Math.pow(multiplier, 3.0D)), 100)) {
                     data.setType(nx + x, height, nz + z, (Material)GenUtils.randChoice(random, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE));
                  }
               }
            }
         }
      }

   }
}
