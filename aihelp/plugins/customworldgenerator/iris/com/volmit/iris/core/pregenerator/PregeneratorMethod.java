package com.volmit.iris.core.pregenerator;

import com.volmit.iris.util.mantle.Mantle;

public interface PregeneratorMethod {
   void init();

   void close();

   void save();

   boolean supportsRegions(int x, int z, PregenListener listener);

   String getMethod(int x, int z);

   void generateRegion(int x, int z, PregenListener listener);

   void generateChunk(int x, int z, PregenListener listener);

   Mantle getMantle();
}
