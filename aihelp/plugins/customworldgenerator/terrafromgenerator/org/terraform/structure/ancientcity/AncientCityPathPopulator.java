package org.terraform.structure.ancientcity;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Lantern;
import org.bukkit.block.data.type.Slab.Type;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.structure.room.PathPopulatorAbstract;
import org.terraform.structure.room.PathPopulatorData;
import org.terraform.structure.room.RoomLayoutGenerator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.SlabBuilder;
import org.terraform.utils.blockdata.StairBuilder;

public class AncientCityPathPopulator extends PathPopulatorAbstract {
   private final RoomLayoutGenerator gen;
   private final HashSet<SimpleLocation> occupied;
   private final Random rand;

   public AncientCityPathPopulator(Random rand, RoomLayoutGenerator gen, HashSet<SimpleLocation> occupied) {
      this.rand = rand;
      this.gen = gen;
      this.occupied = occupied;
   }

   public void populate(@NotNull PathPopulatorData ppd) {
      Wall core = new Wall(ppd.base, ppd.dir);
      if (!this.gen.isInRoom(new int[]{ppd.base.getX(), ppd.base.getZ()})) {
         int nz;
         int var5;
         if (!ppd.isTurn && ppd.dir != BlockFace.UP) {
            if (!ppd.isEnd) {
               Wall[] var12 = new Wall[]{core.getRear(), core, core.getFront()};
               nz = var12.length;

               for(var5 = 0; var5 < nz; ++var5) {
                  Wall floor = var12[var5];
                  floor.setType(Material.GRAY_WOOL);
                  floor.getLeft().setType(Material.GRAY_WOOL);
                  floor.getRight().setType(Material.GRAY_WOOL);
                  floor.getUp().Pillar(3, new Material[]{Material.AIR});
                  (new StairBuilder(Material.DEEPSLATE_BRICK_STAIRS)).setHalf(Half.TOP).setFacing(BlockUtils.getLeft(ppd.dir)).lapply(floor.getRight(2)).setFacing(BlockUtils.getRight(ppd.dir)).lapply(floor.getLeft(2));
                  int var10000;
                  switch(ppd.dir) {
                  case NORTH:
                     var10000 = -1 * floor.getZ() % 9;
                     break;
                  case SOUTH:
                     var10000 = floor.getZ() % 9;
                     break;
                  case EAST:
                     var10000 = floor.getX() % 9;
                     break;
                  default:
                     var10000 = -1 * floor.getX() % 9;
                  }

                  int state = var10000;
                  if (state < 0) {
                     state += 9;
                  }

                  if (!ppd.isOverlapped) {
                     this.placeWallArc(floor, state);
                  }
               }
            } else {
               AncientCityPathMiniRoomPlacer.placeAltar(core);
            }
         } else {
            for(int nx = -1; nx <= 1; ++nx) {
               for(nz = -1; nz <= 1; ++nz) {
                  core.getRelative(nx, 0, nz).setType(Material.GRAY_WOOL);
               }
            }

            BlockFace[] var11 = BlockUtils.xzDiagonalPlaneBlockFaces;
            nz = var11.length;

            BlockFace face;
            for(var5 = 0; var5 < nz; ++var5) {
               face = var11[var5];
               core.getRelative(face, 2).Pillar(GenUtils.randInt(this.rand, 4, 7), AncientCityUtils.deepslateBricks);
            }

            AncientCityUtils.placeSupportPillar(core.getDown());
            var11 = BlockUtils.directBlockFaces;
            nz = var11.length;

            for(var5 = 0; var5 < nz; ++var5) {
               face = var11[var5];
               core.getRelative(face, 2).lsetType(AncientCityUtils.deepslateBricks);
               BlockFace[] var7 = BlockUtils.getAdjacentFaces(face);
               int var8 = var7.length;

               for(int var9 = 0; var9 < var8; ++var9) {
                  BlockFace rel = var7[var9];
                  core.getRelative(rel).getRelative(face, 2).lsetType(AncientCityUtils.deepslateBricks);
               }
            }
         }

      }
   }

   private void placeWallArc(@NotNull Wall core, int state) {
      if (!this.occupied.contains(core.getLoc()) && !this.occupied.contains(core.getRight().getLoc()) && !this.occupied.contains(core.getLeft().getLoc())) {
         this.occupied.add(core.getLoc());
         this.occupied.add(core.getRight().getLoc());
         this.occupied.add(core.getLeft().getLoc());
         BlockFace pathFacing = core.getDirection();
         if (state > 4) {
            pathFacing = core.getDirection().getOppositeFace();
         }

         BlockFace[] var5;
         int var6;
         int var7;
         BlockFace leftRight;
         switch(state) {
         case 0:
            core.getUp(6).lsetType(Material.DEEPSLATE_BRICK_WALL);
            core.getUp(5).lsetType(Material.DEEPSLATE_BRICKS);
            Lantern lantern = (Lantern)Bukkit.createBlockData(Material.SOUL_LANTERN);
            lantern.setHanging(true);
            core.getUp(4).lsetBlockData(lantern);
            AncientCityUtils.placeSupportPillar(core.getDown());
            var5 = BlockUtils.getAdjacentFaces(core.getDirection());
            var6 = var5.length;

            for(var7 = 0; var7 < var6; ++var7) {
               leftRight = var5[var7];
               (new StairBuilder(Material.POLISHED_DEEPSLATE_STAIRS)).setHalf(Half.TOP).setFacing(leftRight.getOppositeFace()).apply(core.getRelative(leftRight, 2).getDown());
               core.getRelative(leftRight, 2).setType(Material.CHISELED_DEEPSLATE);
               core.getRelative(leftRight, 2).getUp().lsetType(Material.DEEPSLATE_TILES);
               core.getRelative(leftRight, 2).getUp(2).LPillar(2, new Material[]{Material.POLISHED_DEEPSLATE});
               core.getRelative(leftRight, 2).getUp(4).lsetType(Material.CHISELED_DEEPSLATE);
               (new StairBuilder(Material.POLISHED_DEEPSLATE_STAIRS)).setFacing(leftRight.getOppositeFace()).lapply(core.getUp(5).getRelative(leftRight, 2));
               (new StairBuilder(Material.POLISHED_DEEPSLATE_STAIRS)).setFacing(leftRight).setHalf(Half.TOP).lapply(core.getUp(4).getRelative(leftRight));
               core.getRelative(leftRight).getUp(5).lsetType(Material.DEEPSLATE_BRICKS);
            }

            return;
         case 1:
         case 8:
            (new StairBuilder(Material.DEEPSLATE_TILE_STAIRS)).setHalf(Half.TOP).setFacing(pathFacing.getOppositeFace()).lapply(core.getUp(5));
            var5 = BlockUtils.getAdjacentFaces(core.getDirection());
            var6 = var5.length;

            for(var7 = 0; var7 < var6; ++var7) {
               leftRight = var5[var7];
               (new SlabBuilder(Material.DEEPSLATE_BRICK_SLAB)).setType(Type.TOP).lapply(core.getRelative(leftRight, 2).getDown());
               (new StairBuilder(Material.POLISHED_DEEPSLATE_STAIRS)).setFacing(pathFacing.getOppositeFace()).lapply(core.getRelative(leftRight, 2).getUp());
               (new StairBuilder(Material.DEEPSLATE_TILE_STAIRS)).setHalf(Half.TOP).setFacing(pathFacing.getOppositeFace()).lapply(core.getRelative(leftRight, 2).getUp(4));
               core.getRelative(leftRight, 2).getUp(5).lsetType(Material.DEEPSLATE_TILES);
               core.getRelative(leftRight).getUp(5).lsetType(Material.DEEPSLATE_BRICK_WALL);
               core.getRelative(leftRight).getUp(6).lsetType(Material.DEEPSLATE_BRICK_SLAB);
            }

            return;
         case 2:
         case 7:
            var5 = BlockUtils.getAdjacentFaces(core.getDirection());
            var6 = var5.length;

            for(var7 = 0; var7 < var6; ++var7) {
               leftRight = var5[var7];
               (new SlabBuilder(Material.DEEPSLATE_BRICK_SLAB)).setType(Type.TOP).lapply(core.getRelative(leftRight, 2).getDown()).lapply(core.getRelative(leftRight).getUp(5));
               core.getRelative(leftRight, 2).getUp(5).lsetType(Material.DEEPSLATE_TILES);
               core.getRelative(leftRight, 2).getUp(6).lsetType(Material.DEEPSLATE_BRICK_SLAB);
            }

            return;
         case 3:
         case 6:
            (new SlabBuilder(Material.DEEPSLATE_BRICK_SLAB)).setType(Type.TOP).lapply(core.getUp(5));
            var5 = BlockUtils.getAdjacentFaces(core.getDirection());
            var6 = var5.length;

            for(var7 = 0; var7 < var6; ++var7) {
               leftRight = var5[var7];
               (new SlabBuilder(Material.DEEPSLATE_BRICK_SLAB)).setType(Type.TOP).lapply(core.getRelative(leftRight, 2).getUp(5));
               (new StairBuilder(Material.DEEPSLATE_BRICK_STAIRS)).setFacing(pathFacing.getOppositeFace()).lapply(core.getRelative(leftRight, 2).getUp(6));
            }

            return;
         case 4:
         case 5:
            (new SlabBuilder(Material.DEEPSLATE_BRICK_SLAB)).setType(Type.TOP).lapply(core.getUp(5));
            var5 = BlockUtils.getAdjacentFaces(core.getDirection());
            var6 = var5.length;

            for(var7 = 0; var7 < var6; ++var7) {
               leftRight = var5[var7];
               (new StairBuilder(Material.DEEPSLATE_BRICK_STAIRS)).setFacing(leftRight).setHalf(Half.TOP).lapply(core.getRelative(leftRight, 2).getUp(5));
               (new StairBuilder(Material.DEEPSLATE_BRICK_STAIRS)).setFacing(pathFacing.getOppositeFace()).lapply(core.getRelative(leftRight, 2).getUp(6));
            }

            return;
         default:
            TerraformGeneratorPlugin.logger.info("Ancient City Populator: Irregular path state: " + state);
         }
      }
   }

   public boolean customCarve(@NotNull SimpleBlock base, BlockFace dir, int pathWidth) {
      Wall core = new Wall(base.getUp(), dir);
      int seed = 55 + core.getX() + core.getY() ^ 2 + core.getZ() ^ 3;
      EnumSet<Material> carveMaterials = BlockUtils.stoneLike.clone();
      carveMaterials.addAll(BlockUtils.caveDecoratorMaterials);
      BlockUtils.carveCaveAir(seed, (float)(pathWidth + 3), (float)(pathWidth + 3), (float)(pathWidth + 3), 0.09F, core.get(), true, true, carveMaterials);
      return true;
   }
}
