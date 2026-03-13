package org.terraform.structure.pyramid;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.main.config.TConfig;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.version.V_1_20;
import org.terraform.utils.version.Version;

public abstract class Antechamber extends RoomPopulatorAbstract {
   public Antechamber(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int[][] corners = room.getAllCorners(1);
      int[][] var4 = corners;
      int var5 = corners.length;

      int i;
      int length;
      for(i = 0; i < var5; ++i) {
         int[] corner = var4[i];
         Wall w = new Wall(new SimpleBlock(data, corner[0], room.getY() + room.getHeight() - 1, corner[1]));
         w.downLPillar(this.rand, 2, new Material[]{Material.CUT_SANDSTONE, Material.SMOOTH_SANDSTONE});
         BlockFace[] var9 = BlockUtils.directBlockFaces;
         int var10 = var9.length;

         for(length = 0; length < var10; ++length) {
            BlockFace face = var9[length];
            w.getRelative(face).setType((Material)GenUtils.randChoice((Object[])(Material.CUT_SANDSTONE, Material.SMOOTH_SANDSTONE)));
         }
      }

      int[] choices = new int[]{-2, -1, 0, 1, 2};
      int[] steps = new int[15];

      for(i = 0; i < 15; ++i) {
         steps[i] = choices[this.rand.nextInt(choices.length)];
      }

      SimpleBlock center = new SimpleBlock(data, room.getX(), room.getY(), room.getZ());
      BlockFace[] var16 = BlockUtils.directBlockFaces;
      int var18 = var16.length;

      int var20;
      BlockFace face;
      int i;
      for(var20 = 0; var20 < var18; ++var20) {
         face = var16[var20];
         length = room.getWidthX() / 2;
         if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
            length = room.getWidthZ() / 2;
         }

         for(i = 0; i < length; ++i) {
            if (face != BlockFace.NORTH && face != BlockFace.SOUTH) {
               center.getRelative(face, i).getRelative(0, 0, steps[i] * face.getModX()).setType(Material.ORANGE_TERRACOTTA);
            } else {
               center.getRelative(face, i).getRelative(steps[i] * face.getModZ(), 0, 0).setType(Material.ORANGE_TERRACOTTA);
            }
         }
      }

      center.setType(Material.BLUE_TERRACOTTA);
      if (Version.VERSION.isAtLeast(Version.v1_20)) {
         for(int i = 0; i < TConfig.c.STRUCTURES_PYRAMID_SUSPICIOUS_SAND_COUNT_PER_ANTECHAMBER; ++i) {
            SimpleBlock target = center.getRelative(GenUtils.getSign(this.rand) * GenUtils.randInt(this.rand, 1, room.getWidthX() / 2 - 1), 0, GenUtils.getSign(this.rand) * GenUtils.randInt(this.rand, 1, room.getWidthZ() / 2 - 1));
            target.setType(V_1_20.SUSPICIOUS_SAND);
            data.lootTableChest(target.getX(), target.getY(), target.getZ(), TerraLootTable.DESERT_PYRAMID_ARCHAEOLOGY);
         }
      }

      center = new SimpleBlock(data, room.getX(), room.getY() + room.getHeight(), room.getZ());
      var16 = BlockUtils.directBlockFaces;
      var18 = var16.length;

      for(var20 = 0; var20 < var18; ++var20) {
         face = var16[var20];
         length = room.getWidthX() / 2;
         if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
            length = room.getWidthZ() / 2;
         }

         for(i = 0; i < length; ++i) {
            if (face != BlockFace.NORTH && face != BlockFace.SOUTH) {
               center.getRelative(face, i).getRelative(0, 0, steps[i] * face.getModX()).setType(Material.ORANGE_TERRACOTTA);
            } else {
               center.getRelative(face, i).getRelative(steps[i] * face.getModZ(), 0, 0).setType(Material.ORANGE_TERRACOTTA);
            }
         }
      }

      center.setType(Material.BLUE_TERRACOTTA);
   }

   protected void randomRoomPlacement(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room, int lowerbound, int upperbound, Material... types) {
      for(int i = 0; i < GenUtils.randInt(lowerbound, upperbound); ++i) {
         int[] coords = room.randomCoords(this.rand, 1);
         BlockData bd = Bukkit.createBlockData((Material)GenUtils.randChoice((Object[])types));
         if (bd instanceof Waterlogged) {
            ((Waterlogged)bd).setWaterlogged(false);
         }

         if (!data.getType(coords[0], room.getY() + 1, coords[2]).isSolid()) {
            data.setBlockData(coords[0], room.getY() + 1, coords[2], bd);
         } else {
            data.setBlockData(coords[0], room.getY() + 2, coords[2], bd);
         }
      }

   }

   public boolean canPopulate(CubeRoom room) {
      return true;
   }
}
