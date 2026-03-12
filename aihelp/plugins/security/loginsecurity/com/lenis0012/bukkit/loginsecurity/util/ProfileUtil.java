package com.lenis0012.bukkit.loginsecurity.util;

import com.google.common.base.Charsets;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ProfileUtil {
   private static final UserIdMode userIdMode;

   public static UserIdMode getUserIdMode() {
      return userIdMode;
   }

   public static UUID getUUID(Player player) {
      return getUUID(player.getName(), player.getUniqueId());
   }

   public static UUID getUUID(String name, UUID fallback) {
      return useOnlineUUID() ? fallback : UUID.nameUUIDFromBytes(("OfflinePlayer:" + name.toLowerCase()).getBytes(Charsets.UTF_8));
   }

   public static boolean useOnlineUUID() {
      return Bukkit.getOnlineMode();
   }

   /** @deprecated */
   @Deprecated
   public static boolean isBungeecord() {
      return false;
   }

   static {
      userIdMode = useOnlineUUID() ? UserIdMode.MOJANG : UserIdMode.OFFLINE;
   }
}
