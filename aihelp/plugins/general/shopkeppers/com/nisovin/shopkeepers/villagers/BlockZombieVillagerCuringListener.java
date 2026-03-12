package com.nisovin.shopkeepers.villagers;

import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.logging.Log;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.EntityTransformEvent.TransformReason;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class BlockZombieVillagerCuringListener implements Listener {
   private boolean isZombieVillagerCuringDisabled(World world) {
      return Settings.disableZombieVillagerCuring && (Settings.disableZombieVillagerCuringWorlds.isEmpty() || Settings.disableZombieVillagerCuringWorlds.contains(world.getName()));
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onZombieVillagerCureStarted(PlayerInteractEntityEvent event) {
      if (event.getRightClicked() instanceof ZombieVillager) {
         Player player = event.getPlayer();
         ItemStack itemInHand = player.getInventory().getItem(event.getHand());
         if (itemInHand != null && itemInHand.getType() == Material.GOLDEN_APPLE) {
            if (this.isZombieVillagerCuringDisabled(player.getWorld())) {
               Log.debug(() -> {
                  return "Preventing zombie villager curing at " + TextUtils.getLocationString(player.getLocation());
               });
               event.setCancelled(true);
               TextUtils.sendMessage(player, (Text)Messages.zombieVillagerCuringDisabled);
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onZombieVillagerCured(EntityTransformEvent event) {
      if (event.getTransformReason() == TransformReason.CURED) {
         Entity var3 = event.getEntity();
         if (var3 instanceof ZombieVillager) {
            ZombieVillager zombieVillager = (ZombieVillager)var3;
            if (this.isZombieVillagerCuringDisabled(zombieVillager.getWorld())) {
               Log.debug(() -> {
                  return "Preventing zombie villager curing (transform) at " + TextUtils.getLocationString(zombieVillager.getLocation());
               });
               event.setCancelled(true);
               OfflinePlayer conversionOfflinePlayer = zombieVillager.getConversionPlayer();
               if (conversionOfflinePlayer != null) {
                  Player conversionPlayer = conversionOfflinePlayer.getPlayer();
                  if (conversionPlayer != null) {
                     TextUtils.sendMessage(conversionPlayer, (Text)Messages.zombieVillagerCuringDisabled);
                  }
               }

            }
         }
      }
   }
}
