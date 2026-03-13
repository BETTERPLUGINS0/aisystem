package org.terraform.structure.room.path;

import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.utils.BlockUtils;

public class CavePathWriter extends PathWriter {
   private final float rXMod;
   private final float rYMod;
   private final float rZMod;
   private final int xOff;
   private final int yOff;
   private final int zOff;

   public CavePathWriter(float rXMod, float rYMod, float rZMod, int xOff, int yOff, int zOff) {
      this.rXMod = rXMod;
      this.rYMod = rYMod;
      this.rZMod = rZMod;
      this.xOff = xOff;
      this.yOff = yOff;
      this.zOff = zOff;
   }

   public void apply(@NotNull PopulatorDataAbstract popData, @NotNull TerraformWorld tw, @NotNull PathState.PathNode node) {
      BlockUtils.carveCaveAir((int)((long)node.center.hashCode() * tw.getSeed()), (float)node.pathRadius + this.rXMod, (float)node.pathRadius + this.rYMod, (float)node.pathRadius + this.rZMod, new SimpleBlock(popData, node.center.getRelative(this.xOff, this.yOff, this.zOff)), false, BlockUtils.caveCarveReplace);
   }
}
