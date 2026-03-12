package me.SuperRonanCraft.BetterRTP.references.depends.regionPlugins;

import net.crashcraft.crashclaim.CrashClaim;
import org.bukkit.Location;

public class RTP_CrashClaim implements RegionPluginCheck {
   public boolean check(Location loc) {
      boolean result = true;
      if (REGIONPLUGINS.CRASH_CLAIM.isEnabled()) {
         try {
            result = CrashClaim.getPlugin().getApi().getClaim(loc) == null;
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return result;
   }
}
