package org.terraform.structure.stronghold;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Slab.Type;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.ChestBuilder;
import org.terraform.utils.blockdata.SlabBuilder;
import org.terraform.utils.blockdata.StairBuilder;

public class PrisonRoomPopulator extends RoomPopulatorAbstract {
   public PrisonRoomPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   private static void dangleIronBarsDown(Random rand, int length, @NotNull SimpleBlock base) {
      for(int i = 0; i < length; ++i) {
         base.setType(Material.CHAIN);
         base = base.getDown();
      }

   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int[] lowerCorner = room.getLowerCorner(1);
      int[] upperCorner = room.getUpperCorner(1);

      for(int i = 0; i < GenUtils.randInt(12, 25); ++i) {
         dangleIronBarsDown(this.rand, GenUtils.randInt(room.getHeight() / 4 - 1, room.getHeight() / 2), new SimpleBlock(data, GenUtils.randInt(lowerCorner[0], upperCorner[0]), room.getY() + room.getHeight() - 1, GenUtils.randInt(lowerCorner[1], upperCorner[1])));
      }

      Material[] slabs = new Material[]{Material.STONE_BRICK_SLAB, Material.MOSSY_STONE_BRICK_SLAB, Material.STONE_SLAB, Material.SMOOTH_STONE_SLAB};

      int x;
      int z;
      for(x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
         for(z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
            data.setBlockData(x, room.getY() + room.getHeight() / 2, z, (new SlabBuilder(slabs)).setType(Type.TOP).get());
         }
      }

      lowerCorner = room.getLowerCorner(7);
      upperCorner = room.getUpperCorner(7);

      for(x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
         for(z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
            data.setType(x, room.getY() + room.getHeight() / 2, z, Material.AIR);
         }
      }

      int[][] var14 = room.getAllCorners(6);
      z = var14.length;

      int var8;
      for(var8 = 0; var8 < z; ++var8) {
         int[] coords = var14[var8];
         (new Wall(new SimpleBlock(data, coords[0], room.getY() + 1, coords[1]))).Pillar(room.getHeight(), this.rand, new Material[]{Material.STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.CRACKED_STONE_BRICKS});
      }

      Iterator var15 = room.getFourWalls(data, 6).entrySet().iterator();

      int i;
      while(var15.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var15.next();
         Wall w = ((Wall)entry.getKey()).getRelative(0, room.getHeight() / 2, 0);

         for(i = 0; i < (Integer)entry.getValue(); ++i) {
            if (!w.isSolid()) {
               if (i != (Integer)entry.getValue() / 2) {
                  w.setType(new Material[]{Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL});
               } else {
                  w.setType(slabs);
               }

               BlockUtils.correctSurroundingMultifacingData(w.get());
            }

            w = w.getLeft();
         }
      }

      BlockFace dir;
      if (room.getWidthX() > room.getWidthZ()) {
         dir = (new BlockFace[]{BlockFace.WEST, BlockFace.EAST})[this.rand.nextInt(1)];
      } else {
         dir = (new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH})[this.rand.nextInt(1)];
      }

      int[][] var19 = room.getCornersAlongFace(dir, 7);
      var8 = var19.length;

      int[] corner;
      for(i = 0; i < var8; ++i) {
         corner = var19[i];
         Wall w = new Wall(new SimpleBlock(data, corner[0], room.getY() + 1, corner[1]), dir.getOppositeFace());
         w.getRear().getRelative(0, room.getHeight() / 2, 0).setType(Material.AIR);
         BlockUtils.correctSurroundingMultifacingData(w.getRear().getUp().get());

         for(int i = 0; i < room.getHeight() / 2; ++i) {
            w.Pillar(room.getHeight() / 2 - i, this.rand, BlockUtils.stoneBricks);
            (new StairBuilder(new Material[]{Material.STONE_BRICK_STAIRS, Material.MOSSY_STONE_BRICK_STAIRS})).setFacing(dir).apply(w.getRelative(0, room.getHeight() / 2 - i - 1, 0));
            w = w.getFront();
         }
      }

      var19 = room.getCornersAlongFace(BlockFace.NORTH, 2);
      var8 = var19.length;

      for(i = 0; i < var8; ++i) {
         corner = var19[i];
         this.placePrisonCell(new SimpleBlock(data, corner[0], room.getY() + 1 + room.getHeight() / 2, corner[1]), BlockFace.SOUTH);
      }

      var19 = room.getCornersAlongFace(BlockFace.SOUTH, 2);
      var8 = var19.length;

      for(i = 0; i < var8; ++i) {
         corner = var19[i];
         this.placePrisonCell(new SimpleBlock(data, corner[0], room.getY() + 1 + room.getHeight() / 2, corner[1]), BlockFace.NORTH);
      }

      Iterator var21 = room.getFourWalls(data, 4).entrySet().iterator();

      while(var21.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var21.next();
         Wall w = ((Wall)entry.getKey()).getRelative(0, room.getHeight() / 2, 0);

         for(int i = 0; i < (Integer)entry.getValue(); ++i) {
            if (i == (Integer)entry.getValue() / 2) {
               w.getUp(3).LPillar(room.getHeight() / 2 - 2, this.rand, BlockUtils.stoneBricks);
               if (this.rand.nextBoolean()) {
                  data.addEntity(w.getX(), w.getY(), w.getZ(), EntityType.SKELETON);
               }
            } else if (i != (Integer)entry.getValue() / 2 + 1 && i != (Integer)entry.getValue() / 2 - 1) {
               w.LPillar(room.getHeight() / 2, this.rand, new Material[]{Material.IRON_BARS});
               w.CorrectMultipleFacing(room.getHeight() / 2);
            } else {
               w.LPillar(room.getHeight() / 2, this.rand, BlockUtils.stoneBricks);
            }

            if (GenUtils.chance(this.rand, 1, 35)) {
               (new ChestBuilder(Material.CHEST)).setFacing(w.getDirection()).setLootTable(TerraLootTable.STRONGHOLD_CORRIDOR).apply(w.getRear(3));
            }

            w = w.getLeft();
         }
      }

   }

   public void placePrisonCell(@NotNull SimpleBlock location, @NotNull BlockFace doorDir) {
      Wall w = new Wall(location);
      Material[] prisonMats = new Material[]{Material.STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.CRACKED_STONE_BRICKS, Material.IRON_BARS};
      int width = 2;
      int[] var6 = new int[]{-width, width};
      int var7 = var6.length;

      int var8;
      int nz;
      int nx;
      for(var8 = 0; var8 < var7; ++var8) {
         nz = var6[var8];

         for(nx = -width; nx <= width; ++nx) {
            w.getRelative(nz, 0, nx).LPillar(15, this.rand, prisonMats);
         }
      }

      var6 = new int[]{-width, width};
      var7 = var6.length;

      for(var8 = 0; var8 < var7; ++var8) {
         nz = var6[var8];

         for(nx = -width; nx <= width; ++nx) {
            w.getRelative(nx, 0, nz).LPillar(15, this.rand, prisonMats);
         }
      }

      SimpleBlock door = w.getRelative(doorDir, width).get();
      BlockUtils.placeDoor(door.getPopData(), Material.IRON_DOOR, door.getX(), door.getY(), door.getZ(), doorDir);
   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getHeight() > 8 && room.getWidthX() > 14 && room.getWidthZ() > 14 && !room.isHuge();
   }
}
