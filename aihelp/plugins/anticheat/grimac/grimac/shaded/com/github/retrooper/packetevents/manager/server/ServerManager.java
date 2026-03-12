package ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public interface ServerManager {
   ServerVersion getVersion();

   default SystemOS getOS() {
      return SystemOS.getOS();
   }

   @Nullable
   default Object getRegistryCacheKey(User user, ClientVersion version) {
      return null;
   }
}
