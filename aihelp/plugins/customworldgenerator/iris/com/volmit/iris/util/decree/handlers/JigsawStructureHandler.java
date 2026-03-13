package com.volmit.iris.util.decree.handlers;

import com.volmit.iris.engine.object.IrisJigsawStructure;
import com.volmit.iris.util.decree.specialhandlers.RegistrantHandler;

public class JigsawStructureHandler extends RegistrantHandler<IrisJigsawStructure> {
   public JigsawStructureHandler() {
      super(IrisJigsawStructure.class, true);
   }

   public String getRandomDefault() {
      return "jigsaw-structure";
   }
}
