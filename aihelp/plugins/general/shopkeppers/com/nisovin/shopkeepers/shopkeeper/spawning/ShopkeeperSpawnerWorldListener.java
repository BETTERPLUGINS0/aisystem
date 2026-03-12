package com.nisovin.shopkeepers.shopkeeper.spawning;

import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;

class ShopkeeperSpawnerWorldListener implements Listener {
   private final ShopkeeperSpawner spawner;
   private final WorldSaveDespawner worldSaveDespawner;

   ShopkeeperSpawnerWorldListener(ShopkeeperSpawner spawner, WorldSaveDespawner worldSaveDespawner) {
      Validate.notNull(spawner, (String)"spawner is null");
      Validate.notNull(worldSaveDespawner, (String)"worldSaveDespawner is null");
      this.spawner = spawner;
      this.worldSaveDespawner = worldSaveDespawner;
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onWorldSave(WorldSaveEvent event) {
      World world = event.getWorld();
      this.worldSaveDespawner.onWorldSave(world);
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onWorldUnload(WorldUnloadEvent event) {
      World world = event.getWorld();
      this.spawner.onWorldUnload(world);
      this.worldSaveDespawner.onWorldUnload(world);
   }
}
