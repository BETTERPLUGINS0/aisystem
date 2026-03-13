package com.volmit.iris.engine.framework;

import com.volmit.iris.Iris;
import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.documentation.BlockCoordinates;
import com.volmit.iris.util.hunk.Hunk;

public abstract class EngineAssignedModifier<T> extends EngineAssignedComponent implements EngineModifier<T> {
   public EngineAssignedModifier(Engine engine, String name) {
      super(var1, var2);
   }

   @BlockCoordinates
   public abstract void onModify(int x, int z, Hunk<T> output, boolean multicore, ChunkContext context);

   @BlockCoordinates
   public void modify(int x, int z, Hunk<T> output, boolean multicore, ChunkContext context) {
      try {
         this.onModify(var1, var2, var3, var4, var5);
      } catch (Throwable var7) {
         Iris.error("Modifier Failure: " + this.getName());
         var7.printStackTrace();
      }

   }
}
