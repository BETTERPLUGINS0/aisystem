package ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ViaVersionUtil {
   private static ViaState available;
   private static ViaVersionAccessor viaVersionAccessor;

   private ViaVersionUtil() {
   }

   private static void load() {
      if (viaVersionAccessor == null) {
         ClassLoader classLoader = PacketEvents.getAPI().getPlugin().getClass().getClassLoader();

         try {
            classLoader.loadClass("com.viaversion.viaversion.api.Via");
            viaVersionAccessor = new ViaVersionAccessorImpl();
         } catch (Exception var4) {
            try {
               classLoader.loadClass("us.myles.ViaVersion.api.Via");
               viaVersionAccessor = new ViaVersionAccessorImplLegacy();
            } catch (ClassNotFoundException var3) {
               viaVersionAccessor = null;
            }
         }
      }

   }

   public static void checkIfViaIsPresent() {
      boolean present = Bukkit.getPluginManager().isPluginEnabled("ViaVersion");
      available = present ? ViaState.ENABLED : ViaState.DISABLED;
   }

   public static boolean isAvailable() {
      if (available == ViaState.UNKNOWN) {
         return getViaVersionAccessor() != null;
      } else {
         return available == ViaState.ENABLED;
      }
   }

   public static ViaVersionAccessor getViaVersionAccessor() {
      load();
      return viaVersionAccessor;
   }

   public static int getProtocolVersion(User user) {
      return getViaVersionAccessor().getProtocolVersion(user);
   }

   public static int getProtocolVersion(Player player) {
      return getViaVersionAccessor().getProtocolVersion(player);
   }

   public static Class<?> getUserConnectionClass() {
      return getViaVersionAccessor().getUserConnectionClass();
   }

   public static Class<?> getBukkitDecodeHandlerClass() {
      return getViaVersionAccessor().getBukkitDecodeHandlerClass();
   }

   public static Class<?> getBukkitEncodeHandlerClass() {
      return getViaVersionAccessor().getBukkitEncodeHandlerClass();
   }

   static {
      available = ViaState.UNKNOWN;
   }
}
