package advancedplugins.pm2.cv.player;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.handler.PlayerWrapperHandler;
import advancedplugins.pm2.cv.api.player.PlayerWrapper;
import advancedplugins.pm2.cv.handler.PluginHandlerAdapter;
import advancedplugins.pm2.cv.handler.PluginHandlerOptions;
import advancedplugins.pm2.cv.service.PacketService;
import gnu.trove.map.hash.THashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

@PluginHandlerOptions(
   apiClass = PlayerWrapperHandler.class,
   eventListener = true
)
public final class PlayerWrapperHandlerImpl extends PluginHandlerAdapter implements PlayerWrapperHandler, Listener {
   private final Map<UUID, PlayerWrapperImpl> wrappers = new THashMap();
   private final PacketService packetService = (PacketService)InfiniteVehicles.getService(PacketService.class);

   @NotNull
   public PlayerWrapper getWrapper(@NotNull Player player) {
      return (PlayerWrapper)this.wrappers.computeIfAbsent(var1.getUniqueId(), (var2) -> {
         PlayerWrapperImpl var3 = new PlayerWrapperImpl(var1.getUniqueId());
         var3.value = var1;
         var3.pipeline = this.packetService.getChannelPipeline(var1);
         return var3;
      });
   }

   @NotNull
   public PlayerWrapper getWrapper(@NotNull UUID uuid) {
      return (PlayerWrapper)this.wrappers.computeIfAbsent(var1, (var2) -> {
         PlayerWrapperImpl var3 = new PlayerWrapperImpl(var1);
         Player var4 = Bukkit.getPlayer(var1);
         var3.value = var4;
         if (var4 != null) {
            var3.pipeline = this.packetService.getChannelPipeline(var4);
         }

         return var3;
      });
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onJoin(PlayerJoinEvent event) {
      Player var2 = var1.getPlayer();
      PlayerWrapperImpl var3 = (PlayerWrapperImpl)this.wrappers.get(var2.getUniqueId());
      if (var3 != null) {
         var3.value = var2;
         var3.pipeline = this.packetService.getChannelPipeline(var2);
      }

   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onDisconnect(PlayerQuitEvent event) {
      PlayerWrapperImpl var2 = (PlayerWrapperImpl)this.wrappers.get(var1.getPlayer().getUniqueId());
      if (var2 != null) {
         var2.value = null;
         var2.pipeline = null;
      }

   }
}
