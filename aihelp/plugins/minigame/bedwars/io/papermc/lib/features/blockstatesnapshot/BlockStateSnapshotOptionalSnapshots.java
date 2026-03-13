/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.block.Block
 */
package io.papermc.lib.features.blockstatesnapshot;

import io.papermc.lib.features.blockstatesnapshot.BlockStateSnapshot;
import io.papermc.lib.features.blockstatesnapshot.BlockStateSnapshotResult;
import org.bukkit.block.Block;

public class BlockStateSnapshotOptionalSnapshots
implements BlockStateSnapshot {
    @Override
    public BlockStateSnapshotResult getBlockState(Block block, boolean useSnapshot) {
        return new BlockStateSnapshotResult(useSnapshot, block.getState(useSnapshot));
    }
}

