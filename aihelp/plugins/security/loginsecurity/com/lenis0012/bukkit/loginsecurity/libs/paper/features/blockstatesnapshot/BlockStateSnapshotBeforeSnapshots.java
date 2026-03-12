package com.lenis0012.bukkit.loginsecurity.libs.paper.features.blockstatesnapshot;

import org.bukkit.block.Block;

public class BlockStateSnapshotBeforeSnapshots implements BlockStateSnapshot {
   public BlockStateSnapshotResult getBlockState(Block block, boolean useSnapshot) {
      return new BlockStateSnapshotResult(false, block.getState());
   }
}
