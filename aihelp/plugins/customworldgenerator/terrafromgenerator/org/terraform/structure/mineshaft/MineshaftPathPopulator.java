package org.terraform.structure.mineshaft;

import java.util.Random;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.Rail.Shape;
import org.bukkit.block.data.type.Lantern;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Slab.Type;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.IPopulatorDataMinecartSpawner;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.main.TLogger;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.structure.room.PathPopulatorAbstract;
import org.terraform.structure.room.PathPopulatorData;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class MineshaftPathPopulator extends PathPopulatorAbstract {
   private final Random rand;

   public MineshaftPathPopulator(Random rand) {
      this.rand = rand;
   }

   /** @deprecated */
   @Deprecated
   public boolean customCarve(@NotNull SimpleBlock base, BlockFace dir, int pathWidth) {
      Wall core = new Wall(base.getUp(), dir);
      int seed = 55 + core.getX() + core.getY() ^ 2 + core.getZ() ^ 3;
      BlockUtils.carveCaveAir(seed, (float)pathWidth, (float)(pathWidth + 1), (float)pathWidth, core.get(), false, BlockUtils.caveCarveReplace);
      return true;
   }

   public void populate(@NotNull PathPopulatorData ppd) {
      Wall core = new Wall(ppd.base, ppd.dir);
      if (ppd.dir != BlockFace.UP) {
         this.legacyPopulate(core.getRear());
         this.legacyPopulate(core);
         this.legacyPopulate(core.getFront());
      } else {
         for(int nx = -1; nx <= 1; ++nx) {
            for(int nz = -1; nz <= 1; ++nz) {
               core.getRelative(nx, 0, nz).setType(this.getPathMaterial());
            }
         }

      }
   }

   public void legacyPopulate(@NotNull Wall core) {
      if (core.getType() == Material.CAVE_AIR) {
         Wall ceiling = core.findCeiling(10);
         if (ceiling != null) {
            ceiling = ceiling.getDown();
         }

         Wall left = core.findLeft(10);
         Wall right = core.findRight(10);
         core.setType((Material)GenUtils.randChoice((Object[])(Material.COBBLESTONE, Material.COBBLESTONE, Material.COBBLESTONE, Material.ANDESITE, Material.DIORITE, Material.MOSSY_COBBLESTONE)));
         core.getRight().setType((Material)GenUtils.randChoice((Object[])this.getPathMaterial()));
         core.getLeft().setType((Material)GenUtils.randChoice((Object[])this.getPathMaterial()));
         if (GenUtils.chance(this.rand, 1, 5)) {
            core.getDown().getRight().downUntilSolid(this.rand, new Material[]{this.getFenceMaterial()});
            core.getDown().getLeft().downUntilSolid(this.rand, new Material[]{this.getFenceMaterial()});
         }

         if (this.rand.nextBoolean()) {
            core.getRight(2).setType((Material)GenUtils.randChoice((Object[])this.getPathMaterial()));
         }

         if (this.rand.nextBoolean()) {
            core.getLeft(2).setType((Material)GenUtils.randChoice((Object[])this.getPathMaterial()));
         }

         for(int i = -2; i <= 2; ++i) {
            if (i != 0) {
               Wall target = core.getLeft(i);
               if (!Tag.SLABS.isTagged(target.getType()) && target.isSolid() && target.getType() != Material.GRAVEL && target.getUp().getType() == Material.CAVE_AIR) {
                  if (GenUtils.chance(1, 10)) {
                     Directional pebble = (Directional)Material.STONE_BUTTON.createBlockData("[face=floor]");
                     pebble.setFacing(BlockUtils.getDirectBlockFace(this.rand));
                     target.getUp().setBlockData(pebble);
                  } else if (GenUtils.chance(1, 10)) {
                     PlantBuilder.build(target.getUp(), PlantBuilder.BROWN_MUSHROOM, PlantBuilder.RED_MUSHROOM);
                  }
               }
            }
         }

         if (TConfig.areDecorationsEnabled() && core.isSolid() && this.rand.nextBoolean()) {
            Rail rail = (Rail)Bukkit.createBlockData(Material.RAIL);
            switch(core.getDirection()) {
            case NORTH:
            case SOUTH:
               rail.setShape(Shape.NORTH_SOUTH);
               break;
            case EAST:
            case WEST:
               rail.setShape(Shape.EAST_WEST);
            }

            if (BlockUtils.isWet(core.getUp().get())) {
               rail.setWaterlogged(true);
            }

            core.getUp().setBlockData(rail);
            if (GenUtils.chance(this.rand, 1, 100)) {
               TLogger var10000 = TerraformGeneratorPlugin.logger;
               int var10001 = core.getX();
               var10000.info("Minecart with chest at: " + var10001 + ", " + core.getY() + ", " + core.getZ());
               IPopulatorDataMinecartSpawner ms = (IPopulatorDataMinecartSpawner)core.get().getPopData();
               ms.spawnMinecartWithChest(core.getX(), core.getY() + 1, core.getZ(), TerraLootTable.ABANDONED_MINESHAFT, this.rand);
            }
         }

         boolean hasSupports = this.setMineshaftSupport(left, right, ceiling);
         if (!hasSupports) {
            for(int i = -2; i <= 2; ++i) {
               Wall ceil = core.getLeft(i).findCeiling(10);
               Wall floor = core.getLeft(i).findFloor(10);
               int var12;
               if (ceil != null && floor != null) {
                  for(int ny = 0; ny <= ceil.getY() - floor.getY(); ++ny) {
                     Wall[] walls = new Wall[]{floor.getRelative(0, ny, 0).findLeft(10), floor.getRelative(0, ny, 0).findRight(10)};
                     Wall[] var11 = walls;
                     var12 = walls.length;

                     for(int var13 = 0; var13 < var12; ++var13) {
                        Wall target = var11[var13];
                        if (target != null && target.getType() == Material.STONE) {
                           if (GenUtils.chance(1, 10)) {
                              target.setType((Material)GenUtils.randChoice((Object[])(Material.COBBLESTONE, Material.MOSSY_COBBLESTONE)));
                           }

                           if (GenUtils.chance(1, 10)) {
                              BlockUtils.vineUp(target.get(), 2);
                           }
                        }
                     }
                  }
               }

               boolean canSpawn;
               BlockFace[] var21;
               int var24;
               BlockFace face;
               if (ceil != null && !Tag.SLABS.isTagged(ceil.getType()) && !Tag.LOGS.isTagged(ceil.getType())) {
                  ceil = ceil.getDown();
                  if (!GenUtils.chance(this.rand, 1, 10)) {
                     if (GenUtils.chance(this.rand, 1, 6)) {
                        if (TConfig.areDecorationsEnabled()) {
                           ceil.setType(Material.COBWEB);
                        }
                     } else if (GenUtils.chance(this.rand, 1, 10)) {
                        Slab slab = (Slab)Bukkit.createBlockData((Material)GenUtils.randChoice((Object[])(Material.COBBLESTONE_SLAB, Material.STONE_SLAB, Material.MOSSY_COBBLESTONE_SLAB)));
                        slab.setType(Type.TOP);
                        ceil.setBlockData(slab);
                     }
                  } else {
                     canSpawn = true;
                     var21 = BlockUtils.directBlockFaces;
                     var24 = var21.length;

                     for(var12 = 0; var12 < var24; ++var12) {
                        face = var21[var12];
                        if (Tag.WALLS.isTagged(ceil.getRelative(face).getType())) {
                           canSpawn = false;
                           break;
                        }
                     }

                     if (canSpawn) {
                        ceil.downLPillar(this.rand, GenUtils.randInt(this.rand, 1, 3), new Material[]{Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL});
                     }
                  }
               }

               if (floor != null && !Tag.SLABS.isTagged(floor.getType()) && !Tag.LOGS.isTagged(floor.getType())) {
                  floor = floor.getUp();
                  if (!GenUtils.chance(this.rand, 1, 10)) {
                     if (GenUtils.chance(this.rand, 1, 10)) {
                        BlockFace[] var25 = BlockUtils.directBlockFaces;
                        int var23 = var25.length;

                        for(var24 = 0; var24 < var23; ++var24) {
                           BlockFace face = var25[var24];
                           if (floor.getRelative(face).isSolid()) {
                              Slab slab = (Slab)Bukkit.createBlockData((Material)GenUtils.randChoice((Object[])(Material.COBBLESTONE_SLAB, Material.STONE_SLAB, Material.MOSSY_COBBLESTONE_SLAB)));
                              slab.setType(Type.BOTTOM);
                              floor.setBlockData(slab);
                              break;
                           }
                        }
                     } else if (GenUtils.chance(1, 15)) {
                        PlantBuilder.build(floor, PlantBuilder.BROWN_MUSHROOM, PlantBuilder.RED_MUSHROOM);
                     }
                  } else {
                     canSpawn = true;
                     var21 = BlockUtils.directBlockFaces;
                     var24 = var21.length;

                     for(var12 = 0; var12 < var24; ++var12) {
                        face = var21[var12];
                        if (Tag.WALLS.isTagged(floor.getRelative(face).getType())) {
                           canSpawn = false;
                           break;
                        }
                     }

                     if (canSpawn) {
                        floor.LPillar(GenUtils.randInt(this.rand, 1, 3), false, this.rand, new Material[]{Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL});
                     }
                  }
               }
            }

         }
      }
   }

   public boolean setMineshaftSupport(@Nullable Wall left, @Nullable Wall right, @Nullable Wall ceil) {
      if (!TConfig.areDecorationsEnabled()) {
         return true;
      } else if (left != null && right != null) {
         if (left.getDirection().getModX() != 0) {
            if (left.getX() % 5 != 0) {
               return false;
            }
         } else if (left.getDirection().getModZ() != 0 && left.getZ() % 5 != 0) {
            return false;
         }

         left = left.getRight();
         right = right.getLeft();
         int dist = (int)left.get().toVector().distance(right.get().toVector());
         if (dist >= 3) {
            if (left.LPillar(10, false, this.rand, new Material[]{Material.BARRIER}) != 10) {
               left.LPillar(10, false, this.rand, new Material[]{this.getFenceMaterial()});
               this.placeSupportFences(left.getDown());
            }

            if (right.LPillar(10, false, this.rand, new Material[]{Material.BARRIER}) != 10) {
               right.LPillar(10, false, this.rand, new Material[]{this.getFenceMaterial()});
               this.placeSupportFences(right.getDown());
            }

            if (ceil != null) {
               Orientable log = (Orientable)Bukkit.createBlockData(this.getSupportMaterial());
               if (left.getDirection().getModX() != 0) {
                  log.setAxis(Axis.Z);
               }

               if (left.getDirection().getModZ() != 0) {
                  log.setAxis(Axis.X);
               }

               ceil = left.clone().getRelative(0, ceil.getY() - left.getY(), 0).getLeft();
               Lantern lantern = (Lantern)Bukkit.createBlockData(Material.LANTERN);
               lantern.setHanging(true);

               for(int i = 0; i < dist + 2; ++i) {
                  Wall support = ceil.getRight(i);
                  if ((!support.isSolid() || support.getType() == this.getFenceMaterial()) && support.getUp().getType() != this.getSupportMaterial() && support.getDown().getType() != this.getSupportMaterial()) {
                     support.setBlockData(log);
                     if (GenUtils.chance(this.rand, 1, 100)) {
                        support.getDown().get().lsetBlockData(lantern);
                     }

                     if (GenUtils.chance(this.rand, 1, 10)) {
                        BlockUtils.vineUp(support.get(), 3);
                     }
                  }
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private void placeSupportFences(@NotNull Wall w) {
      for(; !w.isSolid(); w = w.getDown()) {
         if (w.getType() == Material.LAVA) {
            w.setType(Material.COBBLESTONE);
         } else {
            w.setType(this.getFenceMaterial());
         }
      }

   }

   @NotNull
   public Material[] getPathMaterial() {
      return new Material[]{Material.OAK_PLANKS, Material.OAK_SLAB, Material.OAK_PLANKS, Material.OAK_SLAB, Material.GRAVEL};
   }

   @NotNull
   public Material getFenceMaterial() {
      return Material.OAK_FENCE;
   }

   @NotNull
   public Material getSupportMaterial() {
      return Material.OAK_LOG;
   }
}
