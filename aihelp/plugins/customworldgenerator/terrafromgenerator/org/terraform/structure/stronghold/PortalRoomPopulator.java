package org.terraform.structure.stronghold;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.Slab.Type;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class PortalRoomPopulator extends RoomPopulatorAbstract {
   public PortalRoomPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   @NotNull
   private Slab randTopSlab() {
      Slab slab = (Slab)Bukkit.createBlockData(BlockUtils.stoneBrickSlab(this.rand));
      slab.setType(Type.TOP);
      return slab;
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int[] lowerBounds = room.getLowerCorner();
      int[] upperBounds = room.getUpperCorner();
      Iterator var5 = room.getFourWalls(data, 0).entrySet().iterator();

      Wall w;
      int i;
      int h;
      while(var5.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var5.next();
         w = ((Wall)entry.getKey()).getUp(3);

         for(i = 1; i <= (Integer)entry.getValue(); ++i) {
            w.setType(Material.CHISELED_STONE_BRICKS);
            if (i % 5 == 2 || i % 5 == 4) {
               w.getUp().Pillar(8, this.rand, new Material[]{Material.CHISELED_STONE_BRICKS});
            }

            if (i % 5 == 3) {
               w.getUp().Pillar(8, this.rand, new Material[]{Material.COBBLESTONE_WALL});
            }

            for(h = 1; h < 8; ++h) {
               BlockUtils.correctSurroundingMultifacingData(w.getRelative(0, h, 0).get());
            }

            w = w.getLeft();
         }
      }

      int x;
      int z;
      for(x = lowerBounds[0] + 1; x < upperBounds[0]; ++x) {
         for(z = lowerBounds[1] + 1; z < upperBounds[1]; ++z) {
            data.setType(x, room.getY(), z, Material.OBSIDIAN);
         }
      }

      for(x = lowerBounds[0] + 2; x < upperBounds[0] - 1; ++x) {
         for(z = lowerBounds[1] + 2; z < upperBounds[1] - 1; ++z) {
            data.setType(x, room.getY(), z, BlockUtils.stoneBrick(this.rand));
         }
      }

      for(x = lowerBounds[0] + 1; x < upperBounds[0]; ++x) {
         for(z = lowerBounds[1] + 1; z < upperBounds[1]; ++z) {
            data.setType(x, room.getY() + room.getHeight() - 1, z, Material.COBBLESTONE);
            data.setType(x, room.getY() + room.getHeight() - 2, z, Material.COBBLESTONE);
         }
      }

      for(x = lowerBounds[0] + 3; x <= upperBounds[0] - 3; ++x) {
         for(z = lowerBounds[1] + 3; z <= upperBounds[1] - 3; ++z) {
            data.setType(x, room.getY() + room.getHeight() - 1, z, Material.CAVE_AIR);
         }
      }

      for(x = lowerBounds[0] + 2; x <= upperBounds[0] - 2; ++x) {
         for(z = lowerBounds[1] + 2; z <= upperBounds[1] - 2; ++z) {
            data.setType(x, room.getY() + room.getHeight() - 2, z, Material.CAVE_AIR);
         }
      }

      SimpleBlock ceil = new SimpleBlock(data, room.getX(), room.getHeight() - 1 + room.getY(), room.getZ());
      this.ceilDecor(ceil);
      SimpleBlock base = new SimpleBlock(data, room.getX(), room.getY() + 2, room.getZ());
      BlockFace[] var15 = BlockUtils.directBlockFaces;
      i = var15.length;

      BlockFace face;
      Wall w;
      for(h = 0; h < i; ++h) {
         face = var15[h];
         w = new Wall(base.getRelative(face).getRelative(face), face);
         EndPortalFrame portalFrame = (EndPortalFrame)Bukkit.createBlockData(Material.END_PORTAL_FRAME);
         portalFrame.setFacing(face.getOppositeFace());
         portalFrame.setEye(GenUtils.chance(this.rand, 1, 10));
         w.setBlockData(portalFrame);
         portalFrame.setEye(GenUtils.chance(this.rand, 1, 10));
         w.getLeft().setBlockData(portalFrame);
         portalFrame.setEye(GenUtils.chance(this.rand, 1, 10));
         w.getRight().setBlockData(portalFrame);
         w.getLeft().getLeft().setType(Material.CHISELED_STONE_BRICKS);
         w.getRight().getRight().setType(Material.CHISELED_STONE_BRICKS);
         w = w.getDown();
         w.setType(BlockUtils.stoneBrick(this.rand));
         w.getLeft().setType(BlockUtils.stoneBrick(this.rand));
         w.getRight().setType(BlockUtils.stoneBrick(this.rand));
      }

      base = new SimpleBlock(data, room.getX(), room.getY() + 1, room.getZ());

      for(int nx = -1; nx <= 1; ++nx) {
         for(i = -1; i <= 1; ++i) {
            base.getRelative(nx, 0, i).setType(Material.LAVA);
         }
      }

      var15 = BlockUtils.directBlockFaces;
      i = var15.length;

      for(h = 0; h < i; ++h) {
         face = var15[h];
         w = new Wall(base.getRelative(face).getRelative(face).getRelative(face), face);
         w.setType(BlockUtils.stoneBrick(this.rand));
         w.getLeft().setType(BlockUtils.stoneBrick(this.rand));
         w.getRight().setType(BlockUtils.stoneBrick(this.rand));
         w.getLeft().getLeft().setType(BlockUtils.stoneBrick(this.rand));
         w.getRight().getRight().setType(BlockUtils.stoneBrick(this.rand));
         w.getUp().setType(BlockUtils.stoneBrickSlab(this.rand));
         w.getUp().getLeft().setType(BlockUtils.stoneBrickSlab(this.rand));
         w.getUp().getRight().setType(BlockUtils.stoneBrickSlab(this.rand));
         w.getLeft().getLeft().getLeft().setType(BlockUtils.stoneBrickSlab(this.rand));
         w.getRight().getRight().getRight().setType(BlockUtils.stoneBrickSlab(this.rand));
         Stairs stairs = (Stairs)Bukkit.createBlockData(Material.POLISHED_ANDESITE_STAIRS);
         stairs.setFacing(face.getOppositeFace());
         w.getFront().setBlockData(stairs);
         w.getLeft().getFront().setBlockData(stairs);
         w.getRight().getFront().setBlockData(stairs);
         w.getLeft().getFront().getLeft().setType(BlockUtils.stoneBrickSlab(this.rand));
         w.getRight().getFront().getRight().setType(BlockUtils.stoneBrickSlab(this.rand));
      }

      this.decoratedPillar(this.rand, data, room.getX() + 6, room.getY() + 1, room.getZ() + 6, room.getHeight() - 2);
      this.decoratedPillar(this.rand, data, room.getX() - 6, room.getY() + 1, room.getZ() + 6, room.getHeight() - 2);
      this.decoratedPillar(this.rand, data, room.getX() + 6, room.getY() + 1, room.getZ() - 6, room.getHeight() - 2);
      this.decoratedPillar(this.rand, data, room.getX() - 6, room.getY() + 1, room.getZ() - 6, room.getHeight() - 2);
      this.lavaPool(data, room.getX() + 8, room.getY() + 1, room.getZ(), room.getHeight() - 2);
      this.lavaPool(data, room.getX() - 8, room.getY() + 1, room.getZ(), room.getHeight() - 2);
      w = new Wall(new SimpleBlock(data, room.getX() + 6, room.getY() + 1, room.getZ()), BlockFace.WEST);

      Stairs stairs;
      for(i = 0; i < 4; ++i) {
         w.setType(Material.LAVA);
         if (i != 0 && i != 3) {
            stairs = (Stairs)Bukkit.createBlockData(Material.POLISHED_ANDESITE_STAIRS);
            stairs.setFacing(BlockUtils.getAdjacentFaces(w.getDirection())[1]);
            w.getLeft().setBlockData(stairs);
            stairs = (Stairs)Bukkit.createBlockData(Material.POLISHED_ANDESITE_STAIRS);
            stairs.setFacing(BlockUtils.getAdjacentFaces(w.getDirection())[0]);
            w.getRight().setBlockData(stairs);
         } else {
            w.getLeft().setType(Material.CHISELED_STONE_BRICKS);
            w.getRight().setType(Material.CHISELED_STONE_BRICKS);
         }

         w = w.getFront();
      }

      w = new Wall(new SimpleBlock(data, room.getX() - 6, room.getY() + 1, room.getZ()), BlockFace.EAST);

      for(i = 0; i < 4; ++i) {
         w.setType(Material.LAVA);
         if (i != 0 && i != 3) {
            stairs = (Stairs)Bukkit.createBlockData(Material.POLISHED_ANDESITE_STAIRS);
            stairs.setFacing(BlockUtils.getAdjacentFaces(w.getDirection())[1]);
            w.getLeft().setBlockData(stairs);
            stairs = (Stairs)Bukkit.createBlockData(Material.POLISHED_ANDESITE_STAIRS);
            stairs.setFacing(BlockUtils.getAdjacentFaces(w.getDirection())[0]);
            w.getRight().setBlockData(stairs);
         } else {
            w.getLeft().setType(Material.CHISELED_STONE_BRICKS);
            w.getRight().setType(Material.CHISELED_STONE_BRICKS);
         }

         w = w.getFront();
      }

   }

   public void lavaPool(@NotNull PopulatorDataAbstract data, int x, int y, int z, int height) {
      BlockFace[] var6 = BlockUtils.directBlockFaces;
      int nz = var6.length;

      for(int var8 = 0; var8 < nz; ++var8) {
         BlockFace face = var6[var8];
         Wall w = (new Wall(new SimpleBlock(data, x, y, z), face)).getFront().getFront();
         Stairs stairs = (Stairs)Bukkit.createBlockData(Material.POLISHED_ANDESITE_STAIRS);
         stairs.setFacing(face.getOppositeFace());
         w.setBlockData(stairs);
         w.getLeft().setBlockData(stairs);
         w.getRight().setBlockData(stairs);
         w.getRight().getRight().setType(Material.CHISELED_STONE_BRICKS);
      }

      int nx;
      for(nx = -1; nx <= 1; ++nx) {
         for(nz = -1; nz <= 1; ++nz) {
            data.setType(x + nx, y, z + nz, Material.LAVA);
         }
      }

      for(nx = -1; nx <= 1; ++nx) {
         for(nz = -1; nz <= 1; ++nz) {
            data.setType(x + nx, y + height, z + nz, Material.CHISELED_STONE_BRICKS);
            if (nx == 0 && nz == 0) {
               data.setType(x, y + height, z, Material.LAVA);
            }
         }
      }

   }

   public void decoratedPillar(@NotNull Random rand, @NotNull PopulatorDataAbstract data, int x, int y, int z, int height) {
      BlockUtils.spawnPillar(rand, data, x, y, z, Material.CHISELED_STONE_BRICKS, height, height);
      BlockUtils.spawnPillar(rand, data, x + 1, y, z + 1, Material.COBBLESTONE_WALL, height, height);
      BlockUtils.spawnPillar(rand, data, x - 1, y, z + 1, Material.COBBLESTONE_WALL, height, height);
      BlockUtils.spawnPillar(rand, data, x + 1, y, z - 1, Material.COBBLESTONE_WALL, height, height);
      BlockUtils.spawnPillar(rand, data, x - 1, y, z - 1, Material.COBBLESTONE_WALL, height, height);
      data.setType(x + 1, y, z + 1, Material.CHISELED_STONE_BRICKS);
      data.setType(x - 1, y, z + 1, Material.CHISELED_STONE_BRICKS);
      data.setType(x + 1, y, z - 1, Material.CHISELED_STONE_BRICKS);
      data.setType(x - 1, y, z - 1, Material.CHISELED_STONE_BRICKS);
      BlockFace[] var7 = BlockUtils.directBlockFaces;
      int var8 = var7.length;

      int var9;
      BlockFace face;
      Stairs stairs;
      for(var9 = 0; var9 < var8; ++var9) {
         face = var7[var9];
         stairs = (Stairs)Bukkit.createBlockData(Material.POLISHED_ANDESITE_STAIRS);
         stairs.setFacing(face.getOppositeFace());
         data.setBlockData(x + face.getModX(), y, z + face.getModZ(), stairs);
      }

      var7 = BlockUtils.directBlockFaces;
      var8 = var7.length;

      for(var9 = 0; var9 < var8; ++var9) {
         face = var7[var9];
         stairs = (Stairs)Bukkit.createBlockData(Material.POLISHED_ANDESITE_STAIRS);
         stairs.setFacing(face.getOppositeFace());
         stairs.setHalf(Half.TOP);
         data.setBlockData(x + face.getModX(), y + height, z + face.getModZ(), stairs);
      }

      data.setType(x + 1, y + height, z + 1, Material.CHISELED_STONE_BRICKS);
      data.setType(x - 1, y + height, z + 1, Material.CHISELED_STONE_BRICKS);
      data.setType(x + 1, y + height, z - 1, Material.CHISELED_STONE_BRICKS);
      data.setType(x - 1, y + height, z - 1, Material.CHISELED_STONE_BRICKS);
   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getWidthX() == 25 && room.getWidthZ() == 25 && room.getHeight() == 15;
   }

   private void ceilDecor(@NotNull SimpleBlock ceil) {
      ceil.setType(Material.CHISELED_STONE_BRICKS);
      ceil.getRelative(0, 0, -1).setType(Material.CHISELED_STONE_BRICKS);
      ceil.getRelative(0, 0, -2).setType(Material.MOSSY_STONE_BRICKS);
      ceil.getRelative(0, 0, -3).setType(Material.MOSSY_COBBLESTONE);
      ceil.getRelative(0, 0, -4).setBlockData(this.randTopSlab());
      ceil.getRelative(0, 0, 1).setType(Material.CHISELED_STONE_BRICKS);
      ceil.getRelative(0, 0, 2).setType(Material.MOSSY_STONE_BRICKS);
      ceil.getRelative(0, 0, 3).setType(Material.MOSSY_COBBLESTONE);
      ceil.getRelative(0, 0, 4).setBlockData(this.randTopSlab());
      int[] var2 = new int[]{-1, 1};
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int i = var2[var4];
         ceil.getRelative(i, 0, -1).setType(Material.MOSSY_STONE_BRICKS);
         ceil.getRelative(i, 0, 0).setType(Material.MOSSY_STONE_BRICKS);
         ceil.getRelative(i, 0, 1).setType(Material.MOSSY_STONE_BRICKS);
         ceil.getRelative(2 * i, 0, -1).setType(Material.MOSSY_COBBLESTONE);
         ceil.getRelative(2 * i, 0, 0).setType(Material.MOSSY_COBBLESTONE);
         ceil.getRelative(2 * i, 0, 1).setType(Material.MOSSY_COBBLESTONE);
         ceil.getRelative(i, 0, -2).setType(Material.MOSSY_COBBLESTONE);
         ceil.getRelative(i, 0, 2).setType(Material.MOSSY_COBBLESTONE);
         SimpleBlock[] blocks = new SimpleBlock[]{ceil.getRelative(3 * i, 0, -1), ceil.getRelative(3 * i, 0, 0), ceil.getRelative(3 * i, 0, 1), ceil.getRelative(i, 0, 3), ceil.getRelative(i, 0, -3), ceil.getRelative(2 * i, 0, 2), ceil.getRelative(2 * i, 0, -2)};
         SimpleBlock[] var7 = blocks;
         int var8 = blocks.length;

         int var9;
         SimpleBlock b;
         for(var9 = 0; var9 < var8; ++var9) {
            b = var7[var9];
            b.setType(Material.COBBLESTONE);
         }

         var7 = blocks;
         var8 = blocks.length;

         for(var9 = 0; var9 < var8; ++var9) {
            b = var7[var9];
            BlockFace[] var11 = BlockUtils.directBlockFaces;
            int var12 = var11.length;

            for(int var13 = 0; var13 < var12; ++var13) {
               BlockFace face = var11[var13];
               b.getRelative(face).lsetBlockData(this.randTopSlab());
            }
         }
      }

   }
}
