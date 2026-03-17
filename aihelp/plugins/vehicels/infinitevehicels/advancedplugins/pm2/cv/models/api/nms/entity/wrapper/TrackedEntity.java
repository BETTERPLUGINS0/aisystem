package advancedplugins.pm2.cv.models.api.nms.entity.wrapper;

import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface TrackedEntity {
   Predicate<Player> DEFAULT_PREDICATE = (player) -> {
      return true;
   };

   Entity getEntity();

   int getBaseRange();

   void setBaseRange(int var1);

   int getEffectiveRange();

   Set<UUID> getTrackedPlayer();

   Set<UUID> getTrackedPlayer(Predicate<Player> var1);

   void sendPairingData(Player var1);

   void broadcastSpawn();

   void broadcastRemove();

   void addForcedPairing(UUID var1);

   void removeForcedPairing(UUID var1);

   void addForcedHidden(UUID var1);

   void removeForcedHidden(UUID var1);

   @NotNull
   Predicate<Player> getPlayerPredicate();

   void setPlayerPredicate(@NotNull Predicate<Player> var1);
}
