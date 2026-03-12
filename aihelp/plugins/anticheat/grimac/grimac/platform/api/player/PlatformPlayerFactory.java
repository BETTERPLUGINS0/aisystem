package ac.grim.grimac.platform.api.player;

import java.util.Collection;
import java.util.UUID;

public interface PlatformPlayerFactory {
   OfflinePlatformPlayer getOfflineFromUUID(UUID var1);

   OfflinePlatformPlayer getOfflineFromName(String var1);

   Collection<OfflinePlatformPlayer> getOfflinePlayers();

   PlatformPlayer getFromName(String var1);

   PlatformPlayer getFromUUID(UUID var1);

   PlatformPlayer getFromNativePlayerType(Object var1);

   void invalidatePlayer(UUID var1);

   Collection<PlatformPlayer> getOnlinePlayers();
}
