package com.volmit.iris.engine.platform.studio;

import com.volmit.iris.engine.data.chunk.TerrainChunk;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.WrongEngineBroException;

public interface StudioGenerator {
   void generateChunk(Engine engine, TerrainChunk tc, int x, int z) throws WrongEngineBroException;
}
