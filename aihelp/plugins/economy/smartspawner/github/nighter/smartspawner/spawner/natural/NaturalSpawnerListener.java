package github.nighter.smartspawner.spawner.natural;

import com.destroystokyo.paper.event.entity.PreSpawnerSpawnEvent;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.spawner.data.SpawnerManager;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;

public class NaturalSpawnerListener implements Listener {
   private final SmartSpawner plugin;
   private final SpawnerManager spawnerManager;

   public NaturalSpawnerListener(SmartSpawner plugin) {
      this.plugin = plugin;
      this.spawnerManager = plugin.getSpawnerManager();
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onPreSpawnerSpawn(PreSpawnerSpawnEvent event) {
      SpawnerData smartSpawner = this.spawnerManager.getSpawnerByLocation(event.getSpawnerLocation());
      if (smartSpawner != null) {
         event.setCancelled(true);
         event.setShouldAbortSpawn(true);
      } else if (!this.plugin.getConfig().getBoolean("natural_spawner.spawn_mobs", true)) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onSpawnerSpawn(SpawnerSpawnEvent event) {
      if (event.getSpawner() != null) {
         SpawnerData smartSpawner = this.spawnerManager.getSpawnerByLocation(event.getSpawner().getLocation());
         if (smartSpawner != null) {
            event.setCancelled(true);
         } else if (!this.plugin.getConfig().getBoolean("natural_spawner.spawn_mobs", true)) {
            event.setCancelled(true);
         }

      }
   }
}
