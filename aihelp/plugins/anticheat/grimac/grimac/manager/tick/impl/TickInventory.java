package ac.grim.grimac.manager.tick.impl;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.manager.tick.Tickable;
import ac.grim.grimac.player.GrimPlayer;
import java.util.Iterator;

public class TickInventory implements Tickable {
   public void tick() {
      Iterator var1 = GrimAPI.INSTANCE.getPlayerDataManager().getEntries().iterator();

      while(var1.hasNext()) {
         GrimPlayer player = (GrimPlayer)var1.next();
         player.inventory.inventory.getInventoryStorage().tickWithBukkit();
      }

   }
}
