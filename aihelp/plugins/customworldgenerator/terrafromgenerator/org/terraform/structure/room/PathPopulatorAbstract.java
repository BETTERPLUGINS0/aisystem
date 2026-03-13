package org.terraform.structure.room;

import org.bukkit.block.BlockFace;
import org.terraform.data.SimpleBlock;

public abstract class PathPopulatorAbstract {
   public int getPathWidth() {
      return 3;
   }

   public int getPathHeight() {
      return 3;
   }

   public int getPathMaxBend() {
      return -1;
   }

   public abstract void populate(PathPopulatorData var1);

   /** @deprecated */
   @Deprecated
   public boolean customCarve(SimpleBlock base, BlockFace dir, int pathWidth) {
      return false;
   }
}
