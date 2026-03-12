package xyz.xenondevs.particle.utils;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerConnectionCache implements Listener {
   private final Map<Player, Object> cache = new HashMap();

   public PlayerConnectionCache() {
      if (ReflectionUtils.getPlugin() != null) {
         this.registerListener();
      }

   }

   public void registerListener() {
      Bukkit.getServer().getPluginManager().registerEvents(this, ReflectionUtils.getPlugin());
   }

   public Object getConnection(Player player) {
      Object connection = this.cache.get(player);
      if (connection == null) {
         connection = ReflectionUtils.getPlayerConnection(player);
         if (player.isOnline()) {
            this.cache.put(player, connection);
         }
      }

      return connection;
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void handlePlayerQuit(PlayerQuitEvent event) {
      this.cache.remove(event.getPlayer());
   }
}
