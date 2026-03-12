package me.SuperRonanCraft.BetterRTP.references.depends.regionPlugins;

import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.angeschossen.lands.api.LandsIntegration;
import org.bukkit.Location;

public class RTP_Lands implements RegionPluginCheck {
   public boolean check(Location loc) {
      boolean result = true;
      if (REGIONPLUGINS.LANDS.isEnabled()) {
         try {
            result = LandsIntegration.of(BetterRTP.getInstance()).getArea(loc) == null;
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return result;
   }
}
