package ac.grim.grimac.utils.reflection;

import java.util.UUID;
import lombok.Generated;
import org.geysermc.api.Geyser;
import org.geysermc.floodgate.api.FloodgateApi;

public final class GeyserUtil {
   private static final boolean floodgate = ReflectionUtils.hasClass("org.geysermc.floodgate.api.FloodgateApi");
   private static final boolean geyser = ReflectionUtils.hasClass("org.geysermc.api.Geyser");

   public static boolean isBedrockPlayer(UUID uuid) {
      return floodgate && FloodgateApi.getInstance().isFloodgatePlayer(uuid) || geyser && Geyser.api().isBedrockPlayer(uuid);
   }

   @Generated
   private GeyserUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
