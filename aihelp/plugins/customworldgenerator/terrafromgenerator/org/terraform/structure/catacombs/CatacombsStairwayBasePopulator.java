package org.terraform.structure.catacombs;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.BlockUtils;

public class CatacombsStairwayBasePopulator extends CatacombsStandardPopulator {
   public CatacombsStairwayBasePopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      SimpleBlock center = room.getCenterSimpleBlock(data).getUp();
      BlockFace stairFace = BlockFace.NORTH;

      for(int relY = 0; relY <= 14; ++relY) {
         SimpleBlock target = center.getUp(relY);
         BlockFace[] var7 = BlockUtils.flatBlockFaces3x3;
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            BlockFace face = var7[var9];
            target.getRelative(face).setType(Material.AIR);
            if (face == stairFace) {
               if (relY < 8) {
                  target.getRelative(face).downUntilSolid(new Random(), Material.ANDESITE, Material.COBBLESTONE, Material.STONE);
               } else {
                  if (!target.hasAdjacentSolid(BlockUtils.directBlockFaces)) {
                     break;
                  }

                  target.getRelative(face).setType(Material.ANDESITE, Material.COBBLESTONE, Material.STONE);
               }
            }
         }

         stairFace = BlockUtils.rotateXZPlaneBlockFace(stairFace, 1);
      }

   }
}
