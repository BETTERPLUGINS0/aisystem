package org.terraform.structure.pyramid;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.jetbrains.annotations.NotNull;
import org.terraform.structure.room.PathPopulatorAbstract;
import org.terraform.structure.room.PathPopulatorData;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class PyramidDungeonPathPopulator extends PathPopulatorAbstract {
   private final Random rand;
   private final int height;

   public PyramidDungeonPathPopulator(Random rand) {
      this.rand = rand;
      this.height = 3;
   }

   public PyramidDungeonPathPopulator(Random rand, int height) {
      this.rand = rand;
      this.height = height;
   }

   public void populate(@NotNull PathPopulatorData ppd) {
      if (GenUtils.chance(this.rand, 1, 300)) {
         ppd.base.setType(Material.GRAVEL);
         ppd.base.getDown().setType(Material.TNT);
         ppd.base.getUp().setType(Material.STONE_PRESSURE_PLATE);

         int var4;
         for(int i = -2; i > -8; --i) {
            ppd.base.getRelative(0, i, 0).setType(Material.AIR);
            BlockFace[] var3 = BlockUtils.directBlockFaces;
            var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               BlockFace face = var3[var5];
               ppd.base.getRelative(face).getRelative(0, i, 0).setType(Material.AIR);
            }
         }

         BlockFace[] var7 = BlockUtils.directBlockFaces;
         int var8 = var7.length;

         for(var4 = 0; var4 < var8; ++var4) {
            BlockFace face = var7[var4];
            Directional torch = (Directional)Bukkit.createBlockData(Material.WALL_TORCH);
            torch.setFacing(face);
            ppd.base.getDown().getRelative(face).setBlockData(torch);
            ppd.base.getRelative(face).setType(Material.GRAVEL);
         }
      }

      if (GenUtils.chance(this.rand, 1, 100) && ppd.base.getRelative(0, this.height + 1, 0).isSolid()) {
         ppd.base.getRelative(0, this.height, 0).setType(Material.COBWEB);
      }

   }

   public int getPathWidth() {
      return 1;
   }

   public int getPathHeight() {
      return this.height;
   }
}
