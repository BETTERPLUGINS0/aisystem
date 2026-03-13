package com.volmit.iris.util.decree.handlers;

import com.volmit.iris.engine.object.IrisEntity;
import com.volmit.iris.util.decree.specialhandlers.RegistrantHandler;

public class EntityHandler extends RegistrantHandler<IrisEntity> {
   public EntityHandler() {
      super(IrisEntity.class, false);
   }

   public String getRandomDefault() {
      return "entity";
   }
}
