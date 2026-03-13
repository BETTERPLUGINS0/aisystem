package com.volmit.iris.engine.framework;

import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.documentation.BlockCoordinates;
import com.volmit.iris.util.hunk.Hunk;

public abstract class EngineAssignedActuator<T> extends EngineAssignedComponent implements EngineActuator<T> {
   public EngineAssignedActuator(Engine engine, String name) {
      super(var1, var2);
   }

   public abstract void onActuate(int x, int z, Hunk<T> output, boolean multicore, ChunkContext context);

   @BlockCoordinates
   public void actuate(int x, int z, Hunk<T> output, boolean multicore, ChunkContext context) {
      this.onActuate(var1, var2, var3, var4, var5);
   }
}
