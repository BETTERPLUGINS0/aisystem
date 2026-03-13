package com.volmit.iris.util.decree.handlers;

import com.volmit.iris.engine.object.IrisScript;
import com.volmit.iris.util.decree.specialhandlers.RegistrantHandler;

public class ScriptHandler extends RegistrantHandler<IrisScript> {
   public ScriptHandler() {
      super(IrisScript.class, false);
   }

   public String getRandomDefault() {
      return "script";
   }
}
