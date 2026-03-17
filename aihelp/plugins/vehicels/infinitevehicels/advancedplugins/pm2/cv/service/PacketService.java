package advancedplugins.pm2.cv.service;

import advancedplugins.pm2.cv.api.player.PlayerWrapper;
import advancedplugins.pm2.cv.api.service.Service;
import advancedplugins.pm2.cv.packet.PacketWrapper;
import io.netty.channel.ChannelPipeline;
import java.util.List;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PacketService extends Service {
   ChannelPipeline getChannelPipeline(@NotNull Player player);

   void sendPacket(@NotNull Player player, @NotNull Object packet, boolean compress);

   default void sendPacket(@NotNull Player player, @NotNull Object packet) {
      this.sendPacket(player, packet, false);
   }

   default void sendPacket(@NotNull PlayerWrapper wrapper, @NotNull Object packet, boolean compress) {
      Player player = wrapper.get();
      if (player != null && player.isOnline()) {
         this.sendPacket(player, packet, compress);
      }

   }

   default void sendPacket(@NotNull PlayerWrapper wrapper, @NotNull Object packet) {
      this.sendPacket(wrapper, packet, false);
   }

   @Nullable
   PacketWrapper read(@NotNull Object packet);

   @NotNull
   Object createInstance(@NotNull PacketWrapper wrapper);

   default Object createPassengersPacket(int entityId, List<Integer> passengers) {
      return null;
   }
}
