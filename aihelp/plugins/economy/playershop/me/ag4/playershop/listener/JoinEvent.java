package me.ag4.playershop.listener;

import me.ag4.playershop.PlayerShop;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {
   @EventHandler
   public void onJoin(PlayerJoinEvent e) {
      Player player = e.getPlayer();
      player.discoverRecipe(new NamespacedKey(PlayerShop.getInstance(), "player_shop"));
   }
}
