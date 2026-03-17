package advancedplugins.pm2.cv.api.player;

import io.netty.channel.ChannelPipeline;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PlayerWrapper {
   @NotNull
   UUID getUUID();

   @Nullable
   Player get();

   @Nullable
   ChannelPipeline getPipeline();

   boolean isOnline();

   boolean isOffline();
}
