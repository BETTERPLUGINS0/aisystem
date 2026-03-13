package com.volmit.iris.util.decree.context;

import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.util.decree.DecreeContextHandler;
import com.volmit.iris.util.plugin.VolmitSender;

public class RegionContextHandler implements DecreeContextHandler<IrisRegion> {
   public Class<IrisRegion> getType() {
      return IrisRegion.class;
   }

   public IrisRegion handle(VolmitSender sender) {
      return var1.isPlayer() && IrisToolbelt.isIrisWorld(var1.player().getWorld()) && IrisToolbelt.access(var1.player().getWorld()).getEngine() != null ? IrisToolbelt.access(var1.player().getWorld()).getEngine().getRegion(var1.player().getLocation()) : null;
   }
}
