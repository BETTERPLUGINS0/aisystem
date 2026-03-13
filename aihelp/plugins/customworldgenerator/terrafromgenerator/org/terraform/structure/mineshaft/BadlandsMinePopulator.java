package org.terraform.structure.mineshaft;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Lantern;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.Slab.Type;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.MegaChunk;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.schematic.TerraSchematic;
import org.terraform.structure.JigsawState;
import org.terraform.structure.JigsawStructurePopulator;
import org.terraform.structure.room.LegacyPathGenerator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class BadlandsMinePopulator extends JigsawStructurePopulator {
   static final int shaftDepth;

   public boolean canSpawn(@NotNull TerraformWorld tw, int chunkX, int chunkZ, BiomeBank biome) {
      if (!this.isEnabled()) {
         return false;
      } else {
         return biome != BiomeBank.BADLANDS_CANYON ? false : this.rollSpawnRatio(tw, chunkX, chunkZ);
      }
   }

   private boolean rollSpawnRatio(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return GenUtils.chance(tw.getHashedRand((long)chunkX, chunkZ, 12222), (int)(TConfig.c.STRUCTURES_MINESHAFT_SPAWNRATIO * 10000.0D), 10000);
   }

   @NotNull
   public Random getHashedRandom(@NotNull TerraformWorld world, int chunkX, int chunkZ) {
      return world.getHashedRand(18239211L, chunkX, chunkZ);
   }

   public boolean isEnabled() {
      return TConfig.areStructuresEnabled() && BiomeBank.isBiomeEnabled(BiomeBank.BADLANDS_CANYON) && TConfig.c.STRUCTURES_BADLANDS_MINE_ENABLED;
   }

   @NotNull
   public JigsawState calculateRoomPopulators(@NotNull TerraformWorld tw, @NotNull MegaChunk mc) {
      return (new MineshaftPopulator()).calculateRoomPopulators(tw, mc, true);
   }

   public void populate(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data) {
      if (this.isEnabled()) {
         MegaChunk mc = new MegaChunk(data.getChunkX(), data.getChunkZ());
         int[] spawnCoords = mc.getCenterBiomeSectionBlockCoords();
         SimpleBlock spawnSpot = (new SimpleBlock(data, spawnCoords[0], 0, spawnCoords[1])).getGround();
         BlockFace inDir = BlockUtils.getDirectBlockFace(this.getHashedRandom(tw, data.getChunkX(), data.getChunkZ()));
         BlockFace outDir = inDir.getOppositeFace();
         SimpleBlock entrance = this.getSpawnEntrance(tw, spawnSpot, outDir);
         SimpleBlock shaft = spawnSpot.getAtY(entrance.getY());
         int hallwayLength;
         if (BlockUtils.getAxisFromBlockFace(inDir) == Axis.X) {
            hallwayLength = Math.abs(shaft.getX() - entrance.getX());
         } else {
            hallwayLength = Math.abs(shaft.getZ() - entrance.getZ());
         }

         hallwayLength -= 6;
         TerraformGeneratorPlugin.logger.info("Badlands Mineshaft Entrance: " + String.valueOf(entrance));
         TerraformGeneratorPlugin.logger.info("Badlands Mineshaft Shaft: " + String.valueOf(shaft));
         TerraformGeneratorPlugin.logger.info("Badlands Mineshaft Hallway Length: " + hallwayLength);
         Random random = tw.getHashedRand(entrance.getX(), entrance.getY(), entrance.getZ(), 4L);
         this.spawnShaft(random, shaft, inDir);
         LegacyPathGenerator g = new LegacyPathGenerator(entrance.getRelative(inDir.getModX() * 3, -1, inDir.getModZ() * 3), new Material[]{Material.CAVE_AIR}, new Random(), new int[]{0, 0}, new int[]{0, 0}, -1);
         g.setPopulator(new BadlandsMineshaftPathPopulator(random));
         g.generateStraightPath((SimpleBlock)null, inDir, hallwayLength);
         this.spawnEntrance(entrance.getRelative(inDir, 5), outDir);
         this.patchEntrance(entrance, inDir);
         if (GenUtils.chance(random, 4, 5)) {
            try {
               TerraSchematic schema = TerraSchematic.load("ore-lift", new SimpleBlock(data, shaft.getX() - 1, shaft.getY() - shaftDepth, shaft.getZ() - 1));
               schema.parser = new OreLiftSchematicParser(true);
               schema.setFace(BlockFace.NORTH);
               schema.apply();
            } catch (FileNotFoundException var14) {
               TerraformGeneratorPlugin.logger.stackTrace(var14);
            }
         }

      }
   }

   @NotNull
   SimpleBlock getSpawnEntrance(TerraformWorld tw, @NotNull SimpleBlock query, @NotNull BlockFace dir) {
      while(query.getGround().getY() >= 100) {
         query = query.getRelative(dir);
      }

      double riverHeight = HeightMap.getRawRiverDepth(tw, query.getX(), query.getZ());

      for(double baseHeight = HeightMap.CORE.getHeight(tw, query.getX(), query.getZ()) + riverHeight; (double)query.getGround().getY() > baseHeight + 3.0D; baseHeight = HeightMap.CORE.getHeight(tw, query.getX(), query.getZ()) + riverHeight) {
         query = query.getRelative(dir);
         riverHeight = HeightMap.getRawRiverDepth(tw, query.getX(), query.getZ());
      }

      return query.getGround();
   }

   void spawnEntrance(SimpleBlock entrance, @NotNull BlockFace direction) {
      entrance = entrance.getRelative(direction.getModX(), -1, direction.getModZ());

      try {
         SimpleBlock framePos = entrance.getRelative(BlockUtils.getRight(direction), 1).getRelative(direction);
         TerraSchematic entranceSchematic = TerraSchematic.load("badlands-mineshaft/badlands-mine-entrance", framePos);
         entranceSchematic.parser = new BadlandsMineEntranceParser();
         entranceSchematic.setFace(direction);
         entranceSchematic.apply();
      } catch (Exception var5) {
         TerraformGeneratorPlugin.logger.error("An error occurred reading Badlands Mine Entrance schematic file.");
         TerraformGeneratorPlugin.logger.stackTrace(var5);
      }

   }

   void patchEntrance(@NotNull SimpleBlock entrance, @NotNull BlockFace direction) {
      BlockFace nextDir = BlockUtils.getRight(direction);
      this.fillWithBlock(entrance.getRelative(nextDir.getModX() * 2, -1, nextDir.getModZ() * 2), entrance.getRelative(-nextDir.getModX() * 2, -4, -nextDir.getModZ() * 2).getRelative(direction, 3));
   }

   void fillWithBlock(@NotNull SimpleBlock start, @NotNull SimpleBlock end) {
      for(int x = Math.min(start.getX(), end.getX()); x <= Math.min(start.getX(), end.getX()) + Math.abs(start.getX() - end.getX()); ++x) {
         for(int z = Math.min(start.getZ(), end.getZ()); z <= Math.min(start.getZ(), end.getZ()) + Math.abs(start.getZ() - end.getZ()); ++z) {
            for(int y = Math.min(start.getY(), end.getY()); y <= Math.min(start.getY(), end.getY()) + Math.abs(start.getY() - end.getY()); ++y) {
               (new SimpleBlock(start.getPopData(), x, y, z)).lsetType(Material.RED_SAND);
            }
         }
      }

   }

   private void spawnShaft(@NotNull Random random, @NotNull SimpleBlock shaft, @NotNull BlockFace inDir) {
      BlockFace outDir = inDir.getOppositeFace();
      int mineshaftY = (int)(HeightMap.CORE.getHeight(shaft.getPopData().getTerraformWorld(), shaft.getX(), shaft.getZ()) - (double)shaftDepth);
      int shaftStart = -5;
      int supportR = 3;
      EnumSet<Material> toReplace = EnumSet.copyOf(BlockUtils.badlandsStoneLike);
      toReplace.addAll(Arrays.asList(Material.STONE_SLAB, Material.MOSSY_COBBLESTONE_WALL, Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE, Material.COBWEB, Material.MOSSY_COBBLESTONE_SLAB, Material.COBBLESTONE_SLAB));
      BlockUtils.carveCaveAir(random.nextInt(777123), 2.75F, 4.5F, 2.75F, shaft, true, toReplace);
      ArrayList<SimpleBlock> platforms = new ArrayList();

      int b;
      int y;
      for(double i = 0.0D; i < (double)(shaft.getY() - mineshaftY); ++i) {
         double width = 6.0D + Math.pow(i % 6.0D * 0.2D, 2.0D);
         SimpleBlock centerBlock = shaft.getRelative(GenUtils.randInt(random, -1, 1), (int)Math.round(-i + (double)shaftStart), GenUtils.randInt(random, -1, 1));
         BlockUtils.carveCaveAir(random.nextInt(777123), (float)width / 2.0F, 2.0F, (float)width / 2.0F, centerBlock, true, toReplace);
         if (i % 6.0D > 4.0D && i < (double)(shaft.getY() - mineshaftY - 6)) {
            for(b = 0; b < 1; ++b) {
               double angle = GenUtils.randDouble(random, 0.0D, 6.283185307179586D);
               y = (int)Math.round(Math.sin(angle) * 3.0D);
               int zAdd = (int)Math.round(Math.cos(angle) * 3.0D);
               SimpleBlock platform = centerBlock.getRelative(y, 0, zAdd);
               platform = GenUtils.getTrueHighestBlockBelow(platform);
               platforms.add(platform);
            }
         }
      }

      Iterator var25 = platforms.iterator();

      while(var25.hasNext()) {
         SimpleBlock platform = (SimpleBlock)var25.next();
         if (GenUtils.chance(random, 3, 4)) {
            this.spawnShaftPlatform(platform);
         }
      }

      BlockFace right = BlockUtils.getRight(inDir);
      BlockFace left = BlockUtils.getLeft(inDir);
      ArrayList<SimpleBlock> mainPillars = new ArrayList();
      mainPillars.add(shaft.getRelative(inDir.getModX() * supportR, shaftStart, inDir.getModZ() * supportR).getRelative(right, supportR));
      mainPillars.add(shaft.getRelative(inDir.getModX() * supportR, shaftStart, inDir.getModZ() * supportR).getRelative(left, supportR));
      mainPillars.add(shaft.getRelative(outDir.getModX() * supportR, shaftStart, outDir.getModZ() * supportR).getRelative(left, supportR));
      mainPillars.add(shaft.getRelative(outDir.getModX() * supportR, shaftStart, outDir.getModZ() * supportR).getRelative(right, supportR));
      ArrayList<SimpleBlock> supportPillars = new ArrayList(mainPillars);
      int xAdd = GenUtils.randInt(random, -supportR, supportR);
      b = (supportR - Math.abs(xAdd)) * (random.nextBoolean() ? 1 : -1);
      supportPillars.add(shaft.getRelative(outDir.getModX() * xAdd, shaftStart, outDir.getModZ() * xAdd).getRelative(left, b));
      xAdd = GenUtils.randInt(random, -supportR, supportR);
      b = (supportR - Math.abs(xAdd)) * (random.nextBoolean() ? 1 : -1);
      supportPillars.add(shaft.getRelative(outDir.getModX() * xAdd, shaftStart, outDir.getModZ() * xAdd).getRelative(left, b));
      supportPillars.removeIf((n) -> {
         return GenUtils.chance(random, 1, 5);
      });
      Iterator var30 = supportPillars.iterator();

      SimpleBlock platform;
      while(var30.hasNext()) {
         platform = (SimpleBlock)var30.next();

         for(y = -4; y < shaft.getY() - mineshaftY + 5; ++y) {
            platform.getRelative(0, -y, 0).lsetType(Material.DARK_OAK_FENCE);
         }
      }

      var30 = platforms.iterator();

      while(var30.hasNext()) {
         platform = (SimpleBlock)var30.next();
         y = platform.getY();
         BlockFace face = BlockUtils.getLeft(inDir);

         for(int i = 0; i < mainPillars.size(); ++i) {
            if (supportPillars.contains(mainPillars.get(i)) && supportPillars.contains(mainPillars.get(i + 1 >= mainPillars.size() ? 0 : i + 1)) && GenUtils.chance(random, 2, 3)) {
               SimpleBlock mainPillar = (SimpleBlock)mainPillars.get(i);

               for(int add = 1; add < 2 * supportR; ++add) {
                  SimpleBlock b = (new SimpleBlock(platform.getPopData(), mainPillar.getX(), y, mainPillar.getZ())).getRelative(face, add);
                  if (b.getType() == Material.STONE || BlockUtils.isAir(b.getType())) {
                     b.setType(Material.DARK_OAK_FENCE);
                     BlockUtils.correctSurroundingMultifacingData(b);
                     if (GenUtils.chance(random, 1, 12) && !b.getDown().isSolid()) {
                        Lantern l = (Lantern)Bukkit.createBlockData(Material.LANTERN);
                        l.setHanging(true);
                        b.getDown().setBlockData(l);
                     }
                  }
               }
            }

            face = BlockUtils.getLeft(face);
         }
      }

   }

   private void spawnShaftPlatform(@NotNull SimpleBlock center) {
      BlockUtils.carveCaveAir((new Random()).nextInt(777123), 2.5F, 1.5F, 2.5F, center.getUp(2), true, BlockUtils.badlandsStoneLike);
      center.setType(Material.DARK_OAK_PLANKS);
      ArrayList<SimpleBlock> lootBlocks = new ArrayList();
      lootBlocks.add(center.getUp());
      BlockFace[] var3 = BlockUtils.directBlockFaces;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         BlockFace face = var3[var5];
         center.getRelative(face).setType(Material.DARK_OAK_PLANKS);
         lootBlocks.add(center.getRelative(face).getUp());
         Stairs stairs = (Stairs)Bukkit.createBlockData(Material.DARK_OAK_STAIRS);
         stairs.setHalf(Half.TOP);
         stairs.setFacing(face.getOppositeFace());
         center.getRelative(face, 2).setBlockData(stairs);
         SimpleBlock lantern = center.getRelative(face, 2).getUp();
         if (BlockUtils.isAir(lantern.getType()) && GenUtils.chance(1, 4)) {
            lantern.setType(Material.LANTERN);
         }

         Slab slab = (Slab)Bukkit.createBlockData(Material.DARK_OAK_SLAB);
         slab.setType(Type.TOP);
         center.getRelative(face).getRelative(BlockUtils.getRight(face)).setBlockData(slab);
         lootBlocks.add(center.getRelative(face).getRelative(BlockUtils.getRight(face)).getUp());
      }

      Iterator var10 = lootBlocks.iterator();

      while(var10.hasNext()) {
         SimpleBlock lootBlock = (SimpleBlock)var10.next();
         if (GenUtils.chance(9, 10)) {
            this.setLootBlock(lootBlock);
            if (GenUtils.chance(4, 10)) {
               this.setLootBlock(lootBlock.getUp());
            }
         }
      }

   }

   private void setLootBlock(@NotNull SimpleBlock lootBlock) {
      if (GenUtils.chance(1, 25) && !lootBlock.isSolid()) {
         lootBlock.setType(Material.BARREL);
         lootBlock.setBlockData(BlockUtils.getRandomBarrel());
         lootBlock.getPopData().lootTableChest(lootBlock.getX(), lootBlock.getY(), lootBlock.getZ(), TerraLootTable.ABANDONED_MINESHAFT);
      } else {
         lootBlock.lsetType((Material)GenUtils.randChoice(BlockUtils.ores));
      }

   }

   static {
      shaftDepth = TConfig.c.STRUCTURES_BADLANDS_MINE_DEPTH;
   }
}
