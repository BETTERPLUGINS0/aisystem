package com.volmit.iris.util.decree.handlers;

import com.volmit.iris.engine.object.IrisJigsawPool;
import com.volmit.iris.util.decree.specialhandlers.RegistrantHandler;

public class JigsawPoolHandler extends RegistrantHandler<IrisJigsawPool> {
   public JigsawPoolHandler() {
      super(IrisJigsawPool.class, true);
   }

   public String getRandomDefault() {
      return "jigsaw-pool";
   }
}
