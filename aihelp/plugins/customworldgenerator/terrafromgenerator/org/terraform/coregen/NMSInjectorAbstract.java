package org.terraform.coregen;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Beehive;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.coregen.populatordata.PopulatorDataICAAbstract;

public abstract class NMSInjectorAbstract {
   public void startupTasks() {
   }

   @Nullable
   public BlockDataFixerAbstract getBlockDataFixer() {
      return null;
   }

   public abstract boolean attemptInject(World var1);

   public abstract PopulatorDataICAAbstract getICAData(Chunk var1);

   @Nullable
   public abstract PopulatorDataICAAbstract getICAData(PopulatorDataAbstract var1);

   public abstract void storeBee(Beehive var1);

   public void updatePhysics(World world, Block block) {
      throw new UnsupportedOperationException("Tried to update physics without implementing.");
   }

   public int getMinY() {
      return 0;
   }

   public int getMaxY() {
      return 256;
   }
}
