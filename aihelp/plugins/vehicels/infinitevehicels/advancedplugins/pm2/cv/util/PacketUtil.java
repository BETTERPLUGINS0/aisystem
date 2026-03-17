package advancedplugins.pm2.cv.util;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import io.netty.channel.ChannelPipeline;
import me.PM2.infinitevehicles.math.util.FastMath;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketUtil {
   @Nullable
   public static ChannelPipeline getPipeline(@NotNull Player player) {
      return InfiniteVehicles.getPlayerWrapperHandler().getWrapper(var0).getPipeline();
   }

   public static long serializeDeltaLocation(double current, double last) {
      return (long)((var0 * 32.0D - var2 * 32.0D) * 128.0D);
   }

   public static byte serializeRotationAngle(float rotation) {
      return (byte)((int)FastMath.floor((double)(var0 * 256.0F / 360.0F)));
   }
}
