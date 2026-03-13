package org.terraform.structure.ancientcity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Slab.Type;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomLayoutGenerator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.CylinderBuilder;
import org.terraform.utils.GenUtils;
import org.terraform.utils.StairwayBuilder;
import org.terraform.utils.blockdata.SlabBuilder;
import org.terraform.utils.blockdata.StairBuilder;
import org.terraform.utils.version.V_1_19;
import org.terraform.utils.version.Version;

public class AncientCityCenterPlatformPopulator extends AncientCityAbstractRoomPopulator {
   public AncientCityCenterPlatformPopulator(TerraformWorld tw, HashSet<SimpleLocation> occupied, RoomLayoutGenerator gen, Random rand, boolean forceSpawn, boolean unique) {
      super(tw, gen, rand, forceSpawn, unique);
      this.doCarve = false;
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      Iterator var3 = this.effectiveRoom.getFourWalls(data, 0).entrySet().iterator();

      int i;
      int x;
      int z;
      while(var3.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var3.next();
         Wall w = (Wall)entry.getKey();

         for(i = 0; i < (Integer)entry.getValue(); ++i) {
            if (i > 2 && i < (Integer)entry.getValue() - 3 && i % 3 == 0) {
               if (!this.containsPaths.contains(w.get())) {
                  w.lsetType(Material.CHISELED_DEEPSLATE);
                  w.getUp().CorrectMultipleFacing(2);
                  w.getUp().LPillar(2, new Material[]{Material.DEEPSLATE_BRICK_WALL});
                  w.getUp(3).lsetType(Material.CHISELED_DEEPSLATE);
                  (new SlabBuilder(Material.DEEPSLATE_BRICK_SLAB)).setType(Type.TOP).lapply(w.getUp(3).getRear());
                  w.getRear().getUp(4).lsetType(Material.DEEPSLATE_BRICK_WALL);
                  w.getRear().getUp(4).CorrectMultipleFacing(1);
               }

               BlockFace[] var7 = BlockUtils.getAdjacentFaces(w.getDirection());
               x = var7.length;

               for(z = 0; z < x; ++z) {
                  BlockFace dir = var7[z];
                  Wall rel = w.getRelative(dir);
                  if (!this.containsPaths.contains(rel.get())) {
                     rel.lsetType(Material.DEEPSLATE_BRICKS);
                     (new StairBuilder(Material.DEEPSLATE_BRICK_STAIRS)).setFacing(rel.getDirection().getOppositeFace()).lapply(rel.getFront()).setFacing(dir).lapply(rel.getUp()).setFacing(dir.getOppositeFace()).lapply(rel.getUp(2)).lapply(rel.getUp(4).getRear());
                     rel.getUp(3).lsetType(Material.DEEPSLATE_BRICKS);
                     (new StairBuilder(Material.DEEPSLATE_BRICK_STAIRS)).setHalf(Half.TOP).setFacing(rel.getDirection()).lapply(rel.getUp(3).getRear()).setFacing(rel.getDirection().getOppositeFace()).lapply(rel.getUp(3).getFront());
                  }
               }
            }

            w = w.getLeft();
         }
      }

      int y = this.effectiveRoom.getY();
      int[][] var21 = this.effectiveRoom.getAllCorners(2);
      int var23 = var21.length;

      for(i = 0; i < var23; ++i) {
         int[] corner = var21[i];
         x = corner[0];
         z = corner[1];
         this.spawnLargePillar(new SimpleBlock(data, x, y, z), room);
      }

      BlockFace facing = BlockUtils.getDirectBlockFace(this.getRand());
      byte modX;
      byte modZ;
      CubeRoom fireBox;
      if (BlockUtils.getAxisFromBlockFace(facing) == Axis.X) {
         modX = 3;
         modZ = 17;
         fireBox = this.effectiveRoom.getCloneSubsetRoom(8, 2);
      } else {
         modZ = 3;
         modX = 17;
         fireBox = this.effectiveRoom.getCloneSubsetRoom(2, 8);
      }

      int[] lowerCorner = fireBox.getLowerCorner(6);
      int[] upperCorner = fireBox.getUpperCorner(6);

      int nz;
      for(int nx = lowerCorner[0]; nx <= upperCorner[0]; ++nx) {
         for(nz = lowerCorner[1]; nz <= upperCorner[1]; ++nz) {
            data.setType(nx, fireBox.getY(), nz, Material.SOUL_SAND);
            data.setType(nx, fireBox.getY() + 1, nz, Material.SOUL_FIRE);
         }
      }

      Iterator var31 = fireBox.getFourWalls(data, 5).entrySet().iterator();

      while(var31.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var31.next();
         Wall w = (Wall)entry.getKey();

         for(int i = 0; i < (Integer)entry.getValue(); ++i) {
            w.Pillar(GenUtils.randInt(this.rand, 1, 3), AncientCityUtils.deepslateBricks);
            if (BlockUtils.getAxisFromBlockFace(w.getDirection()) == BlockUtils.getAxisFromBlockFace(facing)) {
               w.getRear().setType(AncientCityUtils.deepslateBricks);
               w.getRear(2).setType(AncientCityUtils.deepslateBricks);
               if (GenUtils.chance(this.rand, 1, 30)) {
                  BlockUtils.placeCandle(w.getRear().getUp(), this.rand.nextInt(3) + 1, true);
               }

               if (GenUtils.chance(this.rand, 1, 30)) {
                  BlockUtils.placeCandle(w.getRear(2).getUp(), this.rand.nextInt(3) + 1, true);
               }

               if (this.rand.nextBoolean()) {
                  w.getRear(3).setType(AncientCityUtils.deepslateBricks);
                  if (GenUtils.chance(this.rand, 1, 30)) {
                     BlockUtils.placeCandle(w.getRear(3).getUp(), this.rand.nextInt(3) + 1, true);
                  }

                  if (this.rand.nextBoolean()) {
                     (new StairBuilder(new Material[]{Material.COBBLED_DEEPSLATE_STAIRS, Material.POLISHED_DEEPSLATE_STAIRS})).setFacing(w.getDirection()).apply(w.getRear(4));
                  }
               } else if (this.rand.nextBoolean()) {
                  (new StairBuilder(new Material[]{Material.COBBLED_DEEPSLATE_STAIRS, Material.POLISHED_DEEPSLATE_STAIRS})).setFacing(w.getDirection()).apply(w.getRear(3));
               }
            }

            w = w.getLeft();
         }
      }

      (new CylinderBuilder(new Random(), room.getCenterSimpleBlock(data).getUp(13), AncientCityUtils.deepslateBricks)).setRX((float)modX).setRZ((float)modZ).setRY(20.0F).build();
      this.spawnCentralHead(room.getCenterSimpleBlock(data).getUp(13), facing);
      BlockFace[] var33 = new BlockFace[]{facing, facing.getOppositeFace()};
      nz = var33.length;

      for(int var36 = 0; var36 < nz; ++var36) {
         BlockFace dir = var33[var36];
         Wall targetStair = new Wall(room.getCenterSimpleBlock(data).getAtY(this.effectiveRoom.getY() + 1).getRelative(dir, 5), dir.getOppositeFace());

         for(int radius = 0; radius <= 14; ++radius) {
            BlockFace[] var16 = BlockUtils.getAdjacentFaces(facing);
            int var17 = var16.length;

            for(int var18 = 0; var18 < var17; ++var18) {
               BlockFace rel = var16[var18];
               (new StairwayBuilder(new Material[]{Material.COBBLED_DEEPSLATE_STAIRS, Material.POLISHED_DEEPSLATE_STAIRS})).setAngled(false).setCarveAirSpace(false).setUpwardsCarveUntilSolid(true).setUpwardsCarveUntilNotSolid(false).setStopAtY(targetStair.getY() + 4).setDownTypes(AncientCityUtils.deepslateBricks).setStairwayDirection(BlockFace.UP).build(targetStair.getRelative(rel, radius));
            }
         }
      }

      CubeRoom basement = new CubeRoom(this.effectiveRoom.getWidthX(), this.effectiveRoom.getWidthZ(), 6, this.effectiveRoom.getX(), this.effectiveRoom.getY() - 6, this.effectiveRoom.getZ());
      AncientCityResearchBasementHandler.populate(this.rand, data, basement, facing);
      super.sculkUp(this.tw, data, this.effectiveRoom);
   }

   private void spawnCentralHead(@NotNull SimpleBlock core, @NotNull BlockFace facing) {
      int headHeight = 11;
      int headWidth = 15;
      int generalFuzzSize = 3;

      for(int radius = 0; radius <= headWidth; ++radius) {
         BlockFace[] var7 = BlockUtils.getAdjacentFaces(facing);
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            BlockFace rel = var7[var9];
            if (Version.VERSION.isAtLeast(Version.v1_19_4)) {
               assert V_1_19.REINFORCED_DEEPSLATE != null;

               core.getRelative(rel, radius).setType(V_1_19.REINFORCED_DEEPSLATE);
               core.getUp(headHeight).getRelative(rel, radius).setType(V_1_19.REINFORCED_DEEPSLATE);
            } else if (radius % 2 == 0) {
               core.getRelative(rel, radius).setType(AncientCityUtils.deepslateTiles);
               core.getUp(headHeight).getRelative(rel, radius).setType(AncientCityUtils.deepslateTiles);
            } else {
               core.getRelative(rel, radius).setType(Material.POLISHED_DIORITE, Material.DIORITE);
               core.getUp(headHeight).getRelative(rel, radius).setType(Material.POLISHED_DIORITE, Material.DIORITE);
            }

            if (radius != headWidth) {
               core.getRelative(rel, radius).getUp().Pillar(headHeight - 1, Material.AIR);
            }

            this.airWardenBlocks(core.getRelative(rel, radius), facing);
            this.airWardenBlocks(core.getUp(headHeight).getRelative(rel, radius), facing);
            this.airWardenBlocks(core.getUp(1).getRelative(rel, radius), headHeight - 1, facing);
            core.getRelative(rel, radius).getDown().downPillar(GenUtils.randInt(this.rand, generalFuzzSize, generalFuzzSize + 1), AncientCityUtils.deepslateTiles);
            this.airWardenBlocks(core.getRelative(rel, radius).getDown(), facing);
            core.getRelative(rel, radius).getUp(headHeight + 1).Pillar(GenUtils.randInt(this.rand, generalFuzzSize, generalFuzzSize + 1), AncientCityUtils.deepslateTiles);
            if (radius == headWidth) {
               if (Version.VERSION.isAtLeast(Version.v1_19_4)) {
                  core.getRelative(rel, radius).Pillar(headHeight, V_1_19.REINFORCED_DEEPSLATE);
               } else {
                  core.getRelative(rel, radius).Pillar(headHeight, true, new Random(), Material.DEEPSLATE_TILES, Material.DIORITE);
               }

               for(int i = -2; i <= headHeight + 2; ++i) {
                  SimpleBlock start = core.getRelative(rel, radius + 1).getUp(i);
                  int maxFuzzSize = GenUtils.randInt(this.rand, generalFuzzSize, generalFuzzSize + 1);
                  if (i >= headHeight / 2 - 1 && i <= headHeight / 2 + 2) {
                     maxFuzzSize += 2;
                  }

                  for(int fuzzSize = 0; fuzzSize < maxFuzzSize; ++fuzzSize) {
                     start.getRelative(rel, fuzzSize).setType(AncientCityUtils.deepslateTiles);
                  }
               }
            }
         }
      }

   }

   private void airWardenBlocks(@NotNull SimpleBlock b, @NotNull BlockFace dir) {
      this.airWardenBlocks(b, 1, dir);
   }

   private void airWardenBlocks(@NotNull SimpleBlock b, int height, @NotNull BlockFace dir) {
      for(int i = 0; i < height; ++i) {
         for(int depth = 1; depth <= 3; ++depth) {
            b.getUp(i).getRelative(dir, depth).setType(Material.AIR);
            b.getUp(i).getRelative(dir, -depth).setType(Material.AIR);
         }
      }

   }

   private void spawnLargePillar(@NotNull SimpleBlock core, @NotNull CubeRoom room) {
      int bfIndex;
      int i;
      SimpleBlock target;
      for(bfIndex = -2; bfIndex <= 2; ++bfIndex) {
         for(i = -2; i <= 2; ++i) {
            target = core.getRelative(bfIndex, 1, i);
            if (Math.abs(bfIndex) != 2 && Math.abs(i) != 2 && (bfIndex != 0 || i != 0)) {
               target.Pillar(10, Material.AIR);
            } else {
               target.RPillar(10, new Random(), Material.DEEPSLATE_BRICKS, Material.CRACKED_DEEPSLATE_BRICKS, Material.DEEPSLATE_BRICKS, Material.CRACKED_DEEPSLATE_BRICKS, Material.DEEPSLATE_BRICKS, Material.CRACKED_DEEPSLATE_BRICKS, Material.DEEPSLATE_BRICK_SLAB);
            }
         }
      }

      bfIndex = 0;
      if (core.getX() < room.getX() && core.getZ() < room.getZ()) {
         bfIndex = 3;
      } else if (core.getX() > room.getX() && core.getZ() < room.getZ()) {
         bfIndex = 5;
      } else if (core.getX() < room.getX() && core.getZ() > room.getZ()) {
         bfIndex = 1;
      } else if (core.getX() > room.getX() && core.getZ() > room.getZ()) {
         bfIndex = 7;
      }

      int relX;
      for(i = 1; i <= 10; ++i) {
         if (i == 1) {
            target = core.getRelative(BlockUtils.xzPlaneBlockFaces[bfIndex], 2);

            for(relX = -1; relX <= 1; ++relX) {
               for(int relZ = -1; relZ <= 1; ++relZ) {
                  target.getRelative(relX, 1, relZ).Pillar(3, Material.AIR);
               }
            }
         }

         BlockFace face = BlockUtils.xzPlaneBlockFaces[bfIndex];
         Slab bottom = (Slab)Bukkit.createBlockData(Material.DEEPSLATE_BRICK_SLAB);
         bottom.setType(Type.BOTTOM);
         core.getRelative(face.getModX(), i, face.getModZ()).setBlockData(bottom);
         bfIndex = getNextIndex(bfIndex);
         face = BlockUtils.xzPlaneBlockFaces[bfIndex];
         Slab top = (Slab)Bukkit.createBlockData(Material.DEEPSLATE_BRICK_SLAB);
         top.setType(Type.TOP);
         core.getRelative(face.getModX(), i, face.getModZ()).setBlockData(top);
         bfIndex = getNextIndex(bfIndex);
      }

      BlockFace[] var9 = BlockUtils.directBlockFaces;
      int var11 = var9.length;

      for(relX = 0; relX < var11; ++relX) {
         BlockFace face = var9[relX];
         Wall target = (new Wall(core.getUp(10), face)).getFront(2);
         target.getFront().getUp().setType(Material.DEEPSLATE_BRICK_WALL);
         target.getFront().getDown().getRight().setType(Material.DEEPSLATE_BRICK_WALL);
         target.getFront().getDown().getLeft().setType(Material.DEEPSLATE_BRICK_WALL);
         target.getFront().getRight().setType(Material.DEEPSLATE_BRICK_SLAB);
         target.getFront().getLeft().setType(Material.DEEPSLATE_BRICK_SLAB);
         (new StairBuilder(Material.DEEPSLATE_BRICK_STAIRS)).setFacing(face).apply(target.getUp().getRight()).apply(target.getUp().getLeft());
         (new StairBuilder(Material.DEEPSLATE_BRICK_STAIRS)).setFacing(face).setHalf(Half.TOP).apply(target.getFront()).apply(target.getFront().getRight().getDown(2)).apply(target.getFront().getLeft().getDown(2));
         target.getUp().getRight(2).setType(Material.DEEPSLATE_BRICK_WALL);
         target.getUp().getLeft(2).setType(Material.DEEPSLATE_BRICK_WALL);
      }

   }

   public boolean canPopulate(CubeRoom room) {
      return true;
   }
}
