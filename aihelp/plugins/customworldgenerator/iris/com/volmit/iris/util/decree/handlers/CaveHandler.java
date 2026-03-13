package com.volmit.iris.util.decree.handlers;

import com.volmit.iris.engine.object.IrisCave;
import com.volmit.iris.util.decree.specialhandlers.RegistrantHandler;

public class CaveHandler extends RegistrantHandler<IrisCave> {
   public CaveHandler() {
      super(IrisCave.class, true);
   }

   public String getRandomDefault() {
      return "cave";
   }
}
