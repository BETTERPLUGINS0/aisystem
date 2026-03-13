package com.volmit.iris.util.decree.context;

import com.volmit.iris.util.decree.DecreeContextHandler;
import com.volmit.iris.util.plugin.VolmitSender;
import org.bukkit.World;

public class WorldContextHandler implements DecreeContextHandler<World> {
   public Class<World> getType() {
      return World.class;
   }

   public World handle(VolmitSender sender) {
      return var1.isPlayer() ? var1.player().getWorld() : null;
   }
}
