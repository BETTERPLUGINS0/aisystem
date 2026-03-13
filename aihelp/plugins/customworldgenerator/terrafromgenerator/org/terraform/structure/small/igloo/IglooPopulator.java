package org.terraform.structure.small.igloo;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Lantern;
import org.bukkit.block.data.type.Slab.Type;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeClimate;
import org.terraform.biome.BiomeType;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.MegaChunk;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.structure.MultiMegaChunkStructurePopulator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.CylinderBuilder;
import org.terraform.utils.GenUtils;
import org.terraform.utils.SphereBuilder;
import org.terraform.utils.StairwayBuilder;
import org.terraform.utils.blockdata.BarrelBuilder;
import org.terraform.utils.blockdata.DirectionalBuilder;
import org.terraform.utils.blockdata.OrientableBuilder;
import org.terraform.utils.blockdata.SlabBuilder;
import org.terraform.utils.blockdata.StairBuilder;
import org.terraform.utils.blockdata.TrapdoorBuilder;

public class IglooPopulator extends MultiMegaChunkStructurePopulator {
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
               this.spawnIgloo(tw, random, data, x, height + 1, z);
            }
         }

      }
   }

   public void spawnIgloo(TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      Wall core = new Wall(data, x, y, z, BlockUtils.getDirectBlockFace(random));
      int size = GenUtils.randInt(random, 4, 7);
      TerraformGeneratorPlugin.logger.info("Placing igloo of size " + size);
      (new CylinderBuilder(random, core.getDown(), new Material[]{Material.SPRUCE_PLANKS})).setHardReplace(false).setRX((float)size * 1.5F).setRY(0.5F).setRZ((float)size * 1.5F).setMinRadius(1.0F).setSingleBlockY(true).build();
      (new SphereBuilder(random, core, new Material[]{Material.SNOW_BLOCK})).setSphereType(SphereBuilder.SphereType.UPPER_SEMISPHERE).setRadius((float)size).setSmooth(true).build();
      (new SphereBuilder(random, core, new Material[]{Material.AIR})).setSphereType(SphereBuilder.SphereType.UPPER_SEMISPHERE).setRadius((float)(size - 1)).setSmooth(true).setHardReplace(true).build();
      this.spawnSpire(core.getRelative(size - 1, 0, size - 1));
      this.spawnSpire(core.getRelative(-size + 1, 0, size - 1));
      this.spawnSpire(core.getRelative(-size + 1, 0, -size + 1));
      this.spawnSpire(core.getRelative(size - 1, 0, -size + 1));
      core.getUp(size + 1).setType(Material.SPRUCE_SLAB);
      this.spawnTrapdoorDecors(new Wall(core.getUp(size), BlockFace.NORTH), size);
      this.spawnTrapdoorDecors(new Wall(core.getUp(size), BlockFace.SOUTH), size);
      this.spawnTrapdoorDecors(new Wall(core.getUp(size), BlockFace.EAST), size);
      this.spawnTrapdoorDecors(new Wall(core.getUp(size), BlockFace.WEST), size);
      core.getFront(size + 1).getUp().setType(Material.AIR);
      core.getFront(size + 1).setType(Material.AIR);
      core.getFront(size).setType(Material.AIR);
      core.getFront(size).getUp().setType(Material.AIR);
      core.getFront(size - 1).setType(Material.AIR);
      core.getFront(size - 1).getUp().setType(Material.AIR);
      BlockUtils.placeDoor(data, Material.SPRUCE_DOOR, core.getFront(size - 1));
      Wall entranceCore = core.getFront(size);
      entranceCore.getLeft().Pillar(2, new Material[]{Material.SPRUCE_LOG});
      entranceCore.getRight().Pillar(2, new Material[]{Material.SPRUCE_LOG});
      entranceCore.getFront().getLeft().setType(Material.SPRUCE_PLANKS);
      entranceCore.getFront().getRight().setType(Material.SPRUCE_PLANKS);
      (new OrientableBuilder(Material.SPRUCE_LOG)).setAxis(BlockUtils.getAxisFromBlockFace(core.getDirection())).apply(entranceCore.getUp(2));
      (new StairBuilder(Material.SPRUCE_STAIRS)).setFacing(core.getDirection().getOppositeFace()).apply(entranceCore.getFront().getLeft().getUp()).apply(entranceCore.getFront().getRight().getUp()).setFacing(BlockUtils.getLeft(core.getDirection())).apply(entranceCore.getRight().getUp(2)).setFacing(BlockUtils.getRight(core.getDirection())).apply(entranceCore.getLeft().getUp(2));
      (new TrapdoorBuilder(Material.SPRUCE_TRAPDOOR)).setOpen(true).setFacing(BlockUtils.getLeft(core.getDirection())).apply(entranceCore.getLeft(2)).apply(entranceCore.getLeft(2).getUp()).setFacing(BlockUtils.getRight(core.getDirection())).apply(entranceCore.getRight(2)).apply(entranceCore.getRight(2).getUp());
      if (entranceCore.getFront(2).isSolid()) {
         (new StairwayBuilder(new Material[]{Material.COBBLESTONE_STAIRS, Material.MOSSY_COBBLESTONE_STAIRS})).setAngled(true).setStopAtWater(true).setStairwayDirection(BlockFace.UP).build(entranceCore.getFront(4));
         entranceCore.getFront(2).Pillar(2, new Random(), new Material[]{Material.AIR});
         entranceCore.getFront(3).Pillar(2, new Random(), new Material[]{Material.AIR});
      } else {
         (new StairwayBuilder(new Material[]{Material.COBBLESTONE_STAIRS, Material.MOSSY_COBBLESTONE_STAIRS})).setAngled(true).setStopAtWater(true).build(entranceCore.getFront(2).getDown());
      }

      int offset = size / 2;
      BlockFace offsetDir = BlockUtils.xzDiagonalPlaneBlockFaces[random.nextInt(BlockUtils.xzDiagonalPlaneBlockFaces.length)];
      SimpleBlock chimneyCore = core.getRelative(offsetDir, offset);
      chimneyCore.getDown().setType(Material.HAY_BLOCK);
      chimneyCore.setType(Material.CAMPFIRE);
      BlockFace[] var13 = BlockUtils.xzPlaneBlockFaces;
      int var14 = var13.length;

      int var15;
      BlockFace face;
      int ry;
      for(var15 = 0; var15 < var14; ++var15) {
         face = var13[var15];
         if (face.getModX() == offsetDir.getModX() || face.getModZ() == offsetDir.getModZ()) {
            for(ry = 1; ry <= 2; ++ry) {
               if (chimneyCore.getRelative(face, ry).distanceSquared(core) < (double)(size * size)) {
                  chimneyCore.getRelative(face, ry).LPillar(size, new Random(), Material.STONE, Material.COBBLESTONE);
               }
            }
         }
      }

      chimneyCore.getUp().Pillar(size + 10, Material.AIR);
      var13 = BlockUtils.directBlockFaces;
      var14 = var13.length;

      for(var15 = 0; var15 < var14; ++var15) {
         face = var13[var15];

         for(ry = size + 1; ry > 0; --ry) {
            SimpleBlock target = chimneyCore.getUp(ry).getRelative(face);
            if (target.getDown().getType() == Material.SNOW_BLOCK) {
               (new StairBuilder(Material.COBBLESTONE_STAIRS)).setFacing(face.getOppositeFace()).apply(target);
               break;
            }

            (new TrapdoorBuilder(Material.SPRUCE_TRAPDOOR)).setFacing(face).setOpen(true).apply(target);
         }
      }

      var13 = BlockUtils.directBlockFaces;
      var14 = var13.length;

      for(var15 = 0; var15 < var14; ++var15) {
         face = var13[var15];
         if (face != core.getDirection()) {
            Wall wall = new Wall(core, face.getOppositeFace());
            int threshold = size + 1;

            boolean found;
            for(found = false; threshold >= 0; --threshold) {
               if (wall.getType() == Material.SPRUCE_LOG) {
                  found = true;
                  break;
               }

               wall = wall.getRear();
            }

            if (found) {
               wall = wall.getFront(2);
               BlockFace[] var20 = BlockUtils.getAdjacentFaces(wall.getDirection());
               int var21 = var20.length;

               label119:
               for(int var22 = 0; var22 < var21; ++var22) {
                  BlockFace side = var20[var22];
                  Wall decoCore = wall.getRelative(side);
                  if (!BlockUtils.isStoneLike(decoCore.getType()) && !BlockUtils.isStoneLike(decoCore.getRelative(side).getType())) {
                     int i;
                     switch(random.nextInt(6)) {
                     case 0:
                     case 1:
                        BlockUtils.placeBed(decoCore, BlockUtils.pickBed(), decoCore.getDirection());
                        decoCore.getRelative(side).lsetType(Material.SPRUCE_LOG);
                        decoCore.getRelative(side).getUp().lsetType(Material.POTTED_SPRUCE_SAPLING);
                        break;
                     case 2:
                     case 3:
                        i = 0;

                        while(true) {
                           if (i >= 5 || decoCore.getRelative(side, i).isSolid()) {
                              continue label119;
                           }

                           switch(random.nextInt(3)) {
                           case 0:
                              (new DirectionalBuilder(new Material[]{Material.FURNACE, Material.BLAST_FURNACE, Material.SMOKER, Material.ANVIL})).setFacing(decoCore.getDirection()).apply(decoCore.getRelative(side, i));
                              break;
                           case 1:
                              decoCore.getRelative(side, i).setType(new Material[]{Material.CRAFTING_TABLE, Material.FLETCHING_TABLE});
                              break;
                           default:
                              (new SlabBuilder(new Material[]{Material.SPRUCE_SLAB, Material.DIORITE_SLAB, Material.ANDESITE_SLAB, Material.COBBLESTONE_SLAB})).setType(Type.TOP).apply(decoCore.getRelative(side, i));
                           }

                           decoCore.getRelative(side, i).getUp().setType(new Material[]{Material.TURTLE_EGG, Material.AIR, Material.AIR, Material.AIR, Material.AIR, Material.AIR, Material.AIR, Material.TORCH, Material.TORCH, Material.LANTERN, Material.LANTERN, Material.POTTED_SPRUCE_SAPLING, Material.POTTED_POPPY, Material.POTTED_FERN});
                           ++i;
                        }
                     default:
                        for(i = 0; i < 5 && !decoCore.getRelative(side, i).isSolid(); ++i) {
                           if (!random.nextBoolean()) {
                              (new BarrelBuilder()).setFacing(BlockUtils.getSixBlockFace(random)).setLootTable(TerraLootTable.IGLOO_CHEST).apply(decoCore.getRelative(side, i));
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      core.setType(Material.RED_CARPET);
      Material carpet = BlockUtils.pickCarpet();
      BlockFace[] var27 = BlockUtils.xzPlaneBlockFaces;
      var15 = var27.length;

      int var28;
      BlockFace face;
      for(var28 = 0; var28 < var15; ++var28) {
         face = var27[var28];
         core.getRelative(face).lsetType(carpet);
      }

      if (size > 5) {
         var27 = BlockUtils.directBlockFaces;
         var15 = var27.length;

         for(var28 = 0; var28 < var15; ++var28) {
            face = var27[var28];
            core.getRelative(face, 2).setType(carpet);
         }
      }

      core.getUp().addEntity(EntityType.VILLAGER);
   }

   private void spawnTrapdoorDecors(@NotNull Wall w, int size) {
      int lowest = 9999;

      int i;
      Wall target;
      for(i = 1; i < size; ++i) {
         target = w.getFront(i);
         if (i <= 2) {
            target.setType(Material.SNOW_BLOCK);
            if (i == 1) {
               target.getLeft().setType(Material.SNOW_BLOCK);
               target.getRight().setType(Material.SNOW_BLOCK);
            }

            (new TrapdoorBuilder(Material.SPRUCE_TRAPDOOR)).setFacing(w.getDirection()).apply(target.getUp());
         } else {
            target = target.getDown(i - 3);
            if (!target.isSolid()) {
               (new StairBuilder(Material.SPRUCE_STAIRS)).setFacing(target.getDirection().getOppositeFace()).apply(target);
               target.getDown().lsetType(Material.SNOW_BLOCK);
            }

            if (target.getY() < lowest) {
               lowest = target.getY();
            }
         }
      }

      for(i = w.getDown(size).getY(); i <= lowest; ++i) {
         target = w.getFront(size).getAtY(i);
         (new TrapdoorBuilder(Material.SPRUCE_TRAPDOOR)).setOpen(true).setFacing(w.getDirection()).lapply(target);
         if (target.getType() == Material.SNOW_BLOCK) {
            target.setType(Material.SPRUCE_LOG);
            target.getUp().setType(Material.SPRUCE_LOG);
            Lantern lantern = (Lantern)Bukkit.createBlockData(Material.LANTERN);
            lantern.setHanging(true);
            target.getUp().getRear().setBlockData(lantern);
            (new StairBuilder(Material.SPRUCE_STAIRS)).setFacing(target.getDirection().getOppositeFace()).apply(target.getUp(2)).apply(target.getFront());
         }
      }

   }

   private void spawnSpire(@NotNull SimpleBlock block) {
      block.Pillar(3, Material.SPRUCE_LOG);
      block.getUp(3).setType(Material.COBBLESTONE_WALL);
      block.getUp(4).setType(Material.SPRUCE_FENCE);
      BlockFace[] var2 = BlockUtils.directBlockFaces;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BlockFace face = var2[var4];
         (new StairBuilder(Material.SPRUCE_STAIRS)).setFacing(face.getOppositeFace()).lapply(block.getRelative(face));
         (new TrapdoorBuilder(Material.SPRUCE_TRAPDOOR)).setFacing(face).setOpen(true).apply(block.getUp(2).getRelative(face));
      }

   }

   public int[][] getCoordsFromMegaChunk(@NotNull TerraformWorld tw, @NotNull MegaChunk mc) {
      int num = TConfig.c.STRUCTURES_IGLOO_COUNT_PER_MEGACHUNK;
      int[][] coords = new int[num][2];

      for(int i = 0; i < num; ++i) {
         coords[i] = mc.getRandomCenterChunkBlockCoords(tw.getHashedRand((long)mc.getX(), mc.getZ(), 992722 * (1 + i)));
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
      return GenUtils.chance(tw.getHashedRand((long)chunkX, chunkZ, 976123), (int)(TConfig.c.STRUCTURES_IGLOO_SPAWNRATIO * 10000.0D), 10000);
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
               EnumSet<BiomeBank> biomes = GenUtils.getBiomesInChunk(tw, chunkX, chunkZ);
               double suitable = 0.0D;
               double notsuitable = 0.0D;
               Iterator var14 = biomes.iterator();

               while(true) {
                  while(var14.hasNext()) {
                     BiomeBank b = (BiomeBank)var14.next();
                     if (b.getClimate() == BiomeClimate.SNOWY && b.getType() == BiomeType.FLAT) {
                        ++suitable;
                     } else {
                        ++notsuitable;
                     }
                  }

                  return suitable / (suitable + notsuitable) > 0.5D && this.rollSpawnRatio(tw, chunkX, chunkZ);
               }
            }
         }

         return false;
      }
   }

   @NotNull
   public Random getHashedRandom(@NotNull TerraformWorld world, int chunkX, int chunkZ) {
      return world.getHashedRand(823641811L, chunkX, chunkZ);
   }

   public boolean isEnabled() {
      return TConfig.areStructuresEnabled() && TConfig.c.STRUCTURES_IGLOO_ENABLED;
   }

   public int getChunkBufferDistance() {
      return 1;
   }
}
