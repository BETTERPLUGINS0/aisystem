package ac.grim.grimac.platform.bukkit.utils.anticheat;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.reflection.ReflectionUtils;
import java.lang.reflect.Method;
import org.bukkit.entity.Player;

public class MultiLibUtil {
   public static final Method externalPlayerMethod = ReflectionUtils.getMethod(Player.class, "isExternalPlayer");
   private static final boolean IS_PRE_1_18;

   public static boolean isExternalPlayer(Player player) {
      if (externalPlayerMethod != null && !IS_PRE_1_18) {
         try {
            return (Boolean)externalPlayerMethod.invoke(player);
         } catch (Exception var2) {
            LogUtil.error("Failed to invoke external player method", var2);
            return false;
         }
      } else {
         return false;
      }
   }

   static {
      IS_PRE_1_18 = PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_18);
   }
}
