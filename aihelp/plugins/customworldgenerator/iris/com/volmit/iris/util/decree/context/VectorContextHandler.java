package com.volmit.iris.util.decree.context;

import com.volmit.iris.util.decree.DecreeContextHandler;
import com.volmit.iris.util.plugin.VolmitSender;
import org.bukkit.util.Vector;

public class VectorContextHandler implements DecreeContextHandler<Vector> {
   public Class<Vector> getType() {
      return Vector.class;
   }

   public Vector handle(VolmitSender sender) {
      return var1.isPlayer() ? var1.player().getLocation().toVector() : null;
   }
}
