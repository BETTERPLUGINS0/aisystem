package me.SuperRonanCraft.BetterRTP.player.rtp;

import me.SuperRonanCraft.BetterRTP.references.depends.regionPlugins.REGIONPLUGINS;
import org.bukkit.Location;

public class RTPPluginValidation {
   public static boolean checkLocation(Location loc) {
      REGIONPLUGINS[] var1 = REGIONPLUGINS.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         REGIONPLUGINS validators = var1[var3];
         if (!validators.getValidator().check(loc)) {
            return false;
         }
      }

      return true;
   }
}
