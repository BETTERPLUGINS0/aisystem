package org.terraform.structure.pyramid;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Slab.Type;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;

public class PyramidStairwayTopPopulator extends RoomPopulatorAbstract {
   public PyramidStairwayTopPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int bfIndex = 6;
      BlockFace[] var4 = BlockUtils.xzPlaneBlockFaces;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         BlockFace face = var4[var6];
         data.setType(room.getX() + face.getModX(), room.getY(), room.getZ() + face.getModZ(), Material.CAVE_AIR);
      }

      for(int i = 0; i < room.getHeight(); ++i) {
         data.setType(room.getX(), room.getY() + i, room.getZ(), Material.CHISELED_SANDSTONE);
      }

      BlockFace face = BlockUtils.xzPlaneBlockFaces[bfIndex];
      Slab bottom = (Slab)Bukkit.createBlockData(Material.SANDSTONE_SLAB);
      bottom.setType(Type.BOTTOM);
      data.setBlockData(room.getX() + face.getModX(), room.getY(), room.getZ() + face.getModZ(), bottom);
      int bfIndex = getNextIndex(bfIndex);
      face = BlockUtils.xzPlaneBlockFaces[bfIndex];
      Slab top = (Slab)Bukkit.createBlockData(Material.SANDSTONE_SLAB);
      top.setType(Type.TOP);
      data.setBlockData(room.getX() + face.getModX(), room.getY(), room.getZ() + face.getModZ(), top);
      bfIndex = getNextIndex(bfIndex);
      face = BlockUtils.xzPlaneBlockFaces[bfIndex];
      data.setBlockData(room.getX() + face.getModX(), room.getY(), room.getZ() + face.getModZ(), top);
      bfIndex = getNextIndex(bfIndex);
      face = BlockUtils.xzPlaneBlockFaces[bfIndex];
      data.setBlockData(room.getX() + face.getModX(), room.getY(), room.getZ() + face.getModZ(), top);
   }

   public boolean canPopulate(CubeRoom room) {
      return false;
   }
}
