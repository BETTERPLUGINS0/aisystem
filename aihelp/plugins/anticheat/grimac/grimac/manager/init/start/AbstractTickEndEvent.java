package ac.grim.grimac.manager.init.start;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.player.GrimPlayer;

public abstract class AbstractTickEndEvent implements StartableInitable {
   public void start() {
   }

   protected void onEndOfTick(GrimPlayer player) {
      player.checkManager.getEntityReplication().onEndOfTickEvent();
   }

   protected boolean shouldInjectEndTick() {
      return GrimAPI.INSTANCE.getConfigManager().getConfig().getBooleanElse("Reach.enable-post-packet", false);
   }
}
