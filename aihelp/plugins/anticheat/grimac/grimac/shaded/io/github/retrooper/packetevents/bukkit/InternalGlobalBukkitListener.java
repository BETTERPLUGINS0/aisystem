package ac.grim.grimac.shaded.io.github.retrooper.packetevents.bukkit;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEventsAPI;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.UserLoginEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class InternalGlobalBukkitListener implements Listener {
   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent event) {
      PacketEventsAPI<?> api = PacketEvents.getAPI();
      User user = api.getPlayerManager().getUser(event.getPlayer());
      PacketEvents.getAPI().getEventManager().callEvent(new UserLoginEvent(user, event.getPlayer()));
   }
}
