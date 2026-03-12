package ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.reflection.Reflection;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.bukkit.handlers.BukkitDecodeHandler;
import com.viaversion.viaversion.bukkit.handlers.BukkitEncodeHandler;
import io.netty.channel.Channel;
import java.lang.reflect.Field;
import org.bukkit.entity.Player;

public class ViaVersionAccessorImpl implements ViaVersionAccessor {
   private static Field CONNECTION_FIELD;

   public int getProtocolVersion(Player player) {
      return Via.getAPI().getPlayerVersion(player);
   }

   public int getProtocolVersion(User user) {
      try {
         Object viaEncoder = ((Channel)user.getChannel()).pipeline().get("via-encoder");
         if (CONNECTION_FIELD == null) {
            CONNECTION_FIELD = Reflection.getField(viaEncoder.getClass(), "connection");
         }

         UserConnection connection = (UserConnection)CONNECTION_FIELD.get(viaEncoder);
         return connection.getProtocolInfo().getProtocolVersion();
      } catch (IllegalAccessException var4) {
         PacketEvents.getAPI().getLogManager().warn("Unable to grab ViaVersion client version for player!");
         return -1;
      }
   }

   public Class<?> getUserConnectionClass() {
      return UserConnection.class;
   }

   public Class<?> getBukkitDecodeHandlerClass() {
      return BukkitDecodeHandler.class;
   }

   public Class<?> getBukkitEncodeHandlerClass() {
      return BukkitEncodeHandler.class;
   }
}
