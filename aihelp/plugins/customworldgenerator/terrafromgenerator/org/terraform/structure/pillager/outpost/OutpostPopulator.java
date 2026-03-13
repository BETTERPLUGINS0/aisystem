package org.terraform.structure.pillager.outpost;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Slab.Type;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.NaturalSpawnType;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.coregen.populatordata.PopulatorDataPostGen;
import org.terraform.data.MegaChunk;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.schematic.TerraSchematic;
import org.terraform.structure.SingleMegaChunkStructurePopulator;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomLayout;
import org.terraform.structure.room.RoomLayoutGenerator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.WoodUtils;
import org.terraform.utils.blockdata.SlabBuilder;
import org.terraform.utils.blockdata.StairBuilder;
import org.terraform.utils.noise.FastNoise;

public class OutpostPopulator extends SingleMegaChunkStructurePopulator {
   private Material[] stakeGravel;

   private static void setupPillagerSpawns(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      int i = -5;
      ArrayList<Integer> done = new ArrayList();

      for(int nx = x - 40 - i; nx <= x + 40 + i; ++nx) {
         for(int nz = z - 40 - i; nz <= z + 40 + i; ++nz) {
            int chunkX = nx >> 4;
            int chunkZ = nz >> 4;
            int hash = Objects.hash(new Object[]{chunkX, chunkZ});
            if (!done.contains(hash)) {
               done.add(hash);
               TerraformGeneratorPlugin.injector.getICAData(((PopulatorDataPostGen)data).getWorld().getChunkAt(chunkX, chunkZ)).registerNaturalSpawns(NaturalSpawnType.PILLAGER, x - 40, y, z - 40, x + 40, 255, z + 40);
            }
         }
      }

   }

   public void populate(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data) {
      if (this.isEnabled()) {
         MegaChunk mc = new MegaChunk(data.getChunkX(), data.getChunkZ());
         int[] coords = mc.getCenterBiomeSectionBlockCoords();
         int x = coords[0];
         int z = coords[1];
         int height = (new SimpleBlock(data, x, 0, z)).getGroundOrSeaLevel().getY();
         this.spawnOutpost(tw, this.getHashedRandom(tw, data.getChunkX(), data.getChunkZ()), data, x, height + 1, z);
      }
   }

   public void spawnOutpost(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      try {
         TerraformGeneratorPlugin.logger.info("Spawning outpost at " + x + "," + y + "," + z);
         BiomeBank biome = tw.getBiomeBank(x, z);
         this.stakeGravel = new Material[]{Material.COBBLESTONE, Material.STONE, Material.ANDESITE, Material.GRAVEL};
         if (biome == BiomeBank.BADLANDS) {
            this.stakeGravel = new Material[]{Material.TERRACOTTA, Material.RED_TERRACOTTA, Material.BROWN_TERRACOTTA, Material.YELLOW_TERRACOTTA, Material.ORANGE_TERRACOTTA};
         }

         this.spawnStakes(random, new SimpleBlock(data, x, y, z), biome);
         TerraSchematic outpostBase = TerraSchematic.load("outpost/outpostbase1", new SimpleBlock(data, x, y, z));
         outpostBase.parser = new OutpostSchematicParser(biome, random, data, y - 1);
         outpostBase.setFace(BlockUtils.getDirectBlockFace(random));
         outpostBase.apply();
         TerraSchematic outpostCore = TerraSchematic.load("outpost/outpostcore1", new SimpleBlock(data, x, y + 5, z));
         outpostCore.parser = new OutpostSchematicParser(biome, random, data, y - 1);
         outpostCore.setFace(BlockUtils.getDirectBlockFace(random));
         outpostCore.apply();
         TerraSchematic outpostTop = TerraSchematic.load("outpost/outposttop1", new SimpleBlock(data, x, y + 11, z));
         outpostTop.parser = new OutpostSchematicParser(biome, random, data, y - 1);
         outpostTop.setFace(BlockUtils.getDirectBlockFace(random));
         outpostTop.apply();
         this.spawnStairway(random, biome, new SimpleBlock(data, x, y, z), 11);
         RoomLayoutGenerator propGenerator = new RoomLayoutGenerator(random, RoomLayout.RANDOM_BRUTEFORCE, 100, x, y, z, 35);
         propGenerator.setRoomMinX(12);
         propGenerator.setRoomMinZ(12);
         propGenerator.setRoomMaxX(12);
         propGenerator.setRoomMaxZ(12);
         CubeRoom placeholder = new CubeRoom(20, 20, 15, x, y, z);
         propGenerator.getRooms().add(placeholder);
         propGenerator.registerRoomPopulator(new OutpostTent(random, false, false, biome));
         propGenerator.registerRoomPopulator(new OutpostCampfire(random, false, true));
         propGenerator.registerRoomPopulator(new OutpostLogpile(random, false, true, biome));
         propGenerator.registerRoomPopulator(new OutpostStakeCage(random, false, true, biome, this.stakeGravel));
         propGenerator.setGenPaths(false);
         propGenerator.calculateRoomPlacement();
         propGenerator.getRooms().remove(placeholder);
         propGenerator.runRoomPopulators(data, tw);
         setupPillagerSpawns(data, x, y, z);
      } catch (Throwable var13) {
         TerraformGeneratorPlugin.logger.error("Something went wrong trying to place outpost at " + x + "," + y + "," + z + "!");
         TerraformGeneratorPlugin.logger.stackTrace(var13);
      }

   }

   public void spawnStakes(@NotNull Random rand, @NotNull SimpleBlock center, @NotNull BiomeBank bank) {
      int radius = 40;
      Material planksMat = WoodUtils.getWoodForBiome(bank, WoodUtils.WoodType.PLANKS);
      FastNoise noise = new FastNoise(rand.nextInt(5000));
      noise.SetNoiseType(FastNoise.NoiseType.Simplex);
      noise.SetFrequency(0.09F);

      for(float x = (float)(-radius); x <= (float)radius; ++x) {
         for(float z = (float)(-radius); z <= (float)radius; ++z) {
            SimpleBlock rel = center.getRelative(Math.round(x), 0, Math.round(z)).getGroundOrSeaLevel();
            double mainRadiusResult = Math.pow((double)x, 2.0D) / Math.pow((double)radius, 2.0D) + Math.pow((double)z, 2.0D) / Math.pow((double)radius, 2.0D);
            double secondaryRadiusResult = mainRadiusResult * 1.3D;
            double noiseVal = (double)noise.GetNoise((float)rel.getX(), (float)rel.getZ());
            if (mainRadiusResult <= 1.0D + 0.7D * noiseVal) {
               rel.lsetType(planksMat);
               if (secondaryRadiusResult > 1.0D + 0.7D * noiseVal) {
                  BlockUtils.replaceCircularPatch(rand.nextInt(4211), 1.5F, rel, this.stakeGravel);
                  this.spawnOneStake(rand, rel.getUp(), bank, this.stakeGravel);
               }
            }
         }
      }

   }

   public void spawnOneStake(@NotNull Random rand, @NotNull SimpleBlock base, @NotNull BiomeBank bank, Material... stakeGravel) {
      WoodUtils.WoodType type = (new WoodUtils.WoodType[]{WoodUtils.WoodType.LOG, WoodUtils.WoodType.STRIPPED_LOG})[rand.nextInt(2)];
      Wall w = new Wall(base);
      BlockFace[] var7 = BlockUtils.xzPlaneBlockFaces;
      int var8 = var7.length;

      int var9;
      for(var9 = 0; var9 < var8; ++var9) {
         BlockFace face = var7[var9];
         if (Tag.LOGS.isTagged(base.getRelative(face).getType())) {
            return;
         }
      }

      int h = GenUtils.randInt(3, 5);
      BlockFace[] var13 = BlockUtils.directBlockFaces;
      var9 = var13.length;

      for(int var14 = 0; var14 < var9; ++var14) {
         BlockFace face = var13[var14];
         if (rand.nextBoolean()) {
            w.getRelative(face).downUntilSolid(rand, stakeGravel);
         }
      }

      w.Pillar(h, rand, new Material[]{WoodUtils.getWoodForBiome(bank, type)});
      w.getRelative(0, h, 0).Pillar(GenUtils.randInt(1, 2), rand, new Material[]{WoodUtils.getWoodForBiome(bank, WoodUtils.WoodType.FENCE)});
      w.getDown().downUntilSolid(rand, stakeGravel);
      w.getDown(2).downUntilSolid(rand, stakeGravel);
   }

   public void spawnStairway(Random rand, @NotNull BiomeBank biome, @NotNull SimpleBlock core, int height) {
      Material pillarMat = (Material)GenUtils.randChoice((Object[])(WoodUtils.getWoodForBiome(biome, WoodUtils.WoodType.LOG), Material.COBBLESTONE));
      Material stair = WoodUtils.getWoodForBiome(biome, WoodUtils.WoodType.STAIRS);
      Material slab = WoodUtils.getWoodForBiome(biome, WoodUtils.WoodType.SLAB);
      BlockFace face = BlockFace.NORTH;

      for(int i = 0; i < height; ++i) {
         core.setType(pillarMat);
         BlockFace[] var10 = BlockUtils.xzPlaneBlockFaces;
         int var11 = var10.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            BlockFace adj = var10[var12];
            core.getRelative(adj).setType(Material.AIR);
         }

         (new StairBuilder(stair)).setFacing(BlockUtils.getLeft(face)).apply(core.getRelative(face));
         (new SlabBuilder(slab)).setType(Type.TOP).apply(core.getRelative(BlockUtils.rotateXZPlaneBlockFace(face, 1)));
         core = core.getUp();
         face = BlockUtils.rotateFace(face, 1);
      }

   }

   public boolean canSpawn(@NotNull TerraformWorld tw, int chunkX, int chunkZ, @NotNull BiomeBank biome) {
      if (!this.isEnabled()) {
         return false;
      } else if (!biome.isDry()) {
         return false;
      } else {
         return biome != BiomeBank.DESERT && biome != BiomeBank.SNOWY_WASTELAND && biome != BiomeBank.BADLANDS && biome != BiomeBank.BAMBOO_FOREST ? false : this.rollSpawnRatio(tw, chunkX, chunkZ);
      }
   }

   private boolean rollSpawnRatio(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return GenUtils.chance(tw.getHashedRand((long)chunkX, chunkZ, 92992), (int)(TConfig.c.STRUCTURES_OUTPOST_SPAWNRATIO * 10000.0D), 10000);
   }

   public boolean isEnabled() {
      return TConfig.areStructuresEnabled() && TConfig.c.STRUCTURES_OUTPOST_ENABLED;
   }

   @NotNull
   public Random getHashedRandom(@NotNull TerraformWorld world, int chunkX, int chunkZ) {
      return world.getHashedRand(81903212L, chunkX, chunkZ);
   }
}
