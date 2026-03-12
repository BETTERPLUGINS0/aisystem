package ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.protocolsupport;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import java.net.SocketAddress;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import protocolsupport.api.ProtocolSupportAPI;

public class ProtocolSupportUtil {
   private static ProtocolSupportState available;

   public static boolean isAvailable() {
      if (available == ProtocolSupportState.UNKNOWN) {
         try {
            ClassLoader classLoader = PacketEvents.getAPI().getPlugin().getClass().getClassLoader();
            classLoader.loadClass("protocolsupport.api.ProtocolSupportAPI");
            available = ProtocolSupportState.ENABLED;
            return true;
         } catch (Exception var1) {
            available = ProtocolSupportState.DISABLED;
            return false;
         }
      } else {
         return available == ProtocolSupportState.ENABLED;
      }
   }

   public static void checkIfProtocolSupportIsPresent() {
      boolean present = Bukkit.getPluginManager().isPluginEnabled("ProtocolSupport");
      available = present ? ProtocolSupportState.ENABLED : ProtocolSupportState.DISABLED;
   }

   public static int getProtocolVersion(SocketAddress address) {
      return ProtocolSupportAPI.getProtocolVersion(address).getId();
   }

   public static int getProtocolVersion(Player player) {
      return ProtocolSupportAPI.getProtocolVersion(player).getId();
   }

   static {
      available = ProtocolSupportState.UNKNOWN;
   }
}
