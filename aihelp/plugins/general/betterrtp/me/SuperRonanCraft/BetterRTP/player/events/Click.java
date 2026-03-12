package me.SuperRonanCraft.BetterRTP.player.events;

import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.invs.RTPInventories;
import me.SuperRonanCraft.BetterRTP.references.player.HelperPlayer;
import me.SuperRonanCraft.BetterRTP.references.player.playerdata.PlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Click {
   static void click(InventoryClickEvent e) {
      if (validClick(e)) {
         e.setCancelled(true);
         handler(e);
      }
   }

   private static void handler(InventoryClickEvent e) {
      try {
         Player p = (Player)e.getWhoClicked();
         PlayerData data = HelperPlayer.getData(p);
         RTPInventories menu = BetterRTP.getInstance().getInvs();
         menu.getInv(data.getMenu().getInvType()).clickEvent(e);
      } catch (NullPointerException var4) {
      }

   }

   private static boolean validClick(InventoryClickEvent e) {
      if (e.getWhoClicked() instanceof Player && !e.isCancelled()) {
         if (e.getCurrentItem() != null && !e.getCurrentItem().getType().equals(Material.AIR)) {
            if (e.getWhoClicked() instanceof Player) {
               PlayerData data = HelperPlayer.getData((Player)e.getWhoClicked());
               if (!e.getInventory().equals(data.getMenu().getInv())) {
                  return false;
               }

               if (!e.getClickedInventory().equals(data.getMenu().getInv())) {
                  e.setCancelled(true);
                  return false;
               }
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
