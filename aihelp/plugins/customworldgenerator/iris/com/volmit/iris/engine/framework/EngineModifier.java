package com.volmit.iris.engine.framework;

import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.documentation.BlockCoordinates;
import com.volmit.iris.util.hunk.Hunk;

public interface EngineModifier<T> extends EngineComponent {
   @BlockCoordinates
   void modify(int x, int z, Hunk<T> t, boolean multicore, ChunkContext context);
}
