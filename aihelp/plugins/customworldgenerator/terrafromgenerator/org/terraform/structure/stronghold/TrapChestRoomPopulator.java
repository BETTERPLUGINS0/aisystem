package org.terraform.structure.stronghold;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Chest;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class TrapChestRoomPopulator extends RoomPopulatorAbstract {
   public TrapChestRoomPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int y;
      int x;
      int z;
      for(y = 0; y < GenUtils.randInt(this.rand, 0, 5); ++y) {
         x = room.getX() + GenUtils.randInt(this.rand, -room.getWidthX() / 2, room.getWidthX() / 2);
         z = room.getZ() + GenUtils.randInt(this.rand, -room.getWidthZ() / 2, room.getWidthZ() / 2);
         data.setType(x, room.getY() + 1, z, Material.STONE_PRESSURE_PLATE);
         if (GenUtils.chance(this.rand, 4, 5)) {
            data.setType(x, room.getY() - 1, z, Material.TNT);
         }
      }

      y = room.getY() + 1;
      x = room.getX();
      z = room.getZ();
      if (GenUtils.chance(this.rand, 1, 2)) {
         data.setType(x, y, z, Material.TNT);
      } else {
         data.setType(x, y, z, Material.SMOOTH_STONE);
      }

      SimpleBlock core = new SimpleBlock(data, x, y, z);
      BlockFace[] var7 = BlockUtils.directBlockFaces;
      int var8 = var7.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         BlockFace face = var7[var9];
         core.getRelative(face).setType(Material.SMOOTH_STONE);
         core.getRelative(face).getUp().setType(Material.STONE_BRICK_STAIRS);
         Directional rot = (Directional)Bukkit.createBlockData(Material.STONE_BRICK_STAIRS);
         rot.setFacing(face.getOppositeFace());
         core.getRelative(face).getUp().setBlockData(rot);
         BlockFace[] var12 = BlockUtils.directBlockFaces;
         int var13 = var12.length;

         for(int var14 = 0; var14 < var13; ++var14) {
            BlockFace f = var12[var14];
            core.getRelative(face).getRelative(f).lsetType(Material.SMOOTH_STONE_SLAB);
         }
      }

      core.getUp().setType(Material.CHISELED_STONE_BRICKS);
      y = core.getUp(2).getY();
      Chest chest = (Chest)Bukkit.createBlockData(Material.TRAPPED_CHEST);
      chest.setFacing(BlockUtils.getDirectBlockFace(this.rand));
      data.setBlockData(x, y, z, chest);
      data.lootTableChest(x, y, z, TerraLootTable.STRONGHOLD_CROSSING);
   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return !room.isBig();
   }
}
