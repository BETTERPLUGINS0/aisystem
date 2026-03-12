package me.SuperRonanCraft.BetterRTP.references.depends.regionPlugins;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;

public class RTP_GriefPrevention implements RegionPluginCheck {
   public boolean check(Location loc) {
      boolean result = true;
      if (REGIONPLUGINS.GRIEFPREVENTION.isEnabled()) {
         try {
            result = GriefPrevention.instance.dataStore.getClaimAt(loc, true, (Claim)null) == null;
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return result;
   }
}
