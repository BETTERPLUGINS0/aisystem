package me.ag4.playershop.listener;

import java.util.Iterator;
import me.ag4.playershop.api.PlayerShopAPI;
import me.ag4.playershop.objects.Shop;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplodeEvent implements Listener {
   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onBreakByExplosion(EntityExplodeEvent e) {
      if (!e.isCancelled()) {
         Iterator var2 = e.blockList().iterator();

         while(var2.hasNext()) {
            Block block = (Block)var2.next();
            Shop shop = PlayerShopAPI.getPlayerShop(block.getLocation());
            if (shop != null) {
               e.blockList().remove(block);
            }
         }

      }
   }
}
