package com.volmit.iris.util.decree.handlers;

import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.util.decree.specialhandlers.RegistrantHandler;

public class DimensionHandler extends RegistrantHandler<IrisDimension> {
   public DimensionHandler() {
      super(IrisDimension.class, false);
   }

   public IrisDimension parse(String in, boolean force) {
      return var1.equalsIgnoreCase("default") ? (IrisDimension)this.parse(IrisSettings.get().getGenerator().getDefaultWorldType()) : (IrisDimension)super.parse(var1, var2);
   }

   public String getRandomDefault() {
      return "dimension";
   }
}
