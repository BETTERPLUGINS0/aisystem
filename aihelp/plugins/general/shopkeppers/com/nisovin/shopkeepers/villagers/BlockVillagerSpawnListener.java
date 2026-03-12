package com.nisovin.shopkeepers.villagers;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.EntitiesLoadEvent;

public class BlockVillagerSpawnListener implements Listener {
   private boolean isSpawnBlockingBypassed(SpawnReason spawnReason) {
      switch(spawnReason) {
      case COMMAND:
      case CUSTOM:
      case SPAWNER_EGG:
      case SPAWNER:
      case CURED:
         return true;
      default:
         return false;
      }
   }

   private boolean isSpawningBlocked(EntityType entityType, World world) {
      if (entityType == EntityType.VILLAGER) {
         return Settings.blockVillagerSpawns && (Settings.blockVillagerSpawnsWorlds.isEmpty() || Settings.blockVillagerSpawnsWorlds.contains(world.getName()));
      } else if (entityType != EntityType.WANDERING_TRADER && entityType != EntityType.TRADER_LLAMA) {
         return false;
      } else {
         return Settings.blockWanderingTraderSpawns && (Settings.blockWanderingTraderSpawnsWorlds.isEmpty() || Settings.blockWanderingTraderSpawnsWorlds.contains(world.getName()));
      }
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onSpawn(CreatureSpawnEvent event) {
      SpawnReason spawnReason = event.getSpawnReason();
      if (!this.isSpawnBlockingBypassed(spawnReason)) {
         EntityType entityType = event.getEntityType();
         World world = (World)Unsafe.assertNonNull(event.getLocation().getWorld());
         if (this.isSpawningBlocked(entityType, world)) {
            Log.debug(() -> {
               String var10000 = String.valueOf(entityType);
               return "Preventing mob spawn of " + var10000 + " at " + TextUtils.getLocationString(event.getLocation());
            });
            event.setCancelled(true);
         }

      }
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onChunkLoad(ChunkLoadEvent event) {
      if (event.isNewChunk()) {
         Chunk chunk = event.getChunk();
         if (chunk.isEntitiesLoaded()) {
            this.removeSpawnBlockedEntities(Arrays.asList(chunk.getEntities()));
         }
      }
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onChunkEntitiesLoaded(EntitiesLoadEvent event) {
      Chunk chunk = event.getChunk();
      if (chunk.isLoaded()) {
         this.removeSpawnBlockedEntities(event.getEntities());
      }
   }

   private void removeSpawnBlockedEntities(List<Entity> entities) {
      entities.forEach((entity) -> {
         EntityType entityType = entity.getType();
         if (this.isSpawningBlocked(entityType, entity.getWorld())) {
            Log.debug(() -> {
               String var10000 = String.valueOf(entityType);
               return "Preventing mob spawn (chunk loading) of " + var10000 + " at " + TextUtils.getLocationString(entity.getLocation());
            });
            entity.remove();
         }

      });
   }
}
