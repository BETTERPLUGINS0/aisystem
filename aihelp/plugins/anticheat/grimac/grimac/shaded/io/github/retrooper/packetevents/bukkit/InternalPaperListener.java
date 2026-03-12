package ac.grim.grimac.shaded.io.github.retrooper.packetevents.bukkit;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

@NullMarked
@ApiStatus.Internal
public class InternalPaperListener implements Listener {
   private final InternalBukkitListener delegate;

   public InternalPaperListener(Plugin plugin) {
      this.delegate = new InternalBukkitListener(plugin);
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onSpawnLocation(PlayerSpawnLocationEvent event) {
      this.delegate.onPreJoin(event.getPlayer());
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onJoin(PlayerJoinEvent event) {
      this.delegate.onPostJoin(event.getPlayer());
   }
}
