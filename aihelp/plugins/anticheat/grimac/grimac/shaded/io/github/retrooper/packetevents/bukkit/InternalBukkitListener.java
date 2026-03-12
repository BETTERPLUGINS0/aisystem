package ac.grim.grimac.shaded.io.github.retrooper.packetevents.bukkit;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEventsAPI;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.FakeChannelUtil;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.SpigotChannelInjector;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.manager.player.PlayerManagerImpl;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Internal
public class InternalBukkitListener implements Listener {
   static final String KICK_MESSAGE = "PacketEvents failed to inject into a channel";
   private final Plugin plugin;

   public InternalBukkitListener(Plugin plugin) {
      this.plugin = plugin;
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onLogin(PlayerLoginEvent event) {
      if (event.getResult() == Result.ALLOWED) {
         this.onPreJoin(event.getPlayer());
      }

   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onJoin(PlayerJoinEvent event) {
      this.onPostJoin(event.getPlayer());
   }

   void onPreJoin(Player player) {
      PacketEventsAPI<?> api = PacketEvents.getAPI();
      Map<UUID, WeakReference<Player>> map = ((PlayerManagerImpl)api.getPlayerManager()).joiningPlayers;
      map.put(player.getUniqueId(), new WeakReference(player));
   }

   void onPostJoin(Player player) {
      PacketEventsAPI<?> api = PacketEvents.getAPI();
      User user = api.getPlayerManager().getUser(player);
      if (user != null) {
         SpigotChannelInjector injector = (SpigotChannelInjector)PacketEvents.getAPI().getInjector();
         injector.setPlayer(user.getChannel(), player);
         ((PlayerManagerImpl)api.getPlayerManager()).joiningPlayers.remove(player.getUniqueId());
      } else {
         ((PlayerManagerImpl)api.getPlayerManager()).joiningPlayers.remove(player.getUniqueId());
         Object channel = api.getPlayerManager().getChannel(player);
         if ((channel == null || !FakeChannelUtil.isFakeChannel(channel)) && (!api.isTerminated() || api.getSettings().isKickIfTerminated())) {
            FoliaScheduler.getEntityScheduler().runDelayed(player, this.plugin, (__) -> {
               if (channel != null) {
                  if (!ChannelHelper.isOpen(channel)) {
                     return;
                  }
               } else if (!player.isOnline()) {
                  return;
               }

               player.kickPlayer("PacketEvents failed to inject into a channel");
            }, (Runnable)null, 0L);
         }
      }
   }
}
