package com.volmit.iris.util.decree.context;

import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.util.decree.DecreeContextHandler;
import com.volmit.iris.util.plugin.VolmitSender;

public class DimensionContextHandler implements DecreeContextHandler<IrisDimension> {
   public Class<IrisDimension> getType() {
      return IrisDimension.class;
   }

   public IrisDimension handle(VolmitSender sender) {
      return var1.isPlayer() && IrisToolbelt.isIrisWorld(var1.player().getWorld()) && IrisToolbelt.access(var1.player().getWorld()).getEngine() != null ? IrisToolbelt.access(var1.player().getWorld()).getEngine().getDimension() : null;
   }
}
