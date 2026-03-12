package com.nisovin.shopkeepers.shopkeeper.activation;

import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

class ChunkActivationListener implements Listener {
   private final ShopkeeperChunkActivator chunkActivator;

   ChunkActivationListener(ShopkeeperChunkActivator chunkActivator) {
      Validate.notNull(chunkActivator, (String)"chunkActivator is null");
      this.chunkActivator = chunkActivator;
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onChunkLoad(ChunkLoadEvent event) {
      Chunk chunk = event.getChunk();
      this.chunkActivator.onChunkLoad(chunk);
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onChunkUnload(ChunkUnloadEvent event) {
      Chunk chunk = event.getChunk();
      this.chunkActivator.onChunkUnload(chunk);
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onWorldLoad(WorldLoadEvent event) {
      World world = event.getWorld();
      this.chunkActivator.onWorldLoad(world);
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onWorldUnload(WorldUnloadEvent event) {
      World world = event.getWorld();
      this.chunkActivator.onWorldUnload(world);
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onPlayerJoin(PlayerJoinEvent event) {
      Player player = event.getPlayer();
      if (player.isOnline()) {
         this.chunkActivator.activatePendingNearbyChunksDelayed(player);
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onPlayerTeleport(PlayerTeleportEvent event) {
      Location targetLocation = event.getTo();
      if (targetLocation != null) {
         Player player = event.getPlayer();
         this.chunkActivator.activatePendingNearbyChunksDelayed(player);
      }
   }
}
