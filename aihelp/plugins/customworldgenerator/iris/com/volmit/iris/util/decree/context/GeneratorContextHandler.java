package com.volmit.iris.util.decree.context;

import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.IrisBiomeGeneratorLink;
import com.volmit.iris.engine.object.IrisGenerator;
import com.volmit.iris.util.decree.DecreeContextHandler;
import com.volmit.iris.util.plugin.VolmitSender;

public class GeneratorContextHandler implements DecreeContextHandler<IrisGenerator> {
   public Class<IrisGenerator> getType() {
      return IrisGenerator.class;
   }

   public IrisGenerator handle(VolmitSender sender) {
      if (var1.isPlayer() && IrisToolbelt.isIrisWorld(var1.player().getWorld()) && IrisToolbelt.access(var1.player().getWorld()).getEngine() != null) {
         Engine var2 = IrisToolbelt.access(var1.player().getWorld()).getEngine();
         return (IrisGenerator)var2.getData().getGeneratorLoader().load(((IrisBiomeGeneratorLink)var2.getBiome(var1.player().getLocation()).getGenerators().getRandom()).getGenerator());
      } else {
         return null;
      }
   }
}
