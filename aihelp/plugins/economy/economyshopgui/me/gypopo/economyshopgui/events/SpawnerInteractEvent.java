package me.gypopo.economyshopgui.events;

import me.gypopo.economyshopgui.util.XMaterial;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SpawnerInteractEvent implements Listener {
   @EventHandler
   public void onPlayerInteract(PlayerInteractEvent e) {
      if (e.getAction() != Action.LEFT_CLICK_BLOCK) {
         if (e.getClickedBlock() != null && e.getClickedBlock().getType() == XMaterial.SPAWNER.parseMaterial()) {
            if (this.isSpawnEgg(e.getPlayer().getItemInHand())) {
               e.setCancelled(true);
            }
         }
      }
   }

   private boolean isSpawnEgg(ItemStack item) {
      return XMaterial.isNewVersion() ? item.getType().name().endsWith("SPAWN_EGG") : item.getType().name().contains("MONSTER_EGG");
   }
}
