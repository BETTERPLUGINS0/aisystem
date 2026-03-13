package org.terraform.structure.stronghold;

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

public class StairwayTopPopulator extends RoomPopulatorAbstract {
   public StairwayTopPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      BlockFace[] var3 = BlockUtils.xzPlaneBlockFaces;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         BlockFace face = var3[var5];
         data.setType(room.getX() + face.getModX(), room.getY(), room.getZ() + face.getModZ(), Material.CAVE_AIR);
      }

      int bfIndex;
      for(bfIndex = 0; bfIndex < room.getHeight(); ++bfIndex) {
         data.setType(room.getX(), room.getY() + bfIndex, room.getZ(), BlockUtils.stoneBrick(this.rand));
      }

      int bfIndex = 2;
      BlockFace face = BlockUtils.xzPlaneBlockFaces[bfIndex];
      Slab bottom = (Slab)Bukkit.createBlockData(BlockUtils.stoneBrickSlab(this.rand));
      bottom.setType(Type.BOTTOM);
      data.setBlockData(room.getX() + face.getModX(), room.getY(), room.getZ() + face.getModZ(), bottom);
      bfIndex = getNextIndex(bfIndex);
      face = BlockUtils.xzPlaneBlockFaces[bfIndex];
      Slab top = (Slab)Bukkit.createBlockData(BlockUtils.stoneBrickSlab(this.rand));
      top.setType(Type.TOP);
      data.setBlockData(room.getX() + face.getModX(), room.getY(), room.getZ() + face.getModZ(), top);
   }

   public boolean canPopulate(CubeRoom room) {
      return false;
   }
}
