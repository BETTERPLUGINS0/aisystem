package org.terraform.coregen.populatordata;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public interface IPopulatorDataPhysicsCapable {
   void setType(int var1, int var2, int var3, Material var4, boolean var5);

   void setBlockData(int var1, int var2, int var3, BlockData var4, boolean var5);
}
