package advancedplugins.pm2.cv.models.api.nms.network;

import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;

public interface NetworkHandler {
   int getProtocolVersion();

   Optional<PipelineWrapper> getPipeline(UUID var1);

   void removePipeline(UUID var1);

   void injectChannel(Player var1);

   void ejectChannel(Player var1);

   default void ping(UUID uuid) {
   }

   void startBatch();

   boolean isBatching();

   void endBatch();
}
