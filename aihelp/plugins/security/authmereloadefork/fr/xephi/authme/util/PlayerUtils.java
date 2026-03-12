package fr.xephi.authme.util;

import org.bukkit.entity.Player;

public final class PlayerUtils {
   private static final boolean isLeavesServer = Utils.isClassLoaded("top.leavesmc.leaves.LeavesConfig") || Utils.isClassLoaded("org.leavesmc.leaves.LeavesConfig");

   private PlayerUtils() {
   }

   public static String getPlayerIp(Player player) {
      return player.getAddress().getAddress().getHostAddress();
   }

   public static boolean isNpc(Player player) {
      if (!isLeavesServer) {
         return player.hasMetadata("NPC");
      } else {
         return player.hasMetadata("NPC") || player.getAddress() == null;
      }
   }
}
