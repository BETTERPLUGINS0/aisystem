package com.volmit.iris.util.decree.handlers;

import com.volmit.iris.engine.object.IrisGenerator;
import com.volmit.iris.util.decree.specialhandlers.RegistrantHandler;

public class GeneratorHandler extends RegistrantHandler<IrisGenerator> {
   public GeneratorHandler() {
      super(IrisGenerator.class, false);
   }

   public String getRandomDefault() {
      return "generator";
   }
}
