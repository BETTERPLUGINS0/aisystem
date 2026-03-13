package me.casperge.realisticseasons.paperlib.features.blockstatesnapshot;

import org.bukkit.block.Block;

public class BlockStateSnapshotOptionalSnapshots implements BlockStateSnapshot {
   public BlockStateSnapshotResult getBlockState(Block var1, boolean var2) {
      return new BlockStateSnapshotResult(var2, var1.getState(var2));
   }
}
