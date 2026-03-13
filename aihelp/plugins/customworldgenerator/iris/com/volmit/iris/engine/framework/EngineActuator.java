package com.volmit.iris.engine.framework;

import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.documentation.BlockCoordinates;
import com.volmit.iris.util.hunk.Hunk;

public interface EngineActuator<O> extends EngineComponent {
   @BlockCoordinates
   void actuate(int x, int z, Hunk<O> output, boolean multicore, ChunkContext context);
}
