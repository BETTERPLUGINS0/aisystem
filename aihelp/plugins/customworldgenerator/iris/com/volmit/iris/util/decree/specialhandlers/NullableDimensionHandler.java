package com.volmit.iris.util.decree.specialhandlers;

import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.engine.object.IrisDimension;

public class NullableDimensionHandler extends RegistrantHandler<IrisDimension> {
   public NullableDimensionHandler() {
      super(IrisDimension.class, true);
   }

   public IrisDimension parse(String in, boolean force) {
      return var1.equalsIgnoreCase("default") ? (IrisDimension)this.parse(IrisSettings.get().getGenerator().getDefaultWorldType()) : (IrisDimension)super.parse(var1, var2);
   }

   public String getRandomDefault() {
      return "dimension";
   }
}
