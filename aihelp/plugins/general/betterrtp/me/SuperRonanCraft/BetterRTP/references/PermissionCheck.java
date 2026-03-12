package me.SuperRonanCraft.BetterRTP.references;

import me.SuperRonanCraft.BetterRTP.BetterRTP;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public interface PermissionCheck {
   static String getPrefix() {
      return "betterrtp.";
   }

   default boolean check(CommandSender sendi) {
      if (!this.isDev()) {
         return BetterRTP.getInstance().getPerms().checkPerm(this.getNode(), sendi);
      } else {
         return sendi.getName().equalsIgnoreCase("SuperRonanCraft") || sendi.getName().equalsIgnoreCase("RonanCrafts");
      }
   }

   static boolean check(CommandSender sendi, String check) {
      return BetterRTP.getInstance().getPerms().checkPerm(check, sendi);
   }

   static boolean getAWorld(CommandSender sendi, String world) {
      return getAWorldText(sendi, world).passed;
   }

   static PermissionCheck.PermissionResult getAWorldText(CommandSender sendi, @NotNull String world) {
      String perm = getPrefix() + "world.*";
      if (check(sendi, perm)) {
         return new PermissionCheck.PermissionResult(perm, true);
      } else {
         perm = getPrefix() + "world." + world;
         return check(sendi, perm) ? new PermissionCheck.PermissionResult(perm, true) : new PermissionCheck.PermissionResult(perm, false);
      }
   }

   static boolean getLocation(CommandSender sendi, String location) {
      return check(sendi, getPrefix() + "location." + location);
   }

   static boolean getPermissionGroup(CommandSender sendi, String group) {
      return check(sendi, getPrefix() + "group." + group);
   }

   boolean isDev();

   String getNode();

   public static class PermissionResult {
      private final boolean passed;
      private final String string;

      PermissionResult(String string, boolean passed) {
         this.passed = passed;
         this.string = string;
      }

      public boolean isPassed() {
         return this.passed;
      }

      public String getString() {
         return this.string;
      }
   }
}
