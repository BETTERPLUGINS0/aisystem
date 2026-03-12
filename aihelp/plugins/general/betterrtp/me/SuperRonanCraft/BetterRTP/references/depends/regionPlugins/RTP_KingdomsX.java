package me.SuperRonanCraft.BetterRTP.references.depends.regionPlugins;

import org.bukkit.Location;
import org.kingdoms.constants.land.Land;

public class RTP_KingdomsX implements RegionPluginCheck {
   public boolean check(Location loc) {
      boolean result = true;
      if (REGIONPLUGINS.KINGDOMSX.isEnabled()) {
         try {
            Land land = Land.getLand(loc);
            result = land == null || !land.isClaimed();
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return result;
   }
}
