package org.terraform.structure.small.ruinedportal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeClimate;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.MegaChunk;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.main.config.TConfig;
import org.terraform.structure.MultiMegaChunkStructurePopulator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.CylinderBuilder;
import org.terraform.utils.GenUtils;
import org.terraform.utils.StalactiteBuilder;
import org.terraform.utils.blockdata.ChestBuilder;
import org.terraform.utils.blockdata.StairBuilder;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class RuinedPortalPopulator extends MultiMegaChunkStructurePopulator {
   private static final Material[] portalBlocks;

   public void populate(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data) {
      if (this.isEnabled()) {
         Random random = this.getHashedRandom(tw, data.getChunkX(), data.getChunkZ());
         MegaChunk mc = new MegaChunk(data.getChunkX(), data.getChunkZ());
         int[][] var5 = this.getCoordsFromMegaChunk(tw, mc);
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            int[] coords = var5[var7];
            int x = coords[0];
            int z = coords[1];
            if (x >> 4 == data.getChunkX() && z >> 4 == data.getChunkZ()) {
               int height = GenUtils.getHighestGround(data, x, z);
               this.spawnRuinedPortal(tw, random, data, x, height + 1, z);
            }
         }

      }
   }

   public void spawnRuinedPortal(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      SimpleBlock core = new SimpleBlock(data, x, y, z);
      BiomeBank biome = tw.getBiomeBank(x, z);
      boolean overgrown = biome.getClimate() == BiomeClimate.HUMID_VEGETATION;
      boolean snowy = biome.getClimate() == BiomeClimate.SNOWY;
      int mossiness = 0;
      if (biome.getClimate() == BiomeClimate.HUMID_VEGETATION) {
         mossiness = 2;
      } else if (biome.getClimate() == BiomeClimate.DRY_VEGETATION || biome.getClimate() == BiomeClimate.TRANSITION) {
         mossiness = 1;
      }

      this.spawnRuinedPortal(tw, random, core.getUp(), mossiness, overgrown, snowy);
   }

   public void spawnRuinedPortal(TerraformWorld tw, @NotNull Random random, @NotNull SimpleBlock core, int mossiness, boolean overgrown, boolean snowy) {
      int horRadius = GenUtils.randInt(random, 2, 4);
      int vertHeight = 1 + horRadius * 2;
      BlockFace facing = BlockUtils.getDirectBlockFace(random);
      Wall w = new Wall(core, facing);
      Material lavaFluid = Material.LAVA;
      if (BlockUtils.isWet(core) || snowy || overgrown) {
         lavaFluid = Material.MAGMA_BLOCK;
      }

      BlockUtils.replaceCircularPatch(random.nextInt(99999), (float)horRadius * 2.5F, core.getDown(), snowy, Material.NETHERRACK);
      Material[] stoneBricks = BlockUtils.stoneBricks;
      if (mossiness == 0) {
         stoneBricks = new Material[]{Material.STONE_BRICKS, Material.CRACKED_STONE_BRICKS};
      }

      if (mossiness > 1) {
         stoneBricks = new Material[]{Material.STONE_BRICKS, Material.CRACKED_STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.MOSSY_STONE_BRICKS};
      }

      (new CylinderBuilder(random, w.getDown(), stoneBricks)).setRadius((float)(horRadius + 1)).setRY(1.0F).setHardReplace(false).build();
      Wall effectiveGround = w.getRight(horRadius).findFloor(10);
      int heightCorrection = vertHeight;
      if (effectiveGround != null) {
         if (effectiveGround.getY() >= w.getY() - 2) {
            effectiveGround = effectiveGround.getAtY(w.getY() - 2);
         } else {
            heightCorrection = vertHeight + (w.getY() - 2 - effectiveGround.getY());
         }

         (new StalactiteBuilder(portalBlocks)).setVerticalSpace(Math.round(2.5F * (float)heightCorrection * 1.5F)).setFacingUp(true).setSolidBlockType(portalBlocks).setMinRadius(2).build(random, effectiveGround);
      }

      effectiveGround = w.getLeft(horRadius + 1).findFloor(10);
      heightCorrection = vertHeight;
      if (effectiveGround != null) {
         if (effectiveGround.getY() >= w.getY() - 2) {
            effectiveGround = effectiveGround.getAtY(w.getY() - 2);
         } else {
            heightCorrection = vertHeight + (w.getY() - 2 - effectiveGround.getY());
         }

         (new StalactiteBuilder(portalBlocks)).setVerticalSpace(Math.round(2.5F * (float)heightCorrection * 1.5F)).setFacingUp(true).setSolidBlockType(portalBlocks).setMinRadius(2).build(random, effectiveGround);
      }

      int right;
      int depth;
      int height;
      Wall target;
      for(right = 0; right < horRadius; ++right) {
         w.getLeft(right).setType(portalBlocks);

         for(depth = -3; depth <= 3; ++depth) {
            for(height = 0; height < vertHeight - 2; ++height) {
               target = w.getFront(depth).getLeft(right).getUp(1 + height);
               target.setType(this.getFluid(target));
            }
         }

         w.getUp(vertHeight).getLeft(right).setType(portalBlocks);
         w.getUp(vertHeight + 1).getLeft(right).setType(new Material[]{Material.STONE_BRICK_SLAB, this.getFluid(w.getUp(vertHeight + 1).getLeft(right))});
      }

      for(right = 1; right < horRadius - 1; ++right) {
         w.getRight(right).setType(portalBlocks);

         for(depth = -3; depth <= 3; ++depth) {
            for(height = 0; height < vertHeight - 2; ++height) {
               target = w.getFront(depth).getRight(right).getUp(1 + height);
               target.setType(this.getFluid(target));
            }
         }

         w.getUp(vertHeight).getRight(right).setType(portalBlocks);
         w.getUp(vertHeight + 1).getRight(right).setType(new Material[]{Material.STONE_BRICK_SLAB, this.getFluid(w.getUp(vertHeight + 1).getRight(right))});
      }

      w.getLeft(horRadius).Pillar(1 + vertHeight, portalBlocks);
      w.getRight(horRadius - 1).Pillar(1 + vertHeight, portalBlocks);
      Wall rightCorner = w.getUp(vertHeight).getRight(horRadius - 1);
      Wall leftCorner = w.getUp(vertHeight).getLeft(horRadius);
      FastNoise noise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.STRUCTURE_RUINEDPORTAL_FISSURES, (world) -> {
         FastNoise n = new FastNoise((int)world.getSeed());
         n.SetNoiseType(FastNoise.NoiseType.PerlinFractal);
         n.SetFrequency(0.035F);
         n.SetFractalOctaves(5);
         return n;
      });
      HashMap<SimpleBlock, Integer> lavaLocs = new HashMap();
      int lowestY = 9999;

      int i;
      for(i = -horRadius * 3; i < horRadius * 3; ++i) {
         for(int relz = -horRadius * 3; relz < horRadius * 3; ++relz) {
            double fissureNoise = (double)(3.0F - 100.0F * Math.abs(noise.GetNoise((float)(i + core.getX()), (float)(relz + core.getZ()))));
            if (fissureNoise > 0.0D) {
               SimpleBlock target = core.getRelative(i, 0, relz).getGround();
               fissureNoise = (1.0D - Math.min(1.0D, target.distance(core) / (double)(horRadius * 3))) * fissureNoise;
               lavaLocs.put(target, (int)Math.round(fissureNoise));
               if (lowestY > target.getY()) {
                  lowestY = target.getY();
               }
            }
         }
      }

      Iterator var30 = lavaLocs.entrySet().iterator();

      while(var30.hasNext()) {
         Entry<SimpleBlock, Integer> entry = (Entry)var30.next();
         SimpleBlock target = (SimpleBlock)entry.getKey();
         int depth = (Integer)entry.getValue();
         int tempY = target.getY();
         target = target.getAtY(lowestY);

         int i;
         for(i = target.getY(); i <= tempY; ++i) {
            target.getAtY(i).setType(this.getFluid(target.getAtY(i)));
         }

         for(i = 0; i <= depth; ++i) {
            target.getDown(i).setType(lavaFluid);
         }
      }

      if (random.nextBoolean()) {
         for(i = 0; i < GenUtils.randInt(1, 3); ++i) {
            rightCorner.getDown(i).setType(this.getFluid(rightCorner.getDown(i)));
         }

         BlockUtils.dropDownBlock(rightCorner.getUp(), this.getFluid(rightCorner.getUp()));

         for(i = 1; i < GenUtils.randInt(random, 2, horRadius + 2); ++i) {
            rightCorner.getLeft(i).setType(this.getFluid(rightCorner.getLeft(i)));
            BlockUtils.dropDownBlock(rightCorner.getLeft(i).getUp(), this.getFluid(rightCorner.getLeft(i).getUp()));
         }

         if (overgrown && TConfig.arePlantsEnabled() && leftCorner.getRight().isSolid()) {
            leftCorner.getRight().getRear().downLPillar(random, GenUtils.randInt(vertHeight / 2, vertHeight - 1), new Material[]{Material.OAK_LEAVES});
            leftCorner.getRight().getFront().downLPillar(random, GenUtils.randInt(vertHeight / 2, vertHeight - 1), new Material[]{Material.OAK_LEAVES});
         }
      } else {
         for(i = 0; i < GenUtils.randInt(1, 3); ++i) {
            leftCorner.getDown(i).setType(this.getFluid(leftCorner.getDown(i)));
         }

         BlockUtils.dropDownBlock(leftCorner.getUp(), this.getFluid(leftCorner.getUp()));

         for(i = 1; i < GenUtils.randInt(random, 2, horRadius + 2); ++i) {
            leftCorner.getRight(i).setType(this.getFluid(leftCorner.getRight(i)));
            BlockUtils.dropDownBlock(leftCorner.getRight(i).getUp(), this.getFluid(leftCorner.getRight(i).getUp()));
         }

         if (overgrown && TConfig.arePlantsEnabled() && rightCorner.getLeft().isSolid()) {
            rightCorner.getLeft().getRear().downLPillar(random, GenUtils.randInt(vertHeight / 2, vertHeight - 1), new Material[]{Material.OAK_LEAVES});
            rightCorner.getLeft().getFront().downLPillar(random, GenUtils.randInt(vertHeight / 2, vertHeight - 1), new Material[]{Material.OAK_LEAVES});
         }
      }

      if (rightCorner.isSolid()) {
         (new StairBuilder(Material.STONE_BRICK_STAIRS)).setFacing(BlockUtils.getLeft(w.getDirection())).apply(rightCorner.getUp());
      }

      if (leftCorner.isSolid()) {
         (new StairBuilder(Material.STONE_BRICK_STAIRS)).setFacing(BlockUtils.getRight(w.getDirection())).apply(leftCorner.getUp());
      }

      if (w.getUp(vertHeight).isSolid()) {
         (new StairBuilder(Material.STONE_BRICK_STAIRS)).setFacing(BlockUtils.getLeft(w.getDirection())).apply(w.getUp(2 + vertHeight));
         w.getUp(1 + vertHeight).setType(new Material[]{Material.GOLD_BLOCK, Material.CHISELED_STONE_BRICKS});
      }

      if (w.getLeft().getUp(vertHeight).isSolid()) {
         (new StairBuilder(Material.STONE_BRICK_STAIRS)).setFacing(BlockUtils.getRight(w.getDirection())).apply(w.getLeft().getUp(2 + vertHeight));
         w.getLeft().getUp(1 + vertHeight).setType(new Material[]{Material.GOLD_BLOCK, Material.CHISELED_STONE_BRICKS});
      }

      (new ChestBuilder(Material.CHEST)).setFacing(w.getDirection()).setLootTable(TerraLootTable.RUINED_PORTAL).apply(w.getFront(GenUtils.randInt(3, (int)((float)horRadius * 1.5F))).getRight(GenUtils.randInt(3, (int)((float)horRadius * 1.5F))).getGround().getUp());
   }

   @NotNull
   private Material getFluid(@NotNull SimpleBlock block) {
      if (BlockUtils.isWet(block)) {
         return Material.WATER;
      } else {
         BlockFace[] var2 = BlockUtils.directBlockFaces;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            BlockFace face = var2[var4];
            if (BlockUtils.isWet(block.getRelative(face))) {
               return Material.WATER;
            }
         }

         return Material.AIR;
      }
   }

   public int[][] getCoordsFromMegaChunk(@NotNull TerraformWorld tw, @NotNull MegaChunk mc) {
      int num = TConfig.c.STRUCTURES_RUINEDPORTAL_COUNT_PER_MEGACHUNK;
      int[][] coords = new int[num][2];

      for(int i = 0; i < num; ++i) {
         coords[i] = mc.getRandomCoords(tw.getHashedRand((long)mc.getX(), mc.getZ(), 4363463 * (1 + i)));
      }

      return coords;
   }

   public int[] getNearestFeature(@NotNull TerraformWorld tw, int rawX, int rawZ) {
      MegaChunk mc = new MegaChunk(rawX, 0, rawZ);
      double minDistanceSquared = 2.147483647E9D;
      int[] min = null;

      for(int nx = -1; nx <= 1; ++nx) {
         for(int nz = -1; nz <= 1; ++nz) {
            int[][] var10 = this.getCoordsFromMegaChunk(tw, mc);
            int var11 = var10.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               int[] loc = var10[var12];
               double distSqr = Math.pow((double)(loc[0] - rawX), 2.0D) + Math.pow((double)(loc[1] - rawZ), 2.0D);
               if (distSqr < minDistanceSquared) {
                  minDistanceSquared = distSqr;
                  min = loc;
               }
            }
         }
      }

      return min;
   }

   private boolean rollSpawnRatio(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return GenUtils.chance(tw.getHashedRand((long)chunkX, chunkZ, 976123), (int)(TConfig.c.STRUCTURES_RUINEDPORTAL_SPAWNRATIO * 10000.0D), 10000);
   }

   public boolean canSpawn(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      if (!this.isEnabled()) {
         return false;
      } else {
         MegaChunk mc = new MegaChunk(chunkX, chunkZ);
         int[][] var5 = this.getCoordsFromMegaChunk(tw, mc);
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            int[] coords = var5[var7];
            if (coords[0] >> 4 == chunkX && coords[1] >> 4 == chunkZ) {
               return this.rollSpawnRatio(tw, chunkX, chunkZ);
            }
         }

         return false;
      }
   }

   @NotNull
   public Random getHashedRandom(@NotNull TerraformWorld world, int chunkX, int chunkZ) {
      return world.getHashedRand(729384234L, chunkX, chunkZ);
   }

   public boolean isEnabled() {
      return TConfig.areStructuresEnabled() && TConfig.c.STRUCTURES_RUINEDPORTAL_ENABLED;
   }

   public int getChunkBufferDistance() {
      return 1;
   }

   static {
      portalBlocks = new Material[]{Material.OBSIDIAN, Material.CRYING_OBSIDIAN};
   }
}
