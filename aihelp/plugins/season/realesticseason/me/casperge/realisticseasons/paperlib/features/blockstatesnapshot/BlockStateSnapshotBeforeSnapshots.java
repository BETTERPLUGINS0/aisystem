package me.casperge.realisticseasons.paperlib.features.blockstatesnapshot;

import org.bukkit.block.Block;

public class BlockStateSnapshotBeforeSnapshots implements BlockStateSnapshot {
   public BlockStateSnapshotResult getBlockState(Block var1, boolean var2) {
      return new BlockStateSnapshotResult(false, var1.getState());
   }
}
