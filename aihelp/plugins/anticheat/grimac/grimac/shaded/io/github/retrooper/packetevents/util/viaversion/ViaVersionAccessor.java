package ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import org.bukkit.entity.Player;

public interface ViaVersionAccessor {
   int getProtocolVersion(Player player);

   int getProtocolVersion(User user);

   Class<?> getUserConnectionClass();

   Class<?> getBukkitDecodeHandlerClass();

   Class<?> getBukkitEncodeHandlerClass();
}
