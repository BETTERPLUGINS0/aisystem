package com.nisovin.shopkeepers.world;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.util.bukkit.LocationUtils;
import com.nisovin.shopkeepers.util.logging.Log;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ForcingEntitySpawner implements Listener {
   private final SKShopkeepersPlugin plugin;
   @Nullable
   private Location nextSpawnLocation = null;
   @Nullable
   private EntityType nextEntityType = null;

   public ForcingEntitySpawner(SKShopkeepersPlugin plugin) {
      this.plugin = plugin;
   }

   public void onEnable() {
      Bukkit.getPluginManager().registerEvents(this, this.plugin);
   }

   public void onDisable() {
      HandlerList.unregisterAll(this);
      this.resetForcedEntitySpawn();
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = false
   )
   void onEntitySpawn(EntitySpawnEvent event) {
      if (this.nextSpawnLocation != null) {
         if (this.matchesForcedCreatureSpawn(event)) {
            event.setCancelled(false);
         } else {
            Log.debug(() -> {
               String var10000 = String.valueOf(this.nextEntityType);
               return "Forced entity spawning seems to be out of sync: Forced spawning was activated for an entity of type " + var10000 + " at location " + String.valueOf(this.nextSpawnLocation) + ", but a different entity of type " + String.valueOf(event.getEntityType()) + " was spawned at location " + String.valueOf(event.getLocation()) + ".";
            });
         }

         this.resetForcedEntitySpawn();
      }
   }

   private boolean matchesForcedCreatureSpawn(EntitySpawnEvent event) {
      return event.getEntityType() == this.nextEntityType && LocationUtils.getSafeDistanceSquared(event.getLocation(), this.nextSpawnLocation) < 0.6D;
   }

   public void forceEntitySpawn(Location location, EntityType entityType) {
      this.nextSpawnLocation = location;
      this.nextEntityType = entityType;
   }

   public void resetForcedEntitySpawn() {
      this.nextSpawnLocation = null;
      this.nextEntityType = null;
   }
}
