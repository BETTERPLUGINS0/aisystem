package org.terraform.coregen;

import java.util.ArrayList;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.data.SimpleBlock;

public abstract class BlockDataFixerAbstract {
   private final ArrayList<Vector> multifacing = new ArrayList();
   public boolean hasFlushed = false;

   @NotNull
   public Vector[] flush() {
      Vector[] stuff = (Vector[])this.multifacing.toArray(new Vector[0]);
      this.multifacing.clear();
      this.hasFlushed = true;
      return stuff;
   }

   public void pushChanges(Vector e) {
      this.multifacing.add(e);
   }

   @Nullable
   public abstract String updateSchematic(double var1, String var3);

   public abstract void correctFacing(Vector var1, SimpleBlock var2, BlockData var3, BlockFace var4);
}
