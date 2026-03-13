package com.volmit.iris.core.pregenerator.methods;

import com.volmit.iris.core.pregenerator.PregenListener;
import com.volmit.iris.core.pregenerator.PregeneratorMethod;
import com.volmit.iris.util.mantle.Mantle;

public class DummyPregenMethod implements PregeneratorMethod {
   public void init() {
   }

   public void close() {
   }

   public String getMethod(int x, int z) {
      return "Dummy";
   }

   public void save() {
   }

   public boolean supportsRegions(int x, int z, PregenListener listener) {
      return false;
   }

   public void generateRegion(int x, int z, PregenListener listener) {
   }

   public void generateChunk(int x, int z, PregenListener listener) {
   }

   public Mantle getMantle() {
      return null;
   }
}
