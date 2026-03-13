package com.volmit.iris.engine.platform.studio;

import com.volmit.iris.engine.data.chunk.TerrainChunk;
import com.volmit.iris.engine.framework.Engine;
import lombok.Generated;

public abstract class EnginedStudioGenerator implements StudioGenerator {
   private final Engine engine;

   public abstract void generateChunk(Engine engine, TerrainChunk tc, int x, int z);

   @Generated
   public EnginedStudioGenerator(final Engine engine) {
      this.engine = var1;
   }
}
