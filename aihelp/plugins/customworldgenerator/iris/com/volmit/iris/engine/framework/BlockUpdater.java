package com.volmit.iris.engine.framework;

import com.volmit.iris.util.mantle.MantleChunk;
import com.volmit.iris.util.math.RNG;
import org.bukkit.Chunk;
import org.bukkit.block.data.BlockData;

public interface BlockUpdater {
   void catchBlockUpdates(int x, int y, int z, BlockData data);

   void updateChunk(Chunk c);

   void update(int x, int y, int z, Chunk c, MantleChunk mc, RNG rf);
}
