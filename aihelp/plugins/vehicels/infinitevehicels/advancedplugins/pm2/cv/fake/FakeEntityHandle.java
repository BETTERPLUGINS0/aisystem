package advancedplugins.pm2.cv.fake;

import advancedplugins.pm2.cv.api.player.PlayerWrapper;
import io.netty.channel.ChannelPipeline;
import java.util.Collection;
import java.util.Collections;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FakeEntityHandle<P extends Enum<P>> {
   int getId();

   boolean isShownTo(@NotNull Player player);

   void show(@NotNull Collection<? extends Player> players);

   default void show(@NotNull Player player) {
      this.show((Collection)Collections.singletonList(player));
   }

   void hide(@NotNull Collection<? extends Player> players, boolean disconnected);

   default void hide(@NotNull Collection<? extends Player> players) {
      this.hide(players, false);
   }

   default void hide(@NotNull Player player, boolean disconnected) {
      this.hide((Collection)Collections.singletonList(player), disconnected);
   }

   default void hide(@NotNull Player player) {
      this.hide(player, false);
   }

   void hide();

   void sendMetadata(boolean all);

   default void sendMetadata() {
      this.sendMetadata(false);
   }

   void applyProperty(@NotNull P property, @NotNull Object value);

   void applyLocationRotation(double x, double y, double z, float yaw, float pitch);

   void applyLocation(double x, double y, double z);

   void applyRotation(float yaw, float pitch);

   void applyHeadRotation(float headRotation);

   void sendLocationRotation(boolean forceTeleport);

   void sendLocation(boolean forceTeleport);

   void sendRotation();

   void sendHeadRotation();

   void writeShowPackets(@NotNull PlayerWrapper viewer, boolean registerViewer);

   void writeHidePackets(@NotNull PlayerWrapper viewer, boolean unregisterViewer);

   @NotNull
   Collection<ChannelPipeline> writeMetadata(boolean all);

   @NotNull
   Collection<ChannelPipeline> writeLocationRotation(boolean forceTeleport);

   @NotNull
   Collection<ChannelPipeline> writeLocation(boolean forceTeleport);

   @NotNull
   Collection<ChannelPipeline> writeRotation();

   @Nullable
   FakeEntityHandle<?> getRiding();

   void setRiding(@Nullable FakeEntityHandle<?> riding);

   void setPassengers(@Nullable Collection<FakeEntityHandle<?>> passengers);

   void sendPassengers();

   void sendPassengersTo(@NotNull Player player);
}
