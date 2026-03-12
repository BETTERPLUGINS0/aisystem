package ac.grim.grimac.manager.tick.impl;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.manager.tick.Tickable;
import ac.grim.grimac.player.GrimPlayer;
import java.util.Iterator;

public class ClearRecentlyUpdatedBlocks implements Tickable {
   private static final int maxTickAge = 2;

   public void tick() {
      Iterator var1 = GrimAPI.INSTANCE.getPlayerDataManager().getEntries().iterator();

      while(var1.hasNext()) {
         GrimPlayer player = (GrimPlayer)var1.next();
         player.blockHistory.cleanup(GrimAPI.INSTANCE.getTickManager().currentTick - 2);
      }

   }
}
