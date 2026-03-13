package org.terraform.structure.room;

import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;

public class PathPopulatorData {
   public final BlockFace dir;
   public final int pathWidth;
   public SimpleBlock base;
   public boolean isOverlapped = false;
   public boolean isTurn;
   public boolean isEnd = false;

   public PathPopulatorData(SimpleBlock base, BlockFace dir, int pathWidth, boolean isTurn) {
      this.base = base;
      this.dir = dir;
      this.pathWidth = pathWidth;
      this.isTurn = isTurn;
   }

   public PathPopulatorData(@NotNull Wall base, int pathWidth) {
      this.base = base.get();
      this.dir = base.getDirection();
      this.pathWidth = pathWidth;
   }

   public int calcRemainder(int multiplier) {
      if (this.dir.getModX() != 0) {
         return this.base.getX() % multiplier;
      } else {
         return this.dir.getModZ() != 0 ? this.base.getZ() % multiplier : 0;
      }
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.base.getX();
      result = 31 * result + this.base.getY();
      result = 31 * result + this.base.getZ();
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof PathPopulatorData)) {
         return false;
      } else {
         PathPopulatorData other = (PathPopulatorData)obj;
         return this.base.getX() == other.base.getX() && this.base.getZ() == other.base.getZ() && this.base.getY() == other.base.getY();
      }
   }
}
