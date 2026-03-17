package advancedplugins.pm2.cv.player;

import advancedplugins.pm2.cv.api.player.PlayerWrapper;
import io.netty.channel.ChannelPipeline;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PlayerWrapperImpl implements PlayerWrapper {
   @NotNull
   private final UUID uuid;
   @Nullable
   Player value;
   @Nullable
   ChannelPipeline pipeline;

   public PlayerWrapperImpl(@NotNull UUID uuid) {
      this.uuid = var1;
   }

   @NotNull
   public UUID getUUID() {
      return this.uuid;
   }

   @Nullable
   public Player get() {
      return this.value;
   }

   @Nullable
   public ChannelPipeline getPipeline() {
      return this.pipeline;
   }

   public boolean isOnline() {
      return this.value != null && this.value.isOnline();
   }

   public boolean isOffline() {
      return this.value == null || !this.value.isOnline();
   }
}
