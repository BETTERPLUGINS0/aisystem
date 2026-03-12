package ac.grim.grimac.shaded.incendo.cloud.bukkit;

import java.util.Collection;
import java.util.Objects;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

final class CloudBukkitListener<C> implements Listener {
   private final BukkitCommandManager<C> bukkitCommandManager;

   CloudBukkitListener(@NonNull final BukkitCommandManager<C> bukkitCommandManager) {
      this.bukkitCommandManager = bukkitCommandManager;
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onPlayerLogin(@NonNull final AsyncPlayerPreLoginEvent event) {
      this.bukkitCommandManager.lockIfBrigadierCapable();
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   void onPluginDisable(@NonNull final PluginDisableEvent event) {
      if (event.getPlugin().equals(this.bukkitCommandManager.owningPlugin())) {
         Collection var10000 = this.bukkitCommandManager.rootCommands();
         BukkitCommandManager var10001 = this.bukkitCommandManager;
         Objects.requireNonNull(var10001);
         var10000.forEach(var10001::deleteRootCommand);
      }

   }
}
