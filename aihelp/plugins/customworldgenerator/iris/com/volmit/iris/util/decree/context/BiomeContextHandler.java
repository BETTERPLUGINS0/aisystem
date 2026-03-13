package com.volmit.iris.util.decree.context;

import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.util.decree.DecreeContextHandler;
import com.volmit.iris.util.plugin.VolmitSender;

public class BiomeContextHandler implements DecreeContextHandler<IrisBiome> {
   public Class<IrisBiome> getType() {
      return IrisBiome.class;
   }

   public IrisBiome handle(VolmitSender sender) {
      return var1.isPlayer() && IrisToolbelt.isIrisWorld(var1.player().getWorld()) && IrisToolbelt.access(var1.player().getWorld()).getEngine() != null ? IrisToolbelt.access(var1.player().getWorld()).getEngine().getBiomeOrMantle(var1.player().getLocation()) : null;
   }
}
