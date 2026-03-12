package me.SuperRonanCraft.BetterRTP.references.depends.regionPlugins;

import net.william278.husktowns.api.HuskTownsAPI;
import org.bukkit.Location;

public class RTP_HuskTowns implements RegionPluginCheck {
   public boolean check(Location loc) {
      boolean result = true;
      if (REGIONPLUGINS.HUSKTOWNS.isEnabled()) {
         try {
            result = !HuskTownsAPI.getInstance().getClaimAt(loc).isPresent();
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return result;
   }
}
