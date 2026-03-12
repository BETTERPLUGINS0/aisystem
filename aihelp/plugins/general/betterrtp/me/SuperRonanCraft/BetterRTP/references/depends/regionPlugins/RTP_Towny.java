package me.SuperRonanCraft.BetterRTP.references.depends.regionPlugins;

import com.palmergames.bukkit.towny.TownyAPI;
import org.bukkit.Location;

public class RTP_Towny implements RegionPluginCheck {
   public boolean check(Location loc) {
      boolean result = true;
      if (REGIONPLUGINS.TOWNY.isEnabled()) {
         try {
            result = TownyAPI.getInstance().isWilderness(loc);
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return result;
   }
}
