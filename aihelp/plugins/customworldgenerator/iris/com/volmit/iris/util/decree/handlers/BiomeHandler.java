package com.volmit.iris.util.decree.handlers;

import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.util.decree.specialhandlers.RegistrantHandler;

public class BiomeHandler extends RegistrantHandler<IrisBiome> {
   public BiomeHandler() {
      super(IrisBiome.class, true);
   }

   public String getRandomDefault() {
      return "biome";
   }
}
