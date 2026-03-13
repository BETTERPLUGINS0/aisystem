package com.volmit.iris.util.decree.handlers;

import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.util.decree.specialhandlers.RegistrantHandler;

public class RegionHandler extends RegistrantHandler<IrisRegion> {
   public RegionHandler() {
      super(IrisRegion.class, true);
   }

   public String getRandomDefault() {
      return "region";
   }
}
