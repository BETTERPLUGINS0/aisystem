package me.SuperRonanCraft.BetterRTP.references.depends.regionPlugins;

import cc.javajobs.factionsbridge.FactionsBridge;
import org.bukkit.Location;

public class RTP_FactionsBridge implements RegionPluginCheck {
   public boolean check(Location loc) {
      boolean result = true;
      if (REGIONPLUGINS.FACTIONSBRIDGE.isEnabled()) {
         try {
            boolean claimed = FactionsBridge.getFactionsAPI().getClaim(loc).isClaimed();
            result = !claimed;
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return result;
   }
}
