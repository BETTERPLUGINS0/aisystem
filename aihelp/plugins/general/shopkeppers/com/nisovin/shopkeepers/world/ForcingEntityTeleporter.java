package com.nisovin.shopkeepers.world;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ForcingEntityTeleporter implements Listener {
   private final SKShopkeepersPlugin plugin;
   @Nullable
   private UUID nextTeleportEntityUuid = null;
   @Nullable
   private Location toLocation = null;

   public ForcingEntityTeleporter(SKShopkeepersPlugin plugin) {
      this.plugin = plugin;
   }

   public void onEnable() {
      Bukkit.getPluginManager().registerEvents(this, this.plugin);
   }

   public void onDisable() {
      HandlerList.unregisterAll(this);
      this.resetForcedEntityTeleport();
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = false
   )
   void onEntityTeleport(EntityTeleportEvent event) {
      if (this.nextTeleportEntityUuid != null) {
         if (this.matchesForcedEntityTeleport(event)) {
            event.setCancelled(false);
            event.setTo(this.toLocation);
         } else {
            Log.debug(() -> {
               String var10000 = String.valueOf(this.nextTeleportEntityUuid);
               return "Forced entity teleporting seems to be out of sync: A forced teleport was requested for entity with unique id " + var10000 + ", but a different entity was teleported: " + String.valueOf(event.getEntity().getUniqueId());
            });
         }

         this.resetForcedEntityTeleport();
      }
   }

   private boolean matchesForcedEntityTeleport(EntityTeleportEvent event) {
      return event.getEntity().getUniqueId().equals(this.nextTeleportEntityUuid);
   }

   public boolean teleport(Entity entity, Location toLocation) {
      this.nextTeleportEntityUuid = entity.getUniqueId();
      this.toLocation = toLocation;
      boolean result = entity.teleport(toLocation);
      this.resetForcedEntityTeleport();
      boolean customNameVisible = entity.isCustomNameVisible();
      entity.setCustomNameVisible(!customNameVisible);
      entity.setCustomNameVisible(customNameVisible);
      return result;
   }

   public void resetForcedEntityTeleport() {
      this.nextTeleportEntityUuid = null;
      this.toLocation = null;
   }
}
