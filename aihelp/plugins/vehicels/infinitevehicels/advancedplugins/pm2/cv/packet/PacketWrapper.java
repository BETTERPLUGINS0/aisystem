package advancedplugins.pm2.cv.packet;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.service.PacketService;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PacketWrapper {
   @Nullable
   public static PacketWrapper of(@NotNull Object handle) {
      return ((PacketService)Objects.requireNonNull((PacketService)InfiniteVehicles.getService(PacketService.class))).read(var0);
   }

   @NotNull
   public static Object createInstance(@NotNull PacketWrapper wrapper) {
      return ((PacketService)Objects.requireNonNull((PacketService)InfiniteVehicles.getService(PacketService.class))).createInstance(var0);
   }
}
