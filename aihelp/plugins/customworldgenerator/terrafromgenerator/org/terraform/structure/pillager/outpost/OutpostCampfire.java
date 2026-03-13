package org.terraform.structure.pillager.outpost;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;

public class OutpostCampfire extends RoomPopulatorAbstract {
   public OutpostCampfire(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      SimpleBlock core = (new SimpleBlock(data, room.getX(), room.getY(), room.getZ())).getGroundOrSeaLevel();
      BlockUtils.replaceCircularPatch(this.rand.nextInt(12322), 3.0F, core, Material.COAL_ORE, Material.STONE, Material.COARSE_DIRT, Material.COARSE_DIRT, Material.COARSE_DIRT, Material.COARSE_DIRT);
      core = core.getUp();
      this.unitCampfire(core);
      BlockFace[] var4 = BlockUtils.xzDiagonalPlaneBlockFaces;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         BlockFace face = var4[var6];
         this.unitCampfire(core.getRelative(face).getGround().getUp());
      }

   }

   private void unitCampfire(@NotNull SimpleBlock block) {
      switch(this.rand.nextInt(3)) {
      case 0:
         block.setType(Material.CAMPFIRE);
         break;
      case 1:
         block.setType(Material.CAMPFIRE);
         block.getDown().setType(Material.HAY_BLOCK);
         break;
      case 2:
         block.setType(Material.HAY_BLOCK);
         block.getUp().setType(Material.CAMPFIRE);
         BlockFace[] var2 = BlockUtils.directBlockFaces;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            BlockFace face = var2[var4];
            SimpleBlock target = block.getRelative(face).getGround().getUp();
            if (!target.isSolid()) {
               target.setType(Material.CAMPFIRE);
            }
         }
      }

   }

   public boolean canPopulate(CubeRoom room) {
      return true;
   }
}
