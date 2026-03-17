package com.bergerkiller.bukkit.tc.attachments.ui.block;

import com.bergerkiller.bukkit.common.wrappers.BlockData;

public interface BlockDataSelector {
   void onSelectedBlockDataChanged(BlockData var1);

   BlockData getSelectedBlockData();

   BlockDataSelector setSelectedBlockData(BlockData var1);
}
