package me.casperge.realisticseasons.paperlib.features.blockstatesnapshot;

import org.bukkit.block.BlockState;

public class BlockStateSnapshotResult {
   private final boolean isSnapshot;
   private final BlockState state;

   public BlockStateSnapshotResult(boolean var1, BlockState var2) {
      this.isSnapshot = var1;
      this.state = var2;
   }

   public boolean isSnapshot() {
      return this.isSnapshot;
   }

   public BlockState getState() {
      return this.state;
   }
}
